
package org.cri.redmetrics.model;

import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
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
