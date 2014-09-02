package org.cri.redmetrics.model;


import com.google.api.client.util.Key;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestGame extends TestEntity {

    @Key
    private String adminKey;

    @Key
    private String name;

}
