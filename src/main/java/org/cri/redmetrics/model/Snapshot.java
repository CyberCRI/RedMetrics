package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@DatabaseTable(tableName = "snapshots")
@NoArgsConstructor
@AllArgsConstructor
public class Snapshot implements Entity {

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

    @DatabaseField(canBeNull = false)
    private Date creationDate = new Date();

    @DatabaseField
    private String customData;

}
