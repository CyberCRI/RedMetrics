package org.cri.redmetrics;

import com.google.api.client.json.GenericJson;
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
import java.math.BigDecimal;

import static org.fest.assertions.api.Assertions.assertThat;

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
        event.setType("start");
    }

    @Test
    public void canSaveSections() throws IOException {
        String[] sections = {"level1"};
        event.setSections(sections);
        event = events.post(event);
        assertThat(event.getSections()).containsOnly("level1");
    }

    @Test
    public void canSaveCustomData() throws IOException {
        GenericJson data = new GenericJson();
        data.set("thing", "coin");
        data.set("amount", 14);
        event.setCustomData(data);
        event = events.post(event);
        assertThat(event.getCustomData().get("thing")).isEqualTo("coin");
        assertThat(event.getCustomData().get("amount")).isEqualTo(new BigDecimal(14));
    }

    @Test
    public void canSaveCoordinates() throws IOException {
        long[] coordinates = {124, 527};
        event.setCoordinates(coordinates);
        event = events.post(event);
        assertThat(event.getCoordinates()).containsOnly(124, 527);
    }

    @Test
    public void generatesCreationDate() throws IOException {
        event = events.post(event);
        assertThat(event.getCreationDate().length()).isEqualTo(19);
    }

}
