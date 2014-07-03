
package org.cri.redmetrics;

import com.google.api.client.http.HttpResponseException;
import java.io.IOException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.failBecauseExceptionWasNotThrown;
import org.testng.annotations.Test;

/**
 *
 * @author Besnard Arthur
 */
public class GroupBackendTest extends HttpBackendTest<TestGroup>{
    
    TestGroup original;
    TestGroup read;
    
    public GroupBackendTest() {
        super("group/", TestGroup.class);
    }
    
    @Override
    void init() throws IOException {
        resetTestGroups();
    }

    private void resetTestGroups() throws IOException {
        original = new TestGroup();
        original.setCreator("TTAK");
        original.setDescription("A test Group");
        original.setName("1337");
        original.setOpen(true);
        read = post(original);
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
        read = get(original.getId());
        assertThat(read.getName()).isEqualTo(original.getName());
    }

    @Test
    void shouldFailWhenReadingUnknownId() throws IOException {
        try {
            get(-1);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }

    // UPDATE

    @Test
    public void canUpdateGroup() throws IOException {
        original = read;
        original.setName(original.getName()+"_Updated");
        read = put(original);
        assertThat(read.getName()).isEqualTo(original.getName());
    }

    @Test
    public void shouldFailWhenUpdatingWithUrlIdDifferentThanContentId() throws IOException {
        try {
            put(path + 9999999, original);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    @Test
    public void shouldFailWhenUpdatingNegativeId() throws IOException {
        try {
            int id = -1;
            original.setId(id);
            read = put(path + id, original);
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(400);
        }
    }

    // DELETE

    @Test
    public void canDeleteGame() throws IOException {
        resetTestGroups();
        read = delete(original.getId());
        try {
            get(original.getId());
            failBecauseExceptionWasNotThrown(HttpResponseException.class);
        } catch (HttpResponseException e) {
            assertThat(e.getStatusCode()).isEqualTo(404);
        }
    }
    
}
