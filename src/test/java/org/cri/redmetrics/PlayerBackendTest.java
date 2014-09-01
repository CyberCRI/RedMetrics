package org.cri.redmetrics;

import com.google.api.client.http.HttpResponseException;
import org.cri.redmetrics.model.Gender;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class PlayerBackendTest extends HttpBackendTest<TestPlayer> {

    private static TestPlayer createdPlayer;
    private static final String EMAIL = Math.random() + "test@test.fr";
    private static final String FNAME = "Arthur";
    private static final String LNAME = "Besnard";
    private static final String BDATE = "1989-06-21 10:15:17";
    private static final TestAddress ADDR = new TestAddress("77114", "Herme");
    private static final String GENDER = Gender.MALE.name();

    public PlayerBackendTest() {
        super("player/", TestPlayer.class);
    }

    @Override
    void init() throws IOException {
        resetCreatedPlayer();
    }

    void resetCreatedPlayer() throws IOException {
        TestPlayer player = new TestPlayer();
        player.setEmail(EMAIL);
        player.setFirstName(FNAME);
        player.setLastName(LNAME);
        player.setBirthDate(BDATE);
        player.setAddress(ADDR);
        player.setGender(GENDER);
        createdPlayer = post(player);
    }

    // CREATE

    @Test
    public void canCreateGame() throws IOException {
        assertThat(createdPlayer.getId()).isNotNull().isNotEqualTo(0);
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
    public void shouldNotAllowDuplicateEmails() throws IOException {
        createdPlayer.setId(0);
        try {
            buildPostRequest(createdPlayer);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

}
