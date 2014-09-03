package org.cri.redmetrics.controller;

import org.cri.redmetrics.dao.EntityDao;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.ProgressData;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public abstract class ProgressDataController<E extends ProgressData, DAO extends EntityDao<E>> extends Controller<E, DAO> {

    ProgressDataController(String path, DAO dao, JsonConverter<E> jsonConverter) {
        super(path, dao, jsonConverter);
    }

    @Override
    protected void beforeCreation(E progressData, Request request, Response response) {
        if (progressData.getGame() == null) halt(400, "game required (integer)");
        if (progressData.getPlayer() == null) halt(400, "player required (integer)");
//        String adminKey = request.params("adminKey");
//        if (adminKey == null) halt();
    }

}
