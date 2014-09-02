package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestEvent;

public class EventBackend extends HttpBackend<TestEvent> {

    public EventBackend() {
        super("event/", TestEvent.class);
    }

}
