package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.dao.GameDao;
import org.cri.redmetrics.json.GameJsonConverter;
import org.cri.redmetrics.model.Game;
import org.cri.redmetrics.model.ResultsPage;
import spark.Request;

import java.util.List;
import java.util.UUID;


public class GameController extends Controller<Game, GameDao> {

    @Inject
    GameController(GameDao dao, GameJsonConverter jsonConverter) {
        super("game", dao, jsonConverter);
    }

    @Override
    protected Game read(UUID id) {
        Game game = super.read(id);
        if (game != null) game.setAdminKey(null);
        return game;
    }

    @Override
    protected ResultsPage<Game> list(Request request) {
        ResultsPage<Game> allGames = super.list(request);
        allGames.results.forEach((game) -> game.setAdminKey(null));
        return allGames;
    }
}
