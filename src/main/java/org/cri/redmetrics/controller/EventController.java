package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.dao.EventDao;
import org.cri.redmetrics.json.EventJsonConverter;
import org.cri.redmetrics.model.Event;

public class EventController extends Controller<Event, EventDao> {

    @Inject
    EventController(EventDao dao, EventJsonConverter json) {
        super("/event", dao, json);
    }

}
