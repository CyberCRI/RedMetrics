package org.cri.redmetrics.controller;

import org.cri.redmetrics.dao.ProgressDataDao;
import org.cri.redmetrics.dao.SearchQuery;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ProgressData;
import spark.Request;
import spark.Response;

import java.util.*;

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
            SearchQuery search = dao.search();
            addGameParams(request, search);
            addPlayerParam(request, search);
            addTypeParam(request, search);
            return search.execute();
        }
                , jsonConverter);
    }

    private void addGameParams(Request request, SearchQuery search) {
        if (request.queryParams().contains("game")) {
            if (request.queryParams("game").contains(",")) {
                addGameIdList(request, search);
            } else {
                search.game(idFromQueryParam(request, "game"));
            }
        }
    }

    private void addGameIdList(Request request, SearchQuery search) {
        Iterator<String> i = Arrays.asList(request.queryParams("game").split(",")).iterator();
        List<UUID> gameIds = new ArrayList<>();
        while (i.hasNext()) {
            gameIds.add(Entity.parseId(i.next()));
        }
        search.games(gameIds);
    }

    private void addPlayerParam(Request request, SearchQuery search) {
        if (request.queryParams().contains("player")) {
            search.player(idFromQueryParam(request, "player"));
        }
    }

    private void addTypeParam(Request request, SearchQuery search) {
        if (request.queryParams().contains("type")) {
            search.type(request.queryParams("type"));
        }
    }
}