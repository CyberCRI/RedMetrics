package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.dao.GameDao;
import org.cri.redmetrics.json.GameJsonConverter;
import org.cri.redmetrics.model.Game;


public class GameController extends Controller<Game, GameDao> {

    @Inject
    GameController(GameDao dao, GameJsonConverter json) {
        super("/game", dao, json);
    }

    @Override
    protected Game read(int id) {
        Game game = super.read(id);
        if (game != null) game.setAdminKey(null);
        return game;
    }

}
