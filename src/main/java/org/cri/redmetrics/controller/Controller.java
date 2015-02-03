package org.cri.redmetrics.controller;

import org.cri.redmetrics.Server;
import org.cri.redmetrics.dao.EntityDao;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ResultsPage;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static spark.Spark.*;

public abstract class Controller<E extends Entity, DAO extends EntityDao<E>> {

    public static final String basePath = "/v1/";
    public static final long maxListCount = 200;

    protected final String path;
    protected final DAO dao;
    protected final JsonConverter<E> jsonConverter;

    Controller(String path, DAO dao, JsonConverter<E> jsonConverter) {
        this.path = basePath + path;
        this.dao = dao;
        this.jsonConverter = jsonConverter;
    }

    public final void publish() {
        publishGeneric();
        publishSpecific();
    }

    protected UUID idFromUrl(Request request) {
        String idParam = request.params(":id");
        return Entity.parseId(idParam);
    }

    protected UUID idFromQueryParam(Request request, String key) {
        String idParam = request.queryParams(key);
        return Entity.parseId(idParam);
    }

    private void publishGeneric() {

        // POST

        Route postRoute = (request, response) -> {
//            Console.log("Request body : " + request.body());
            E entity = jsonConverter.parse(request.body());
            beforeCreation(entity, request, response);
            create(entity);
            response.header("Location", path + "/" + entity.getId());
            response.status(201); // Created
            return entity;
        };

        post(path + "", postRoute, jsonConverter);
        post(path + "/", postRoute, jsonConverter);


        // GET

        Route getByIdRoute = (request, response) -> {
            E entity = read(idFromUrl(request));
            if (entity == null) halt(404);
            return entity;
        };

        get(path + "/:id", getByIdRoute, jsonConverter);
        get(path + "/:id/", getByIdRoute, jsonConverter);

        Route listRoute = (Request request, Response response) -> {
            // Figure out how many entities to return
            long startAt = request.queryMap("start").hasValue() ? request.queryMap("start").longValue() : 0;
            long count = request.queryMap("count").hasValue() ? request.queryMap("count").longValue() : maxListCount;
            ResultsPage<E> resultsPage = list(startAt, count);

            // Send the pagination headers
            response.header("X-Total-Count", Long.toString(resultsPage.total));
            response.header("Link", makeLinkHeaders(resultsPage));

            // Return the actual results (to be converted to JSON)
            return resultsPage;
        };

        get(path + "/", listRoute, jsonConverter);


        // PUT

        Route putRoute = (request, response) -> {
            E entity = jsonConverter.parse(request.body());
            UUID id = idFromUrl(request);
            if (entity.getId() != null && !entity.getId().equals(id)) {
                halt(400, "IDs in URL and body do not match");
            } else {
                entity.setId(id);
            }
            return update(entity);
        };

        put(path + "/:id", putRoute, jsonConverter);
        put(path + "/:id/", putRoute, jsonConverter);


        // DELETE

        delete(path + "/:id", (request, response) -> dao.delete(idFromUrl(request)), jsonConverter);
        delete(path + "/:id/", (request, response) -> dao.delete(idFromUrl(request)), jsonConverter);


        // OPTIONS
        // Always return empty response with CORS headers
        Route optionsRoute = (request, response) -> { return "{}"; };
        options(path + "", optionsRoute);
        options(path + "/", optionsRoute);
        options(path + "/:id", optionsRoute);
        options(path + "/:id/", optionsRoute);
    }

    protected E create(E entity) {
        return dao.create(entity);
    }

    protected E read(UUID id) {
        return dao.read(id);
    }

    protected E update(E entity) {
        return dao.update(entity);
    }

    protected ResultsPage<E> list(long startAt, long count) { return dao.list(startAt, count); }

    protected void publishSpecific() {
    }

    protected void beforeCreation(E entity, Request request, Response response) {
    }

    String makeLinkHeaders(ResultsPage<E> resultsPage) {
        final String prefix = Server.hostName + path + "/";

        ArrayList<String> linkHeaderArray = new ArrayList<String>();
        if (resultsPage.start > 0) {
            // Add first header
            linkHeaderArray.add(prefix + "?start=0&count=" + resultsPage.count + "; rel=first");
            // Add previous header
            long previousStart = Long.max(0, resultsPage.start - resultsPage.count);
            linkHeaderArray.add(prefix + "?start=" + previousStart + "&count=" + resultsPage.count + "; rel=prev");
        }

        if (resultsPage.start + resultsPage.count < resultsPage.total) {
            // Add next header
            long nextStart = resultsPage.start + resultsPage.count;
            linkHeaderArray.add(prefix + "?start=" + nextStart + "&count=" + resultsPage.count + "; rel=next");
            // Add last header
            long lastStart = resultsPage.total - resultsPage.count;
            linkHeaderArray.add(prefix + "?start=" + lastStart + "&count=" + resultsPage.count + "; rel=last");
        }

        return linkHeaderArray.stream().collect(Collectors.joining(", "));
    }
}
