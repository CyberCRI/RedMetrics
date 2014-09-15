package org.cri.redmetrics.controller;

import com.google.common.base.Splitter;
import org.cri.redmetrics.dao.ProgressDataDao;
import org.cri.redmetrics.dao.SearchQuery;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ProgressData;
import spark.Request;
import spark.Response;

import java.util.UUID;
import java.util.stream.Stream;

import static spark.Spark.get;
import static spark.Spark.halt;

public abstract class ProgressDataController<E extends ProgressData, DAO extends ProgressDataDao<E>> extends Controller<E, DAO> {

    private static final String[] FOREIGN_ENTITIES = {"game", "player"};
    private static final String[] VALUES = {"type"};

    private static final Splitter SPLITTER = Splitter.on(',')
            .trimResults()
            .omitEmptyStrings();

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
            searchForeignEntities(request, search);
            searchValues(request, search);
            return search.execute();
        }
                , jsonConverter);
    }

    private void searchForeignEntities(Request request, SearchQuery search) {
        for (String foreignEntityName : FOREIGN_ENTITIES) {
            String params = request.queryParams(foreignEntityName);
            if (params != null) {
                search.foreignEntity(foreignEntityName, parseIds(params));
            }
        }
    }

    private Stream<UUID> parseIds(String ids) {
        return SPLITTER.splitToList(ids)
                .stream()
                .map(Entity::parseId);
    }

    private void searchValues(Request request, SearchQuery search) {
        for (String columnName : VALUES) {
            String params = request.queryParams(columnName);
            if (params != null) {
                search.value(columnName, params);
            }
        }
    }

}