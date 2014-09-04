package org.cri.redmetrics.controller;

import org.cri.redmetrics.dao.EntityDao;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.Entity;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.UUID;

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

    protected UUID idFromUrl(Request request) {
        String idParam = request.params(":id");
        try {
            return UUID.fromString(idParam);
        } catch (IllegalArgumentException e) {
            halt(400, "ID illegal format - expected UUID");
            return null;
        }
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
            E entity = read(idFromUrl(request));
            if (entity == null) halt(404);
            return entity;
        }, jsonConverter);

        get(path + "/", (request, response) -> list()
                , jsonConverter);

        put(path + "/:id", (request, response) -> {
            E entity = jsonConverter.parse(request.body());
            UUID id = idFromUrl(request);
            if (entity.getId() != null && !entity.getId().equals(id)) {
                halt(400, "IDs in URL and body do not match");
            } else {
                entity.setId(id);
            }
            return update(entity);
        }, jsonConverter);

        delete(path + "/:id", (request, response) -> dao.delete(idFromUrl(request)), jsonConverter);

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

    protected List<E> list() {
        return dao.list();
    }

    protected void publishSpecific() {
    }

    protected void beforeCreation(E entity, Request request, Response response) {
    }
}
