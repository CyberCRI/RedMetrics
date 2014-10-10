package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.GameVersion;

import java.sql.SQLException;

public class GameVersionDao extends EntityDao<GameVersion> {

    @Inject
    public GameVersionDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, GameVersion.class);
    }

}
