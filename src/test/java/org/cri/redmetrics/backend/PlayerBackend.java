package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestPlayer;

public class PlayerBackend extends HttpBackend<TestPlayer> {

    public PlayerBackend() {
        super("player/", TestPlayer.class);
    }
}
