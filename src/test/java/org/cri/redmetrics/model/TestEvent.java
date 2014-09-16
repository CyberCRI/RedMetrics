package org.cri.redmetrics.model;

import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestEvent extends TestProgressData {

    @Key
    private String type;

    @Key
    private String[] sections;

    @Key
    private String coordinates;

}
