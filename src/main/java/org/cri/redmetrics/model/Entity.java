package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import lombok.Data;

import java.util.UUID;

import static spark.Spark.halt;

@Data
public abstract class Entity {

    public static UUID parseId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            halt(400, e.getMessage());
            return null;
        }
    }

    @DatabaseField(generatedId = true)
    private UUID id;

}
