package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestEvent;

public class EventBackend extends ProgressDataBackend<TestEvent> {

    EventBackend() {
        super("event", TestEvent.class);
    }

}
