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

    @Override
    protected void publishSpecific() {
        Route getByEmailRoute = (request, response) -> {
            String email = request.params(":email");
            Player player = dao.findByEmail(email);
            if (player == null) halt(404, "No player found for email : " + email);
            return player;
        };
        get(path + "/email/:email", getByEmailRoute, jsonConverter);
        get(path + "/email/:email/", getByEmailRoute, jsonConverter);
    }

    @Override
    protected Player create(Player player) {
        checkNoDuplicate(player.getEmail());
        return super.create(player);
    }

    @Override
    protected Player read(UUID id) {
        Player player = super.read(id);
        player.setEmail(null); // Hide email
        return player;
    }

    @Override
    protected Player update(Player player) {
        checkNoDuplicate(player.getEmail());
        return super.update(player);
    }

    protected void checkNoDuplicate(String email) {
        Player duplicate = dao.findByEmail(email);
        if (duplicate != null) {
            halt(400, "A player already exists with the email " + email);
        }
    }
}
