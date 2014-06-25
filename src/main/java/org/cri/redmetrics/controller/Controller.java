package org.cri.redmetrics.controller;

import lombok.RequiredArgsConstructor;
import org.cri.redmetrics.dao.EntityDao;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.Entity;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static spark.Spark.*;

@RequiredArgsConstructor
public abstract class Controller<E extends Entity, DAO extends EntityDao<E>> {

    protected final String path;
    protected final DAO dao;
    protected final JsonConverter<E> jsonConverter;

    public final void publish() {
        publishGeneric();
        publishSpecific();
    }

    private void publishGeneric() {

        post(path + "/", (request, response) -> {
            E entity = jsonConverter.parse(request.body());
            create(entity);
            response.status(201); // Created
            return entity;
        }, jsonConverter);

        /*post(path + "/", new Route() {
            @Override
            public Object handle(Request request, Response response) {
                E entity = jsonConverter.parse(request.body());
                create(entity);
                response.status(201); // Created
                return entity;
            }
        });*/

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
            return dao.update(entity);
        }, jsonConverter);

        delete(path + "/:id", (request, response) -> {
            return dao.delete(Integer.parseInt(request.params(":id")));
        }, jsonConverter);

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
