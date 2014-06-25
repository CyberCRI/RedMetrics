package org.cri.redmetrics;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class SimpleTest {

    static final String GAME_NAME = "Asteroids";
    static final String UPDATED_GAME_NAME = "Gasteroids";

    Server server = new Server();
    HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory((request) -> request.setParser(new JsonObjectParser(new GsonFactory())));

    TestGame createdGame;

    @BeforeClass
    public void setUp() throws IOException {
        server.start();
        resetCreatedGame();
    }

    void resetCreatedGame() throws IOException {
        TestGame game = new TestGame();
        game.setName(GAME_NAME);
        createdGame = post(game);
    }

    @AfterClass
    public void tearDown() {
        server.clearAllRoutes();
    }

    // CREATE

    @Test
    public void canCreateGame() throws IOException {
        assertThat(createdGame.getId()).isNotNull().isNotEqualTo(0);
        assertThat(createdGame.getApiKey()).isNotNull();
        assertThat(createdGame.getName()).isEqualTo(GAME_NAME);
    }

    @Test
    public void shouldForbidUnnamedGameCreation() throws IOException {
        try {
            post(new TestGame());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    // READ

    @Test
    public void canReadGame() throws IOException {
        TestGame readGame = get(createdGame.getId());
        assertThat(readGame.getName()).isEqualTo(GAME_NAME);
    }

    @Test
    void shouldFailWhenReadingUnknownId() throws IOException {
        try {
            get(randomId());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }

    // UPDATE

    @Test
    public void canUpdateGame() throws IOException {
        createdGame.setName(UPDATED_GAME_NAME);
        TestGame updatedGame = put(createdGame);
        assertThat(updatedGame.getName()).isEqualTo(UPDATED_GAME_NAME);
    }

    @Test
    public void shouldFailWhenUpdatingWithUrlIdDifferentThanContentId() throws IOException {
        try {
            put("game/" + randomId(), createdGame, TestGame.class);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    @Test
    public void shouldFailWhenUpdatingNegativeId() throws IOException {
        try {
            int id = -1;
            createdGame.setId(id);
            TestGame updatedGame = put("game/" + id, createdGame, TestGame.class);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    // DELETE

    @Test
    public void canDeleteGame() throws IOException {
        TestGame deletedGame = delete(createdGame.getId());
        resetCreatedGame();
        try {
            get(deletedGame.getId());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }

    // TEST UTIL

    TestGame get(int id) throws IOException {
        return get("game/" + id, TestGame.class);
    }

    TestGame post(TestGame game) throws IOException {
        return post("game/", game, TestGame.class);
    }

    TestGame put(TestGame game) throws IOException {
        return put("game/" + game.getId(), game, TestGame.class);
    }

    TestGame delete(int id) throws IOException {
        return delete("game/" + id, TestGame.class);
    }

    <T> T get(String path, Class<T> type) throws IOException {
        return requestFactory.buildGetRequest(url(path)).execute().parseAs(type);
    }

    <T> T post(String path, GenericJson json, Class<T> type) throws IOException {
        HttpContent content = new JsonHttpContent(new GsonFactory(), json);
        return requestFactory.buildPostRequest(url(path), content).execute().parseAs(type);
    }

    <T> T put(String path, GenericJson json, Class<T> type) throws IOException {
        HttpContent content = new JsonHttpContent(new GsonFactory(), json);
        return requestFactory.buildPutRequest(url(path), content).execute().parseAs(type);
    }

    <T> T delete(String path, Class<T> type) throws IOException {
        return requestFactory.buildDeleteRequest(url(path)).execute().parseAs(type);
    }

    GenericUrl url(String path) {
        return new GenericUrl("http://localhost:4567/" + path);
    }

    int randomId() {
        return (int) Math.round(Math.random() * 1000000000) + 1000000000;
    }

}
