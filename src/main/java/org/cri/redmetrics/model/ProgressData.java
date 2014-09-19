package org.cri.redmetrics.model;

import com.j256.ormlite.field.DatabaseField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cri.redmetrics.db.LtreePersister;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ProgressData extends Entity {

    @DatabaseField(
            canBeNull = false,
            foreign = true,
            columnDefinition = "VARCHAR, FOREIGN KEY (game_id) REFERENCES games(id)")
    private Game game;

    @DatabaseField(
            canBeNull = false,
            foreign = true,
            columnDefinition = "VARCHAR, FOREIGN KEY (player_id) REFERENCES players(id)")
    private Player player;

    @DatabaseField(canBeNull = false)
    private Date serverTime = new Date();

    @DatabaseField
    private Date userTime;

    @DatabaseField
    private String customData;

    @DatabaseField(persisterClass = LtreePersister.class, columnDefinition = "ltree")
    private String sections;

}
