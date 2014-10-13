package org.cri.redmetrics.dao;

import com.google.inject.Inject;
import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.GameVersion;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class GameVersionDao extends EntityDao<GameVersion> {

    @Inject
    public GameVersionDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, GameVersion.class);
    }

    public List<GameVersion> searchByGameId(UUID gameId) {
        try {
            return orm.queryForEq("game_id", gameId);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
