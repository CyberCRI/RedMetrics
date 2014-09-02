package org.cri.redmetrics;

import org.cri.redmetrics.backend.EventBackend;
import org.cri.redmetrics.backend.GameBackend;
import org.cri.redmetrics.backend.PlayerBackend;
import org.cri.redmetrics.model.Players;
import org.cri.redmetrics.model.TestEvent;
import org.cri.redmetrics.model.TestGame;
import org.cri.redmetrics.model.TestPlayer;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class EventBackendTest {

    static final EventBackend events = new EventBackend();
    static final GameBackend games = new GameBackend();
    static final PlayerBackend players = new PlayerBackend();

    TestGame game;
    TestPlayer player;
    TestEvent event;

    @BeforeTest
    public void setUp() throws IOException {
        game = new TestGame();
        game.setName("My Test Game");
        game = games.post(game);

        player = Players.johnSnow();
        player = players.post(player);

        event = new TestEvent();
        event.setGame(game.getId());
        event.setPlayer(player.getId());
    }

    @Test
    public void test() throws IOException {
        event.setType("Win");
        events.post(event);
    }

}
