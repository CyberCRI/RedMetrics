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
        assertThat(savedPlayer.getBirthDate()).isEqualTo(johnSnow.getBirthDate());
        assertThat(savedPlayer.getGender()).isEqualTo(johnSnow.getGender());
        assertThat(savedPlayer.getRegion()).isEqualTo(johnSnow.getRegion());
        assertThat(savedPlayer.getCountry()).isEqualTo(johnSnow.getCountry());
        assertThat(savedPlayer.getExternalId()).isEqualTo(johnSnow.getExternalId());
    }

    // READ

/*    @Test
    public void canFindByEmail() throws IOException {
        TestPlayer savedPlayer = players.post(Players.newJohnSnow());
        TestPlayer foundPlayer = players.getById("email/" + savedPlayer.getEmail());
        assertThat(foundPlayer.getId()).isEqualTo(savedPlayer.getId());
    }
*/

    // UPDATE

    @Test
    public void canUpdatePlayer() throws IOException {
        TestPlayer savedPlayer = players.post(Players.newJohnSnow());
        savedPlayer.setRegion("99999");
        TestPlayer updatedPlayer = players.put(savedPlayer);
        assertThat(savedPlayer.getId()).isEqualTo(savedPlayer.getId());
        assertThat(savedPlayer.getBirthDate()).isEqualTo(savedPlayer.getBirthDate());
        assertThat(savedPlayer.getGender()).isEqualTo(savedPlayer.getGender());
        assertThat(savedPlayer.getRegion()).isEqualTo(savedPlayer.getRegion());
        assertThat(savedPlayer.getCountry()).isEqualTo(savedPlayer.getCountry());
        assertThat(savedPlayer.getExternalId()).isEqualTo(savedPlayer.getExternalId());
    }

}