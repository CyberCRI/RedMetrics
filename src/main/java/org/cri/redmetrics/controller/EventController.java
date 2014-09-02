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
        if (entity.getGame() == null) halt(400, "required Game ID");
        if (entity.getPlayer() == null) halt(400, "required Player ID");
        if (entity.getType() == null) halt(400, "required event type (START, END, WIN, FAIL, RESTART, GAIN or LOSE");
//        String adminKey = request.params("adminKey");
//        if (adminKey == null) halt();
    }
}
