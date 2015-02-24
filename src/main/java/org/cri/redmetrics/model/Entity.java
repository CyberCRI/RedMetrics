package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import lombok.Data;

import java.util.UUID;

import static spark.Spark.halt;

@Data
public abstract class Entity {

    public static UUID parseId(String id) {
        return UUID.fromString(id);
    }

    @DatabaseField(generatedId = true)
    private UUID id;

}
