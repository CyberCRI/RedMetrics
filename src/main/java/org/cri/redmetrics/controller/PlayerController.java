package org.cri.redmetrics.controller;

import com.google.inject.Inject;
import org.cri.redmetrics.dao.PlayerDao;
import org.cri.redmetrics.json.PlayerJsonConverter;
import org.cri.redmetrics.model.Player;

import static spark.Spark.*;

public class PlayerController extends Controller<Player, PlayerDao> {

    @Inject
    PlayerController(PlayerDao dao, PlayerJsonConverter json) {
        super("/player", dao, json);
    }

    @Override
    protected void publishSpecific() {
        get(path + "/email/:email", (request, response) -> {
            Player player = dao.findByEmail(request.params(":email"));
            if (player == null) halt(404);
            return player;
        }, jsonConverter);
    }

    @Override
    protected Player create(Player player) {
        checkNoDuplicate(player.getEmail());
        return super.create(player);
    }

    protected void checkNoDuplicate(String email) {
        Player duplicate = dao.findByEmail(email);
        if (duplicate != null) {
            halt(400, "A player already exists with the email " + email);
        }
    }
}
