package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.dao.EventDao;
import org.cri.redmetrics.json.EventJsonConverter;
import org.cri.redmetrics.model.Event;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class EventController extends Controller<Event, EventDao> {

    @Inject
    EventController(EventDao dao, EventJsonConverter json) {
        super("/event", dao, json);
    }

    @Override
    protected void beforeCreation(Event entity, Request request, Response response) {
        if (entity.getGame() == null) halt(400, "game required (integer");
        if (entity.getPlayer() == null) halt(400, "player required (integer)");
        if (entity.getType() == null) halt(400, "type is required (string)");
//        String adminKey = request.params("adminKey");
//        if (adminKey == null) halt();
    }
}
