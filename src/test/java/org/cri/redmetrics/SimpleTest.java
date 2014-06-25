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

    Server server = new Server();
    HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory((request) -> request.setParser(new JsonObjectParser(new GsonFactory())));

    TestGame createdGame;

    @BeforeClass
    public void setUp() throws IOException {
        server.start();
        createGame();
    }

    void createGame() throws IOException {
        TestGame game = new TestGame();
        game.setName(GAME_NAME);
        createdGame = post(game);
    }

    @AfterClass
    public void tearDown() {
        server.clearAllRoutes();
    }

    @Test
    public void cantCreateGameWithoutName() throws IOException {
        try {
            post(new TestGame());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    @Test
    public void canCreateGame() throws IOException {
        assertThat(createdGame.getId()).isNotNull().isNotEqualTo(0);
        assertThat(createdGame.getApiKey()).isNotNull();
        assertThat(createdGame.getName()).isEqualTo(GAME_NAME);
    }

    @Test
    public void canReadGame() throws IOException {
        TestGame readGame = get(createdGame.getId());
        assertThat(readGame.getName()).isEqualTo(GAME_NAME);
    }

    TestGame get(int id) throws IOException {
        return get("game/" + id, TestGame.class);
    }

    TestGame post(TestGame game) throws IOException {
        return post("game/", game, TestGame.class);
    }

    <T> T get(String path, Class<T> type) throws IOException {
        return requestFactory.buildGetRequest(url(path)).execute().parseAs(type);
    }

    <T> T post(String path, GenericJson json, Class<T> type) throws IOException {
        HttpContent content = new JsonHttpContent(new GsonFactory(), json);
        return requestFactory.buildPostRequest(url(path), content).execute().parseAs(type);
    }

    GenericUrl url(String path) {
        return new GenericUrl("http://localhost:4567/" + path);
    }

}
