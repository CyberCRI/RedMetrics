
package org.cri.redmetrics.model;

import com.google.api.client.util.Key;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TestAddress extends TestEntity {

    @Key
    private String postalCode;

    @Key
    private String country;

}
