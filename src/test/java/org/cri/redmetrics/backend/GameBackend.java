package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestGame;

public class GameBackend extends HttpBackend<TestGame> {

    public GameBackend() {
        super("game/", TestGame.class);
    }
}
