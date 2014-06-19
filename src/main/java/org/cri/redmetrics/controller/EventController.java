package org.cri.redmetrics.controller;

import com.google.gson.JsonElement;
import com.google.inject.Inject;
import org.cri.redmetrics.dao.EventDao;
import org.cri.redmetrics.json.EventJson;
import org.cri.redmetrics.model.Event;

public class EventController extends Controller<Event, EventDao> {

    @Inject
    EventController(EventDao dao, EventJson json) {
        super("/event", dao, json);
    }

}
