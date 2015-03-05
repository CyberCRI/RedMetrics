package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.csv.CsvEntityConverter;
import org.cri.redmetrics.dao.PlayerDao;
import org.cri.redmetrics.json.PlayerJsonConverter;
import org.cri.redmetrics.model.Player;

public class PlayerController extends Controller<Player, PlayerDao> {

    @Inject
    PlayerController(PlayerDao dao, PlayerJsonConverter jsonConverter, CsvEntityConverter<Player> csvEntityConverter) {
        super("player", dao, jsonConverter, csvEntityConverter);
    }

}
