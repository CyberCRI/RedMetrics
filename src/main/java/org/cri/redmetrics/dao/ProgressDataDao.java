package org.cri.redmetrics.dao;

import com.j256.ormlite.support.ConnectionSource;
import org.cri.redmetrics.model.ProgressData;

import java.sql.SQLException;

public abstract class ProgressDataDao<E extends ProgressData> extends EntityDao<E> {

    protected final GameVersionDao gameVersionDao;
    protected final PlayerDao playerDao;

    ProgressDataDao(ConnectionSource connectionSource, GameVersionDao gameVersionDao, PlayerDao playerDao, Class<E> type) throws SQLException {
        super(connectionSource, type);
        this.gameVersionDao = gameVersionDao;
        this.playerDao = playerDao;
    }

    public SearchQuery<E> search() {
        return new SearchQuery<>(orm, orm.queryBuilder(), gameVersionDao.orm.queryBuilder(), playerDao.orm.queryBuilder());
    }

}