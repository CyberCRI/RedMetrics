package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.csv.CsvEntityConverter;
import org.cri.redmetrics.dao.GameVersionDao;
import org.cri.redmetrics.json.GameVersionJsonConverter;
import org.cri.redmetrics.model.GameVersion;
import org.cri.redmetrics.util.RouteHelper;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.halt;

public class GameVersionController extends Controller<GameVersion, GameVersionDao> {

    @Inject
    GameVersionController(GameVersionDao dao, GameVersionJsonConverter jsonConverter, CsvEntityConverter<GameVersion> csvEntityConverter) {
        super("gameVersion", dao, jsonConverter, csvEntityConverter);
    }

    @Override
    protected void beforeCreation(GameVersion gameVersion, Request request, Response response) {
        if (gameVersion.getGame() == null) throw new IllegalArgumentException("game required");
    }

    @Override
    protected void publishSpecific() {
        super.publishSpecific();
        Route findByGameId = (request, response) -> {
            UUID gameId = idFromUrl(request);
            return dao.searchByGameId(gameId);
        };

        routeHelper.publishRouteSet(RouteHelper.HttpVerb.GET, basePath + "game/:id/versions", findByGameId);
    }

}