package org.cri.redmetrics;

import java.io.IOException;
import java.util.ArrayList;
import org.cri.redmetrics.backend.Backends;
import org.cri.redmetrics.backend.GameBackend;
import org.cri.redmetrics.backend.GameVersionBackend;
import org.cri.redmetrics.model.TestGame;
import org.cri.redmetrics.model.TestGameVersion;
import static org.fest.assertions.api.Assertions.assertThat;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class GameVersionBackendTest {
    static final GameBackend games = Backends.GAME;
    static final GameVersionBackend gameVersions = Backends.GAME_VERSION;

    static final String GAME_NAMES[] = {"I wanna be the dude", "Call of pupy", "Tong Rider", "Partial Annihilation"};
    
    static final String[] GAME_VERSION_NAMES = {"V12", "alpha0.2", "Gamma3", "W3.1"};

    ArrayList<TestGame> createdGames = new ArrayList<>();
    ArrayList<TestGameVersion> createdVersions = new ArrayList<>();

    @BeforeTest
    public void setUp() throws IOException {
        resetCreatedVersionedGame();
    }

    void resetCreatedVersionedGame() throws IOException {
        for(int i=0 ; i< GAME_NAMES.length ; ++i){
            TestGame game = new TestGame();
            game.setName(GAME_NAMES[i]);
            createdGames.add(games.post(game));
            for(int j =0 ; j< GAME_VERSION_NAMES.length ; ++j){
                TestGameVersion gameVersion = new TestGameVersion();
                gameVersion.setGame(createdGames.get(i).getId());
                createdVersions.add(gameVersions.post(gameVersion));
            }
        }
    }

    // CREATE

    @Test
    public void canCreateVersion() throws IOException {
        for(int i=0 ; i<createdVersions.size() ; ++i){
            assertThat(createdVersions.get(i)).isNotNull();
            assertThat(createdVersions.get(i).getGame()).isEqualTo(createdGames.get(i/GAME_NAMES.length).getId());
        }
    }
    /*
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

    @Test
    public void canFindGameVersions() throws IOException {
        TestGameVersion gameVersion1 = new TestGameVersion();
        gameVersion1.setGame(createdGame.getId());
        gameVersions.post(gameVersion1);

        TestGameVersion gameVersion2 = new TestGameVersion();
        gameVersion2.setGame(createdGame.getId());
        gameVersions.post(gameVersion2);

        List<TestGameVersion> foundGameVersions = gameVersions.getGameVersions(createdGame.getId());
        assertThat(foundGameVersions).hasSize(2);
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
        resetCreatedVersionedGame();
        try {
            games.getById(deletedGame.getId());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }
    */
}
