package org.cri.redmetrics;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.json.GenericJson;
import org.cri.redmetrics.backend.Backends;
import org.cri.redmetrics.backend.EventBackend;
import org.cri.redmetrics.model.TestEvent;
import org.cri.redmetrics.model.TestGameVersion;
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

    TestGameVersion gameVersion = Backends.newSavedGameVersion();
    TestPlayer player = Backends.newSavedPlayer();
    TestEvent event;

    void saveEvent() throws IOException {
        event = events.post(event);
    }

    public TestGameVersion resetGameVersion() throws IOException {
        gameVersion = Backends.newSavedGameVersion();
        event.setGameVersion(gameVersion.getId());
        return gameVersion;
    }

    public TestPlayer resetPlayer() throws IOException {
        player = Backends.newSavedPlayer();
        event.setPlayer(player.getId());
        return player;
    }

    @BeforeTest
    public void resetEvent() throws IOException {
        event = new TestEvent();
        event.setGameVersion(gameVersion.getId());
        event.setPlayer(player.getId());
        event.setType("start");
    }

    // CREATE

    @Test
    public void canSaveSection() throws IOException {
        String section = "level1";
        event.setSection(section);
        saveEvent();
        assertThat(event.getSection()).isEqualTo("level1");
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
        assertThat(event.getServerTime().length()).isEqualTo(24);
    }

    // READ

    @Test
    public void findsEventsByGame() throws IOException {
        resetGameVersion();
        saveEvent();
        List<TestEvent> foundEvents = events.search().withGame(gameVersion.getGame()).execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsEventsByGameVersion() throws IOException {
        resetGameVersion();
        saveEvent();
        List<TestEvent> foundEvents = events.search().withGameVersion(event.getGameVersion()).execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsEventsForMultipleGames() throws IOException {
        String firstGameId = resetGameVersion().getGame();
        saveEvent();
        String secondGameId = resetGameVersion().getGame();
        saveEvent();
        List<TestEvent> foundEvents = events.search()
                .withGames(firstGameId, secondGameId)
                .execute();
        assertThat(foundEvents).hasSize(2);
    }

    @Test
    public void findsEventsForMultipleGameVersions() throws IOException {
        String firstGameVersionId = resetGameVersion().getId();
        saveEvent();
        String secondGameVersionId = resetGameVersion().getId();
        saveEvent();
        List<TestEvent> foundEvents = events.search()
                .withGameVersions(firstGameVersionId, secondGameVersionId)
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
        String gameVersionId = resetGameVersion().getId();
        String playerId = resetPlayer().getId();
        saveEvent();

        resetEvent();
        event.setGameVersion(gameVersionId);
        resetPlayer();
        saveEvent();// Same gameVersion but different player

        resetEvent();
        event.setPlayer(playerId);
        resetGameVersion();
        saveEvent(); // Same player but different gameVersion

        List<TestEvent> foundEvents = events.search()
                .withGameVersion(gameVersionId)
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
    public void shouldFailWhenSearchingWithEmptyParamsOnly() throws IOException {
        try {
            events
                    .search()
                    .withGameVersion("")
                    .withPlayer("")
                    .execute();
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    @Test
    public void findsBeforeServerTime() throws IOException {
        resetGameVersion();
        saveEvent();

        DateTime afterFirstSave = new DateTime();

        resetEvent();
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .before(afterFirstSave)
                .withGameVersion(gameVersion.getId())
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
        resetGameVersion();
        DateTime time = new DateTime();

        event.setUserTime(DateUtils.print(time.minusSeconds(1)));
        saveEvent();

        resetEvent();
        event.setUserTime(DateUtils.print(time.plusSeconds(1)));
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .beforeUserTime(time)
                .withGameVersion(gameVersion.getId())
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsAfterUserTime() throws IOException {
        resetGameVersion();
        DateTime time = new DateTime();

        event.setUserTime(DateUtils.print(time.minusSeconds(1)));
        saveEvent();

        resetEvent();
        event.setUserTime(DateUtils.print(time.plusSeconds(1)));
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .afterUserTime(time)
                .withGameVersion(gameVersion.getId())
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void findsBySection() throws IOException {
        resetGameVersion();

        event.setSection("nosection");
        saveEvent();

        resetEvent();
        String section = "level1.section1.subsection1";
        event.setSection(section);
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .withSection("level1.*")
                .withGameVersion(gameVersion.getId())
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void searchShouldAcceptEmptyParams() throws IOException {
        resetGameVersion();
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .withGameVersion(gameVersion.getId())
                .withPlayer("")
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void searchShouldIgnoreEmptyStringParams() throws IOException {
        resetGameVersion();
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .withGameVersion(gameVersion.getId())
                .withType("")
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

    @Test
    public void searchShouldIgnoreEmptySectionParam() throws IOException {
        resetGameVersion();
        saveEvent();

        List<TestEvent> foundEvents = events.search()
                .withGameVersion(gameVersion.getId())
                .withSection("")
                .execute();
        assertThat(foundEvents).hasSize(1);
    }

}
