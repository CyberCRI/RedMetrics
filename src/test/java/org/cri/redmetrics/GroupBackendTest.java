
package org.cri.redmetrics;

import java.io.IOException;
import static org.fest.assertions.api.Assertions.assertThat;
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
    
    @Test
    public void canCreateGroup() throws IOException {
        assertThat(read.getId()).isNotNull();
        assertThat(read.getCreator()).isEqualTo(original.getCreator());
        assertThat(read.getDescription()).isEqualTo(original.getDescription());
        assertThat(read.getName()).isEqualTo(original.getName());
    }
}
