package org.cri.redmetrics;


import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class TestGame extends TestEntity {

    @Key
    private String apiKey;

    @Key
    private String name;

}
