package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@DatabaseTable(tableName = "game_versions")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GameVersion extends Entity {

    @DatabaseField(
            canBeNull = false,
            foreign = true,
            columnDefinition = "VARCHAR, FOREIGN KEY (game_id) REFERENCES games(id)")
    private Game game;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String author;

    @DatabaseField
    private String description;

}
