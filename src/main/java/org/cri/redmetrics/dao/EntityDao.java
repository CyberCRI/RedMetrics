package org.cri.redmetrics.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.cri.redmetrics.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public abstract class EntityDao<E extends Entity> {

    private static final Logger logger = LoggerFactory.getLogger(EntityDao.class);

    protected final Dao<E, Integer> orm;

    EntityDao(ConnectionSource connectionSource) throws SQLException {
        try {
            TableUtils.createTableIfNotExists(connectionSource, getEntityType());
        } catch (SQLException e) {
            logger.info("Problem when trying to create table, probably concerning unique id sequence. Shouldn't be a problem.");
        }
        this.orm = DaoManager.createDao(connectionSource, getEntityType());
    }

    protected abstract Class<E> getEntityType();

    public E create(E entity) throws DbException {
        try {
            orm.create(entity);
            return entity;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public E read(int id) throws DbException {
        try {
            return orm.queryForId(id);
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public List<E> list() throws DbException {
        try {
            return orm.queryForAll();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
