
package org.cri.redmetrics;

import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Besnard Arthur
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TestGroup extends TestEntity{
    @Key
    private String name;
    
    @Key
    private String description;
    
    @Key
    private String creator;
    
    @Key
    private boolean open;
}
