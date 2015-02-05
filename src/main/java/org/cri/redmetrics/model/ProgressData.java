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
            columnDefinition = "VARCHAR, FOREIGN KEY (\"gameVersion_id\") REFERENCES game_versions(id)")
    private GameVersion gameVersion;

    @DatabaseField(
            canBeNull = false,
            foreign = true,
            columnDefinition = "VARCHAR, FOREIGN KEY (player_id) REFERENCES players(id)")
    private Player player;

    @DatabaseField(canBeNull = false)
    private Date serverTime = new Date();

    @DatabaseField
    private Date userTime;

    @DatabaseField(columnDefinition = "text")
    private String customData;

    @DatabaseField(persisterClass = LtreePersister.class, columnDefinition = "ltree")
    private String sections;

}
