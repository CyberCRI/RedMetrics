package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class Entity {

    public static UUID parseId(String id) {
        return UUID.fromString(id);
    }

    @DatabaseField(generatedId = true)
    private UUID id;

    @DatabaseField(columnDefinition = "text")
    private String customData;

}
