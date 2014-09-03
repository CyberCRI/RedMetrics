package org.cri.redmetrics.model;

import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestSnapshot extends TestProgressData {

    @Key
    private String gameTime;

}
