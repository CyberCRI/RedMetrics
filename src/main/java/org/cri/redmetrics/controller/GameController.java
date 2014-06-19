package org.cri.redmetrics.controller;

import com.google.gson.Gson;
import com.google.inject.Inject;
import org.cri.redmetrics.dao.GameDao;
import org.cri.redmetrics.json.GameJson;
import org.cri.redmetrics.model.Game;

public class GameController extends Controller<Game, GameDao> {

    @Inject
    GameController(GameDao dao, GameJson json) {
        super("/game", dao, json);
    }

    @Override
    protected Game read(int id) {
        Game game = super.read(id);
        if (game != null) game.setApiKey(null);
        return game;
    }

}
