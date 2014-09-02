package org.cri.redmetrics;

import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestEvent extends TestEntity {

    @Key
    private int gameId;

    @Key
    private int playerId;

    @Key
    private String type;

    @Key
    private String subject;

    @Key
    private double value;

    @Key
    private String data;

    @Key
    private long[] coordinates;

}
