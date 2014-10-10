package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestGameVersion;

public class GameVersionBackend extends HttpBackend<TestGameVersion> {

    GameVersionBackend() {
        super("gameVersion", TestGameVersion.class);
    }

}