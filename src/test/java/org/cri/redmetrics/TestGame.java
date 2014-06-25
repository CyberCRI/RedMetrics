package org.cri.redmetrics;


import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public class TestGame extends GenericJson {

    @Key
    private int id;

    @Key
    private String apiKey;

    @Key
    private String name;

}
