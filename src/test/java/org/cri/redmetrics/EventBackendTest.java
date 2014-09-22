package org.cri.redmetrics;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.GenericJson;
import org.cri.redmetrics.backend.Backends;
import org.cri.redmetrics.backend.EventBackend;
import org.cri.redmetrics.model.TestEvent;
import org.cri.redmetrics.model.TestGame;
import org.cri.redmetrics.model.TestPlayer;
import org.cri.redmetrics.util.DateUtils;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class EventBackendTest {

    static final EventBackend events = Backends.EVENT;

    TestGame game = Backends.newSavedGame();
    TestPlayer player = Backends.newSavedPlayer();
    TestEvent event;

    void saveEvent() throws IOException {
        event = events.post(event);
    }

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
        String sections = "level1";
        event.setSections(sections);
        saveEvent();
        assertThat(event.getSections()).isEqualTo("level1");
    }

    @Test
    public void canSaveCustomData() throws IOException {
        GenericJson data = new GenericJson();
        data.set("thing", "coin");
        data.set("amount", 14);
        event.setCustomData(data);
        saveEvent();
        assertThat(event.getCustomData().get("thing")).isEqualTo("coin");
        assertThat(event.getCustomData().get("amount")).isEqualTo(new BigDecimal(14));
    }

    @Test
    public void canSaveCoordinates() throws IOException {
        Integer[] coordinates = {123, 456};
        event.setCoordinates(coordinates);
        saveEvent();
        assertThat(event.getCoordinates()).isEqualTo(coordinates);
    }

    @Test
    public void generatesCreationDate() throws IOException {
        saveEvent();
        assertThat(event.getServerTime().length()).isEqualTo(29);
    }

    // READ

    @Test
    public void findsEventsByGame() throws IOException {
        resetGame();
        saveEvent();
        List<TestEvent> foundEvents = events.search().withGame(event.getGame()).execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsEventsForMultipleGames() throws IOException {
        String firstGameId = resetGame().getId();
        saveEvent();
        String secondGameId = resetGame().getId();
        saveEvent();
        List<TestEvent> foundEvents = events.search()
                .withGames(firstGameId, secondGameId)
                .execute();
        assertThat(foundEvents).hasSize(2);
    }

    @Test
    public void findsEventsByPlayer() throws IOException {
        resetPlayer();
        event = events.post(event);
        List<TestEvent> foundEvents = events.search().withPlayer(event.getPlayer()).execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsEventsForMultiplePlayers() throws IOException {
        String firstPlayerId = resetPlayer().getId();
        saveEvent();
        String secondPlayerId = resetPlayer().getId();
        saveEvent();
        List<TestEvent> foundEvents = events.search()
                .withPlayers(firstPlayerId, secondPlayerId)
                .execute();
        assertThat(foundEvents).hasSize(2);
    }

    @Test
    public void findsEventsByGameAndPlayer() throws IOException {
        String gameId = resetGame().getId();
        String playerId = resetPlayer().getId();
        saveEvent();

        resetEvent();
        event.setGame(gameId);
        resetPlayer();
        saveEvent();// Same game but different player

        resetEvent();
        event.setPlayer(playerId);
        resetGame();
        saveEvent(); // Same player but different game

        List<TestEvent> foundEvents = events.search()
                .withGame(gameId)
                .withPlayer(playerId)
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsEventsByType() throws IOException {
        event.setType("findsEventsByType" + Math.random());
        saveEvent();

        List<TestEvent> foundEvents = events.search().withType(event.getType()).execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void shouldFailWhenSearchingWithoutRequestParams() throws IOException {
        try {
            events.search().execute();
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    @Test
    public void findsBeforeServerTime() throws IOException {
        resetGame();
        saveEvent();

        DateTime afterFirstSave = new DateTime();

        resetEvent();
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .before(afterFirstSave)
                .withGame(game.getId())
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsAfterServerTime() throws IOException {
        resetEvent(); // Should have been called by TestNG, but the test will strangely fail without this line
        try {
            Thread.sleep(1001); // Wait one second
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        DateTime beforeCreation = new DateTime();
        saveEvent();

        List<TestEvent> foundEvents = events.search().after(beforeCreation).execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsBeforeUserTime() throws IOException {
        resetGame();
        DateTime time = new DateTime();

        event.setUserTime(DateUtils.print(time.minusSeconds(1)));
        saveEvent();

        resetEvent();
        event.setUserTime(DateUtils.print(time.plusSeconds(1)));
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .beforeUserTime(time)
                .withGame(game.getId())
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsAfterUserTime() throws IOException {
        resetGame();
        DateTime time = new DateTime();

        event.setUserTime(DateUtils.print(time.minusSeconds(1)));
        saveEvent();

        resetEvent();
        event.setUserTime(DateUtils.print(time.plusSeconds(1)));
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .afterUserTime(time)
                .withGame(game.getId())
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsBySection() throws IOException {
        resetGame();

        event.setSections("nosections");
        saveEvent();

        resetEvent();
        String sections = "level1.section1.subsection1";
        event.setSections(sections);
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .withSections("level1.*")
                .withGame(game.getId())
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

}
