package org.cri.redmetrics.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestEvent extends TestEntity {

    @Key
    private int game;

    @Key
    private int player;

    @Key
    private String type;

    @Key
    private String creationDate;

    @Key
    private GenericJson customData;

    @Key
    private String[] sections;

    @Key
    private long[] coordinates;

}
