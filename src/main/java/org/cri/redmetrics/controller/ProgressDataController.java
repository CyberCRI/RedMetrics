package org.cri.redmetrics.controller;

import com.google.common.base.Splitter;
import org.cri.redmetrics.csv.CsvEntityConverter;
import org.cri.redmetrics.dao.ProgressDataDao;
import org.cri.redmetrics.dao.SearchQuery;
import org.cri.redmetrics.json.JsonConverter;
import org.cri.redmetrics.model.BinCount;
import org.cri.redmetrics.model.Entity;
import org.cri.redmetrics.model.ProgressData;
import org.cri.redmetrics.model.ResultsPage;
import org.cri.redmetrics.util.DateFormatter;
import org.cri.redmetrics.util.RouteHelper;
import spark.Request;
import spark.Response;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static spark.Spark.halt;

public abstract class ProgressDataController<E extends ProgressData, DAO extends ProgressDataDao<E>> extends Controller<E, DAO> {

    protected static final String[] FOREIGN_ENTITIES = {"gameVersion", "player"};
    protected static final String[] VALUES = {"type"};

    private static final Splitter SPLITTER = Splitter.on(',')
            .trimResults()
            .omitEmptyStrings();

    ProgressDataController(String path, DAO dao, JsonConverter<E> jsonConverter, CsvEntityConverter<E> csvEntityConverter) {
        super(path, dao, jsonConverter, csvEntityConverter);
    }

    protected abstract String[] searchableValues();

    @Override
    protected void beforeCreation(E progressData, Request request, Response response) {
        // Required fields
        if (progressData.getGameVersion() == null) throw new IllegalArgumentException("game version required");
        if (progressData.getPlayer() == null) throw new IllegalArgumentException("player required (integer)");

        // Fill default values
        if (progressData.getType() == null) progressData.setType("default");
    }

    @Override
    protected ResultsPage<E> list(Request request, long page, long perPage) {
        // SEARCH
        SearchQuery search = dao.search();
        searchGame(request, search);
        searchForeignEntities(request, search);
        searchValues(request, search);
        searchDates(request, search);
        searchSection(request, search);
        searchPlayerExternalId(request, search);

        long totalResultCount = search.countResults();

        search.paginate(page, perPage);
        setSearchOrder(request, search);

        List<E> results = search.execute();

        return new ResultsPage<E>(totalResultCount, page, perPage, results);
    }

    @Override
    protected List<E> listAllPages(Request request) {
        // SEARCH
        SearchQuery search = dao.search();
        searchGame(request, search);
        searchForeignEntities(request, search);
        searchValues(request, search);
        searchDates(request, search);
        searchSection(request, search);
        searchPlayerExternalId(request, search);

        return search.execute();
    }

    protected List<BinCount> countResultsOverTime(Request request) {
        // SEARCH
        SearchQuery search = dao.search();
        searchGame(request, search);
        searchForeignEntities(request, search);
        searchValues(request, search);
        searchSection(request, search);
        searchPlayerExternalId(request, search);

        Date minDate = getMinDate(request);
        if(minDate == null) {
            // Need to repeat this whole process in order to follow builder pattern

            SearchQuery dateSearch = dao.search();
            searchGame(request, dateSearch);
            searchForeignEntities(request, dateSearch);
            searchValues(request, dateSearch);
            searchSection(request, dateSearch);
            searchPlayerExternalId(request, dateSearch);

            minDate = dateSearch.getMinTime();

            // If there is no minimum date, then there's no data!
            // For now, just return the first possible date
            if(minDate == null) minDate = new Date(0);
        }

        // TODO: is this in UTC time?
        Date maxDate = getMaxDate(request);
        if(maxDate == null) maxDate = new Date();

        return search.countResultsOverTime(minDate, maxDate, 100);
    }

    @Override
    protected void publishSpecific() {
        routeHelper.publishRouteSet(RouteHelper.HttpVerb.GET, RouteHelper.DataType.BIN_COUNT_LIST, path + "-count", (request, response) -> {
            return countResultsOverTime(request);
        });

        routeHelper.publishOptionsRouteSet(path + "-count");
    }

    private void searchGame(Request request, SearchQuery search) {
        String params = request.queryParams("game");
        if (params != null) {
            search.game(parseIds(params));
        }
    }

    private void searchForeignEntities(Request request, SearchQuery search) {
        for (String foreignEntityName : FOREIGN_ENTITIES) {
            String params = request.queryParams(foreignEntityName);
            if (params != null) {
                search.foreignEntity(foreignEntityName, parseIds(params));
            }
        }
    }

    private void searchPlayerExternalId(Request request, SearchQuery search) {
        String params = request.queryParams("playerExternalId");
        if (params != null) {
            search.playerExternalId(params);
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
            Date date = DateFormatter.parseIso(beforeParam);
            search.before(date);
        }
        // AFTER
        String afterParam = request.queryParams("after");
        if (afterParam != null) {
            Date date = DateFormatter.parseIso(afterParam);
            search.after(date);
        }
        // BEFORE USER TIME
        String beforeUserTime = request.queryParams("beforeUserTime");
        if (beforeUserTime != null) {
            Date date = DateFormatter.parseIso(beforeUserTime);
            search.beforeUserTime(date);
        }
        // AFTER USER TIME
        String afterUserTime = request.queryParams("afterUserTime");
        if (afterUserTime != null) {
            Date date = DateFormatter.parseIso(afterUserTime);
            search.afterUserTime(date);
        }
    }

    private void searchSection(Request request, SearchQuery search) {
        String sectionParam = request.queryParams("section");
        if (sectionParam != null && !sectionParam.isEmpty()) {
            sectionParam = sectionParam.trim();
            search.section(sectionParam);
        }
    }

    private void setSearchOrder(Request request, SearchQuery search) {
        String orderByParam = request.queryParams("orderBy");
        if (orderByParam == null || orderByParam.isEmpty()) {
            // Default ordering by server time
            search.orderBy("serverTime", true);
        } else {
            // split the parameter into columnName(:(asc|desc))? (defaults to asc)
            String[] split = orderByParam.split(":", 2);

            boolean ascending = !(split.length == 2 && split[1].equals("desc"));

            search.orderBy(split[0], ascending);
        }
    }

    private Date getMinDate(Request request) {
        // AFTER
        String afterParam = request.queryParams("after");
        if (afterParam != null) {
            return DateFormatter.parseIso(afterParam);
        }
        // AFTER USER TIME
        String afterUserTime = request.queryParams("afterUserTime");
        if (afterUserTime != null) {
            return DateFormatter.parseIso(afterUserTime);
        }

        return null;
    }

    private Date getMaxDate(Request request) {
        // BEFORE
        String beforeParam = request.queryParams("before");
        if (beforeParam != null) {
            return DateFormatter.parseIso(beforeParam);
        }
        // BEFORE USER TIME
        String beforeUserTime = request.queryParams("beforeUserTime");
        if (beforeUserTime != null) {
            return DateFormatter.parseIso(beforeUserTime);
        }

        return null;
    }
}