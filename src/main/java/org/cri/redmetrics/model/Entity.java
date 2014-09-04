package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import lombok.Data;

import java.util.UUID;

@Data
public abstract class Entity {

    @DatabaseField(generatedId = true)
    private UUID id;

}
