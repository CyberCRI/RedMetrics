package org.cri.redmetrics.dao;

import com.j256.ormlite.stmt.Where;
import org.cri.redmetrics.model.Entity;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class SearchQuery<E extends Entity> {

    private final Where where;

    private boolean hasStatement = false;

    SearchQuery(Where where) {
        this.where = where;
    }

    public List<E> execute() {
        try {
            return where.query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private void addAndIfNecessary() {
        if (hasStatement) where.and();
        else hasStatement = true;
    }

    public SearchQuery foreignEntity(String columnName, UUID id) {
        try {
            addAndIfNecessary();
            where.eq(columnName, id);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery foreignEntities(String columnName, List<UUID> ids) {
        try {
            addAndIfNecessary();
            for (Iterator<UUID> i = ids.iterator(); i.hasNext(); ) {
                where.eq(columnName, i.next());
                if (i.hasNext()) where.or();
            }
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery value(String columnName, String value) {
        try {
            addAndIfNecessary();
            where.eq(columnName, value);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery values(String columnName, List<String> values) {
        try {
            addAndIfNecessary();
            for (Iterator<String> i = values.iterator(); i.hasNext(); ) {
                where.eq(columnName, i.next());
                if (i.hasNext()) where.or();
            }
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery game(UUID gameId) {
        return foreignEntity("game_id", gameId);
    }

    public SearchQuery games(List<UUID> gameIds) {
        return foreignEntities("game_id", gameIds);
    }

    public SearchQuery player(UUID playerId) {
        return foreignEntity("player_id", playerId);
    }

    public SearchQuery type(String type) {
        return value("type", type);
    }
}
