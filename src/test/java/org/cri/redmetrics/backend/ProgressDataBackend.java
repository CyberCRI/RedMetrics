package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestProgressData;

import java.io.IOException;
import java.util.List;

public class ProgressDataBackend<E extends TestProgressData> extends HttpBackend<E> {

    ProgressDataBackend(String entityPath, Class<E> type) {
        super(entityPath, type);
    }

    public List<E> searchByGame(String gameId) throws IOException {
        return search("game", gameId);
    }

    public List<E> searchByPlayer(String playerId) throws IOException {
        return search("player", playerId);
    }

}
