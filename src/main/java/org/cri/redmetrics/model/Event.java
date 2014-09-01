package org.cri.redmetrics.model;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DatabaseTable(tableName = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Entity {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(
            canBeNull = false,
            foreign = true,
            columnDefinition = "INT, FOREIGN KEY (game_id) REFERENCES games(id)")
    private Game game;

    @DatabaseField(
            canBeNull = false,
            foreign = true,
            columnDefinition = "INT, FOREIGN KEY (player_id) REFERENCES players(id)")
    private Player player;

    @DatabaseField
    private EventType type;

    @DatabaseField
    private String subject;

    @DatabaseField
    private double value;

    @DatabaseField
    private String data;
    
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private long[] coordinates;

}
