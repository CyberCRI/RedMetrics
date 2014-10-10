package org.cri.redmetrics.controller;

import com.google.common.base.Splitter;
import org.cri.redmetrics.dao.ProgressDataDao;
import org.cri.redmetrics.dao.SearchQuery;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ProgressData;
import org.cri.redmetrics.util.DateFormatter;
import spark.Request;
import spark.Response;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import static spark.Spark.get;
import static spark.Spark.halt;

public abstract class ProgressDataController<E extends ProgressData, DAO extends ProgressDataDao<E>> extends Controller<E, DAO> {

    protected static final String[] FOREIGN_ENTITIES = {"gameVersion", "player"};
    protected static final String[] VALUES = {"type"};

    private static final Splitter SPLITTER = Splitter.on(',')
            .trimResults()
            .omitEmptyStrings();

    ProgressDataController(String path, DAO dao, JsonConverter<E> jsonConverter) {
        super(path, dao, jsonConverter);
    }

    protected abstract String[] searchableValues();

    @Override
    protected void beforeCreation(E progressData, Request request, Response response) {
        if (progressData.getGameVersion() == null) halt(400, "game version required (integer)");
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
            searchDates(request, search);
            searchSections(request, search);
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

    protected void searchValues(Request request, SearchQuery search) {
        for (String columnName : searchableValues()) {
            String params = request.queryParams(columnName);
            if (params != null && !params.isEmpty()) {
                search.value(columnName, params);
            }
        }
    }

    private void searchDates(Request request, SearchQuery search) {
        // BEFORE
        String beforeParam = request.queryParams("before");
        if (beforeParam != null) {
            Date date = DateFormatter.parse(beforeParam);
            search.before(date);
        }
        // AFTER
        String afterParam = request.queryParams("after");
        if (afterParam != null) {
            Date date = DateFormatter.parse(afterParam);
            search.after(date);
        }
        // BEFORE USER TIME
        String beforeUserTime = request.queryParams("beforeUserTime");
        if (beforeUserTime != null) {
            Date date = DateFormatter.parse(beforeUserTime);
            search.beforeUserTime(date);
        }
        // AFTER USER TIME
        String afterUserTime = request.queryParams("afterUserTime");
        if (afterUserTime != null) {
            Date date = DateFormatter.parse(afterUserTime);
            search.afterUserTime(date);
        }
    }

    private void searchSections(Request request, SearchQuery search) {
        String sectionsParam = request.queryParams("sections");
        if (sectionsParam != null && !sectionsParam.isEmpty()) {
            sectionsParam = sectionsParam.trim();
            search.sections(sectionsParam);
        }
    }

}