package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestGame;

public class GameBackend extends HttpBackend<TestGame> {

    GameBackend() {
        super("game", TestGame.class);
    }

}
