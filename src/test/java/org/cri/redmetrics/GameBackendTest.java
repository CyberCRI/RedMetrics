package org.cri.redmetrics;

import com.google.api.client.http.HttpResponseException;
import org.cri.redmetrics.backend.Backends;
import org.cri.redmetrics.backend.GameBackend;
import org.cri.redmetrics.model.TestGame;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class GameBackendTest {

    static final GameBackend games = Backends.GAME;

    static final String GAME_NAME = "Asteroids";
    static final String UPDATED_GAME_NAME = "Gasteroids";

    TestGame createdGame;

    @BeforeTest
    public void setUp() throws IOException {
        resetCreatedGame();
    }

    void resetCreatedGame() throws IOException {
        TestGame game = new TestGame();
        game.setName(GAME_NAME);
        createdGame = games.post(game);
    }

    // CREATE

    @Test
    public void canCreateGame() throws IOException {
        assertThat(createdGame.getId()).isNotNull().hasSize(36);
        assertThat(createdGame.getAdminKey()).isNotNull().hasSize(36);
        assertThat(createdGame.getName()).isEqualTo(GAME_NAME);
    }

    @Test
    public void shouldForbidUnnamedGameCreation() throws IOException {
        try {
            games.post(new TestGame());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    // READ

    @Test
    public void canReadGame() throws IOException {
        TestGame readGame = games.getById(createdGame.getId());
        assertThat(readGame.getName()).isEqualTo(GAME_NAME);
    }

    @Test
    public void shouldFailWhenReadingUnknownId() throws IOException {
        try {
            games.getById(UUID.randomUUID().toString());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }

    // UPDATE

    @Test
    public void canUpdateGame() throws IOException {
        createdGame.setName(UPDATED_GAME_NAME);
        TestGame updatedGame = games.put(createdGame);
        assertThat(updatedGame.getName()).isEqualTo(UPDATED_GAME_NAME);
    }

    @Test
    public void shouldFailWhenUpdatingWithUrlIdDifferentThanContentId() throws IOException {
        try {
            String newId = UUID.randomUUID().toString();
            games.put(newId, createdGame);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    // DELETE

    @Test
    public void canDeleteGame() throws IOException {
        TestGame deletedGame = games.delete(createdGame.getId());
        resetCreatedGame();
        try {
            games.getById(deletedGame.getId());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }
}
