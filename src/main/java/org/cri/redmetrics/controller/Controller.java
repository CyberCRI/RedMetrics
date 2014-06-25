package org.cri.redmetrics.controller;

import lombok.RequiredArgsConstructor;
import org.cri.redmetrics.dao.EntityDao;
import org.cri.redmetrics.json.Json;
import org.cri.redmetrics.model.Entity;

import java.util.List;

import static spark.Spark.*;

@RequiredArgsConstructor
public abstract class Controller<E extends Entity, DAO extends EntityDao<E>> {

    protected final String path;
    protected final DAO dao;
    protected final Json<E> json;

    public final void publish() {
        publishGeneric();
        publishSpecific();
    }

    private void publishGeneric() {

        post(path + "/", "application/json", (request, response) -> {
            E entity = json.parse(request.body());
            create(entity);
            response.status(201); // Created
            return entity;
        }, json);

        get(path + "/:id", (request, response) -> {
            E entity = read(Integer.parseInt(request.params(":id")));
            if (entity == null) halt(404);
            return entity;
        }, json);

        get(path + "/", (request, response) -> list()
                , json);

        put(path + "/:id", (request, response) -> {
            E entity = json.parse(request.body());
            int urlId = Integer.parseInt(request.params(":id"));
            if (urlId < 0) halt(400, "ID should not be negative");
            if (urlId != entity.getId()) {
                halt(400, "IDs in URL and body do not match");
            } else {
                entity.setId(urlId);
            }
            return dao.update(entity);
        }, json);

        delete(path + "/:id", (request, response) -> {
            return dao.delete(Integer.parseInt(request.params(":id")));
        }, json);

    }

    protected E create(E entity) {
        return dao.create(entity);
    }

    protected E read(int id) {
        return dao.read(id);
    }

    protected List<E> list() {
        return dao.list();
    }

    protected void publishSpecific() {
    }

}
