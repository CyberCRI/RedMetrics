package org.cri.redmetrics.model;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@DatabaseTable(tableName = "games")
@NoArgsConstructor
@AllArgsConstructor
public class Game implements Entity {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(dataType = DataType.UUID)
    private UUID adminKey;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String author;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] credits;

}