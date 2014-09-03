package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ProgressData extends Entity {

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
