
package org.cri.redmetrics;

import com.google.api.client.util.Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Besnard Arthur
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TestAddress extends TestEntity {

//    @Key
//    private String[] lines;
    private String postalCode;

    @Key
    private String country;

}
