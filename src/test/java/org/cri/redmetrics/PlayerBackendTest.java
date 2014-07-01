package org.cri.redmetrics;

import com.google.api.client.http.HttpContent;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.util.Date;
import org.cri.redmetrics.model.Address;
import org.cri.redmetrics.model.Gender;
import static org.fest.assertions.api.Assertions.*;
import org.testng.annotations.Test;

/**
 *
 * @author Besnard Arthur
 */
public class PlayerBackendTest extends HttpBackendTest<TestPlayer> {

    private static TestPlayer createdPlayer;
    private static final String EMAIL = "test@test.fr";
    private static final String FNAME = "Arthur";
    private static final String LNAME = "Besnard";
    private static final Date BDATE = new Date();
    private static final Address ADDR  = new Address(5, "77114", "France");
    private static final Gender GENDER = Gender.MALE;

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
        assertThat(createdPlayer.getAddress()).isEqualTo(ADDR);
        assertThat(createdPlayer.getGender());
        assertThat(createdPlayer.getGender() == GENDER);
    }
    
    
    @Override
    TestPlayer post(String path, GenericJson json) throws IOException {
        HttpContent content = new JsonHttpContent(new GsonFactory(), json);
        return requestFactory.buildPostRequest(url(path), content).execute().parseAs(type);
    }
    @Override
    TestPlayer put(String path, GenericJson json) throws IOException {
        HttpContent content = new JsonHttpContent(new GsonFactory(), json);
        return requestFactory.buildPutRequest(url(path), content).execute().parseAs(type);
    }
}
