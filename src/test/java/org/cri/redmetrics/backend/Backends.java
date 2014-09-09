package org.cri.redmetrics.backend;

import org.cri.redmetrics.model.Players;
import org.cri.redmetrics.model.TestGame;
import org.cri.redmetrics.model.TestPlayer;

import java.io.IOException;

public class Backends {

    public static final EventBackend EVENT = new EventBackend();
    public static final GameBackend GAME = new GameBackend();
    public static final GroupBackend GROUP = new GroupBackend();
    public static final PlayerBackend PLAYER = new PlayerBackend();
    public static final SnapshotBackend SNAPSHOT = new SnapshotBackend();

    public static TestGame newSavedGame() {
        try {
            TestGame game = new TestGame();
            game.setName("My Test Game");
            TestGame savedGame = GAME.post(game);
            return savedGame;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TestPlayer newSavedPlayer() {
        try {
            TestPlayer player = Players.newJohnSnow();
            TestPlayer savedPlayer = PLAYER.post(player);
            return savedPlayer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
