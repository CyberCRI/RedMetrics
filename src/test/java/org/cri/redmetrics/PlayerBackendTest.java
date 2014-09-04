package org.cri.redmetrics;

import com.google.api.client.http.HttpResponseException;
import org.cri.redmetrics.backend.PlayerBackend;
import org.cri.redmetrics.model.Gender;
import org.cri.redmetrics.model.TestAddress;
import org.cri.redmetrics.model.TestPlayer;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class PlayerBackendTest {

    final PlayerBackend players = new PlayerBackend();

    static final String EMAIL = Math.random() + "test@test.fr";
    static final String FNAME = "Arthur";
    static final String LNAME = "Besnard";
    static final String BDATE = "1989-06-21 10:15:17";
    static final TestAddress ADDR = new TestAddress("77114", "Herme");
    static final String GENDER = Gender.MALE.name();

    TestPlayer createdPlayer;
    TestPlayer anotherPlayer;

    @BeforeTest
    public void setUp() throws IOException {
        resetCreatedPlayer();
        createdPlayer = players.post(createdPlayer);
    }

    void resetCreatedPlayer() throws IOException {
        createdPlayer = new TestPlayer();
        createdPlayer.setEmail(EMAIL);
        createdPlayer.setFirstName(FNAME);
        createdPlayer.setLastName(LNAME);
        createdPlayer.setBirthDate(BDATE);
        createdPlayer.setAddress(ADDR);
        createdPlayer.setGender(GENDER);
    }

    void createAnotherPlayer() throws IOException {
        anotherPlayer = new TestPlayer();
        anotherPlayer.setEmail(Math.random() + "test@test.fr");
        anotherPlayer.setFirstName("John");
        anotherPlayer.setLastName("Snow");
        anotherPlayer.setBirthDate("1983-07-17 00:00:00");
        players.post(anotherPlayer);
    }

    // CREATE

    @Test
    public void canCreateGame() throws IOException {
        assertThat(createdPlayer.getId()).isNotNull();
        assertThat(createdPlayer.getEmail()).isEqualTo(EMAIL);
        assertThat(createdPlayer.getFirstName()).isEqualTo(FNAME);
        assertThat(createdPlayer.getLastName()).isEqualTo(LNAME);
        assertThat(createdPlayer.getBirthDate()).isEqualTo(BDATE);
        assertThat(createdPlayer.getGender()).isEqualTo(GENDER);
        // Comparing addresses
        assertThat(createdPlayer.getAddress().getPostalCode()).isEqualTo(ADDR.getPostalCode());
        assertThat(createdPlayer.getAddress().getCountry()).isEqualTo(ADDR.getCountry());
    }

    @Test
    public void shouldForbidCreationWhenDuplicateEmail() throws IOException {
        createdPlayer.setId(null);
        try {
            players.post(createdPlayer);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    // READ

    @Test
    public void canFindByEmail() throws IOException {
        TestPlayer foundPlayer = players.get("player/email/" + EMAIL);
        assertThat(foundPlayer.getId()).isEqualTo(createdPlayer.getId());
    }

    // UPDATE

    @Test
    public void shouldForbidUpdateWhenDuplicateEmail() throws IOException {
        createAnotherPlayer();
        try {
            anotherPlayer.setEmail(EMAIL);
            players.put(createdPlayer);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

}