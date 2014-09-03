package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.dao.EventDao;
import org.cri.redmetrics.json.EventJsonConverter;
import org.cri.redmetrics.model.Event;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class EventController extends ProgressDataController<Event, EventDao> {

    @Inject
    EventController(EventDao dao, EventJsonConverter jsonConverter) {
        super("/event", dao, jsonConverter);
    }

    @Override
    protected void beforeCreation(Event event, Request request, Response response) {
        super.beforeCreation(event, request, response);
        if (event.getType() == null) halt(400, "type is required (string)");
    }
}
