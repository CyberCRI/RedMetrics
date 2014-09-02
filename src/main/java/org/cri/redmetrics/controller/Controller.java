package org.cri.redmetrics.controller;

import org.cri.redmetrics.dao.EntityDao;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.Entity;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.*;

public abstract class Controller<E extends Entity, DAO extends EntityDao<E>> {

    public static final String basePath = "/v1/";

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

    private void publishGeneric() {

        post(path + "/", (request, response) -> {
            // System.out.println("Request body : " + request.body());
            E entity = jsonConverter.parse(request.body());
            beforeCreation(entity, request, response);
            create(entity);
            response.status(201); // Created
            return entity;
        }, jsonConverter);

        get(path + "/:id", (request, response) -> {
            E entity = read(Integer.parseInt(request.params(":id")));
            if (entity == null) halt(404);
            return entity;
        }, jsonConverter);

        get(path + "/", (request, response) -> list()
                , jsonConverter);

        put(path + "/:id", (request, response) -> {
            E entity = jsonConverter.parse(request.body());
            int urlId = Integer.parseInt(request.params(":id"));
            if (urlId < 0) halt(400, "ID should not be negative");
            if (urlId != entity.getId()) {
                halt(400, "IDs in URL and body do not match");
            } else {
                entity.setId(urlId);
            }
            return update(entity);
        }, jsonConverter);

        delete(path + "/:id", (request, response) -> dao.delete(Integer.parseInt(request.params(":id"))), jsonConverter);

    }

    protected E create(E entity) {
        return dao.create(entity);
    }

    protected E read(int id) {
        return dao.read(id);
    }

    protected E update(E entity) {
        return dao.update(entity);
    }

    protected List<E> list() {
        return dao.list();
    }

    protected void publishSpecific() {
    }

    protected void beforeCreation(E entity, Request request, Response response) {
    }
}
