
package org.cri.redmetrics;

import com.google.api.client.http.HttpResponseException;
import org.cri.redmetrics.backend.GroupBackend;
import org.cri.redmetrics.model.TestGroup;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.UUID;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;

public class GroupBackendTest {

    static final GroupBackend groups = new GroupBackend();

    TestGroup original;
    TestGroup read;

    @BeforeTest
    public void setUp() throws IOException {
        resetTestGroups();
    }

    private void resetTestGroups() throws IOException {
        original = new TestGroup();
        original.setCreator("TTAK");
        original.setDescription("A test Group");
        original.setName("1337");
        original.setOpen(true);
        read = groups.post(original);
    }

    //CREATE

    @Test
    public void canCreateGroup() throws IOException {
        assertThat(read.getId()).isNotNull();
        assertThat(read.getCreator()).isEqualTo(original.getCreator());
        assertThat(read.getDescription()).isEqualTo(original.getDescription());
        assertThat(read.getName()).isEqualTo(original.getName());
    }

    // READ

    @Test
    public void canReadGroup() throws IOException {
        resetTestGroups();
        original = read;
        read = groups.get(original.getId());
        assertThat(read.getName()).isEqualTo(original.getName());
    }

    @Test
    void shouldFailWhenReadingUnknownId() throws IOException {
        try {
            groups.get(UUID.randomUUID().toString());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }

    // UPDATE

    @Test
    public void canUpdateGroup() throws IOException {
        original = read;
        original.setName(original.getName() + "_Updated");
        read = groups.put(original);
        assertThat(read.getName()).isEqualTo(original.getName());
    }

    @Test
    public void shouldFailWhenUpdatingWithUrlIdDifferentThanContentId() throws IOException {
        try {
            groups.put(UUID.randomUUID().toString(), original);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    // DELETE

    @Test
    public void canDeleteGame() throws IOException {
        resetTestGroups();
        read = groups.delete(original.getId());
        try {
            groups.get(original.getId());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }

}
