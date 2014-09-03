package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import lombok.Data;

@Data
public abstract class Entity {

    @DatabaseField(generatedId = true)
    private int id;


}
