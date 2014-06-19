package org.cri.redmetrics.model;

public enum EventType {

    START,
    END,
    WIN,
    FAIL,
    RESTART,
    GAIN,
    LOSE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
