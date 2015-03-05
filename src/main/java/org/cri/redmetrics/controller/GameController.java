package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.csv.CsvEntityConverter;
import org.cri.redmetrics.dao.GameDao;
import org.cri.redmetrics.json.GameJsonConverter;
import org.cri.redmetrics.model.Game;
import org.cri.redmetrics.model.ResultsPage;
import spark.Request;

import java.util.UUID;


public class GameController extends Controller<Game, GameDao> {

    @Inject
    GameController(GameDao dao, GameJsonConverter jsonConverter, CsvEntityConverter<Game> csvEntityConverter) {
        super("game", dao, jsonConverter, csvEntityConverter);
    }

    @Override
    protected Game read(UUID id) {
        Game game = super.read(id);
        if (game != null) game.setAdminKey(null);
        return game;
    }

    @Override
    protected ResultsPage<Game> list(Request request, long start, long count) {
        ResultsPage<Game> allGames = super.list(request, start, count);
        allGames.results.forEach((game) -> game.setAdminKey(null));
        return allGames;
    }
}
