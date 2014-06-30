
package org.cri.redmetrics;

import java.io.IOException;

/**
 *
 * @author Besnard Arthur
 */
public class PlayerBackendTest extends HttpBackendTest<TestPlayer>{

    public PlayerBackendTest() {
        super("player/", TestPlayer.class);
    }

    @Override
    void init() throws IOException {
        
    }
    
}
