package org.cri.redmetrics;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import lombok.Data;

@Data
public abstract class TestEntity extends GenericJson{
    @Key
    private int id;
}
