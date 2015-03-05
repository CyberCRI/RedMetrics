package org.cri.redmetrics.controller;

import com.google.common.collect.ObjectArrays;
import com.google.inject.Inject;
import org.cri.redmetrics.csv.CsvEntityConverter;
import org.cri.redmetrics.dao.EventDao;
import org.cri.redmetrics.dao.SearchQuery;
import org.cri.redmetrics.json.EventJsonConverter;
import org.cri.redmetrics.model.Event;
import spark.Request;
import spark.Response;

public class EventController extends ProgressDataController<Event, EventDao> {

    private static final String[] VALUES;

    static {
        String[] values = {"coordinates"};
        VALUES = ObjectArrays.concat(ProgressDataController.VALUES, values, String.class);
    }

    @Inject
    EventController(EventDao dao, EventJsonConverter jsonConverter, CsvEntityConverter<Event> csvEntityConverter) {
        super("event", dao, jsonConverter, csvEntityConverter);
    }

    @Override
    protected String[] searchableValues() {
        return VALUES;
    }

    @Override
    protected void beforeCreation(Event event, Request request, Response response) {
        super.beforeCreation(event, request, response);
        if (event.getType() == null) throw new IllegalArgumentException("type is required (string)");
    }

    protected void searchValues(Request request, SearchQuery search) {
        super.searchValues(request, search);

    }
}
