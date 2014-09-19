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
    private Integer[] coordinates;

}
