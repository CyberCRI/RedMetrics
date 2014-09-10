package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.TestProgressData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<E> searchByGameAndPlayer(String gameId, String playerId) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("game", gameId);
        params.put("player", playerId);
        return search(params);
    }

}
