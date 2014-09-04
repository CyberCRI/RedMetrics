package org.cri.redmetrics.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestProgressData extends TestEntity {

    @Key
    private String game;

    @Key
    private String player;

    @Key
    private String creationDate;

    @Key
    private GenericJson customData;

}
