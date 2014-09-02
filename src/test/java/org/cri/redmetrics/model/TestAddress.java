
package org.cri.redmetrics.model;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestAddress extends TestEntity {

//    @Key
//    private String[] lines;
    
    @Key
    private String postalCode;

    @Key
    private String country;

}
