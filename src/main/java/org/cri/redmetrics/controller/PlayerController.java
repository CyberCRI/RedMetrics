package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.dao.PlayerDao;
import org.cri.redmetrics.json.PlayerJsonConverter;
import org.cri.redmetrics.model.Player;
import spark.Route;

import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.halt;

public class PlayerController extends Controller<Player, PlayerDao> {

    @Inject
    PlayerController(PlayerDao dao, PlayerJsonConverter jsonConverter) {
        super("player", dao, jsonConverter);
    }

}
