package org.cri.redmetrics;

import org.cri.redmetrics.backend.GameBackend;
import org.cri.redmetrics.backend.PlayerBackend;
import org.cri.redmetrics.model.Players;
import org.cri.redmetrics.model.TestGame;
import org.cri.redmetrics.model.TestPlayer;

import java.io.IOException;

public abstract class ProgressDataBackendTest {

    static final GameBackend games = new GameBackend();
    static final PlayerBackend players = new PlayerBackend();

    TestGame game;
    TestPlayer player;

    public void createGameAndPlayer() {
        try {
            game = new TestGame();
            game.setName("My Test Game");
            game = games.post(game);

            player = Players.johnSnow();
            player = players.post(player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
