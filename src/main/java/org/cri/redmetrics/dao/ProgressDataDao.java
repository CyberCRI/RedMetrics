package org.cri.redmetrics.dao;

import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.ProgressData;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class ProgressDataDao<E extends ProgressData> extends EntityDao<E> {

    ProgressDataDao(ConnectionSource connectionSource, Class<E> type) throws SQLException {
        super(connectionSource, type);
    }

    public List<E> searchByGame(UUID gameId) {
        try {
            return orm.queryBuilder().where().eq("game_id", gameId).query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}