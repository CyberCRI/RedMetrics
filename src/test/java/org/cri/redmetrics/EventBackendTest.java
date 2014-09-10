package org.cri.redmetrics;

import com.google.api.client.json.GenericJson;
import org.cri.redmetrics.backend.Backends;
import org.cri.redmetrics.backend.EventBackend;
import org.cri.redmetrics.model.TestEvent;
import org.cri.redmetrics.model.TestGame;
import org.cri.redmetrics.model.TestPlayer;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class EventBackendTest {

    static final EventBackend events = Backends.EVENT;

    TestGame game = Backends.newSavedGame();
    TestPlayer player = Backends.newSavedPlayer();
    TestEvent event;

    public TestGame resetGame() throws IOException {
        game = Backends.newSavedGame();
        event.setGame(game.getId());
        return game;
    }

    public TestPlayer resetPlayer() throws IOException {
        player = Backends.newSavedPlayer();
        event.setPlayer(player.getId());
        return player;
    }

    @BeforeTest
    public void resetEvent() throws IOException {
        event = new TestEvent();
        event.setGame(game.getId());
        event.setPlayer(player.getId());
        event.setType("start");
    }

    // CREATE

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
        assertThat(event.getCreationDate().length()).isEqualTo(29);
    }

    // READ

    @Test
    public void findsEventsByGame() throws IOException {
        resetGame();
        event = events.post(event);
        List<TestEvent> foundEvents = events.searchByGame(event.getGame());
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsEventsByPlayer() throws IOException {
        resetPlayer();
        event = events.post(event);
        List<TestEvent> foundEvents = events.searchByPlayer(event.getPlayer());
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsEventsByGameAndPlayer() throws IOException {
        String gameId = resetGame().getId();
        String playerId = resetPlayer().getId();
        event = events.post(event);

        resetEvent();
        event.setGame(gameId);
        resetPlayer();
        events.post(event);// Save event with same game and different player

        resetEvent();
        event.setPlayer(playerId);
        resetGame();
        events.post(event); // Save event with same player and different game

        List<TestEvent> foundEvents = events.searchByGameAndPlayer(gameId, playerId);
        assertThat(foundEvents).hasSize(1);
    }

}
