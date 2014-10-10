package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.dao.GameVersionDao;
import org.cri.redmetrics.json.GameVersionJsonConverter;
import org.cri.redmetrics.model.GameVersion;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class GameVersionController extends Controller<GameVersion, GameVersionDao> {

    @Inject
    GameVersionController(GameVersionDao dao, GameVersionJsonConverter jsonConverter) {
        super("/gameVersion", dao, jsonConverter);
    }

    @Override
    protected void beforeCreation(GameVersion gameVersion, Request request, Response response) {
        if (gameVersion.getGame() == null) halt(400, "game required (integer)");
    }

}