package org.cri.redmetrics;

import com.google.api.client.http.HttpResponseException;
import org.cri.redmetrics.backend.Backends;
import org.cri.redmetrics.backend.PlayerBackend;
import org.cri.redmetrics.model.Players;
import org.cri.redmetrics.model.TestPlayer;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class PlayerBackendTest {

    final PlayerBackend players = Backends.PLAYER;

    // CREATE

    @Test
    public void canCreatePlayer() throws IOException {
        TestPlayer johnSnow = Players.newJohnSnow();
        TestPlayer savedPlayer = players.post(johnSnow);
        assertThat(savedPlayer.getId()).isNotNull().hasSize(36);
        assertThat(savedPlayer.getEmail()).isEqualTo(johnSnow.getEmail());
        assertThat(savedPlayer.getFirstName()).isEqualTo(johnSnow.getFirstName());
        assertThat(savedPlayer.getLastName()).isEqualTo(johnSnow.getLastName());
        assertThat(savedPlayer.getBirthDate()).isEqualTo(johnSnow.getBirthDate());
        assertThat(savedPlayer.getGender()).isEqualTo(johnSnow.getGender());
        // Comparing addresses
        assertThat(savedPlayer.getAddress().getPostalCode()).isEqualTo(johnSnow.getAddress().getPostalCode());
        assertThat(savedPlayer.getAddress().getCountry()).isEqualTo(johnSnow.getAddress().getCountry());
    }

    @Test
    public void shouldHideEmail() throws IOException {
        TestPlayer savedPlayer = players.post(Players.newJohnSnow());
        TestPlayer fetchedPlayer = players.getById(savedPlayer.getId());
        assertThat(fetchedPlayer.getEmail()).isNull();
    }

    @Test
    public void shouldForbidCreationWhenDuplicateEmail() throws IOException {
        TestPlayer savedPlayer = players.post(Players.newJohnSnow());
        try {
            players.post(savedPlayer);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    // READ

    @Test
    public void canFindByEmail() throws IOException {
        TestPlayer savedPlayer = players.post(Players.newJohnSnow());
        TestPlayer foundPlayer = players.getById("email/" + savedPlayer.getEmail());
        assertThat(foundPlayer.getId()).isEqualTo(savedPlayer.getId());
    }

    // UPDATE

    @Test
    public void shouldForbidUpdateWhenDuplicateEmail() throws IOException {
        TestPlayer savedPlayer = players.post(Players.newJohnSnow());
        TestPlayer anotherPlayer = Players.newJohnSnow();
        try {
            anotherPlayer.setEmail(savedPlayer.getEmail());
            players.put(anotherPlayer);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

}