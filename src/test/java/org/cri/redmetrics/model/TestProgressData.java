package org.cri.redmetrics.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TestProgressData extends TestEntity {

    @Key
    private String gameVersion;

    @Key
    private String player;

    @Key
    private String serverTime;

    @Key
    private String userTime;

    @Key
    private GenericJson customData;

    @Key
    private String section;

}
