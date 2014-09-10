package org.cri.redmetrics.controller;

import org.cri.redmetrics.dao.ProgressDataDao;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.ProgressData;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.halt;

public abstract class ProgressDataController<E extends ProgressData, DAO extends ProgressDataDao<E>> extends Controller<E, DAO> {

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

    @Override
    protected void publishSpecific() {
        super.publishSpecific();

        // SEARCH
        get(path, (request, response) -> {
            boolean hasGame = request.queryParams().contains("game");
            boolean hasPlayer = request.queryParams().contains("player");
            if (hasGame) {
                UUID gameId = idFromQueryParam(request, "game");
                return dao.searchByGame(gameId);
            } else if (hasPlayer) {
                UUID playerId = idFromQueryParam(request, "player");
                return dao.searchByPlayer(playerId);
            } else return list();
        }
                , jsonConverter);
    }
}
