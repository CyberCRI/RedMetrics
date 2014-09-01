package org.cri.redmetrics.model;

import com.google.api.client.util.Value;

public enum EventType {
    @Value("Start")
    START,
    
    @Value("End")
    END,
    
    @Value("Win")
    WIN,
    
    @Value("Fail")
    FAIL,
    
    @Value("Restart")
    RESTART,
    
    @Value("Gain")
    GAIN,
    
    @Value("Lose")
    LOSE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
