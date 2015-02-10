package org.cri.redmetrics.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import org.cri.redmetrics.model.GameVersion;
import org.cri.redmetrics.model.ProgressData;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static spark.Spark.halt;

public class SearchQuery<E extends ProgressData> {

    private final QueryBuilder<E, UUID> queryBuilder;
    private final QueryBuilder<GameVersion, UUID> gameVersionQueryBuilder;
    private final Dao<E, UUID> orm;
    private Where where;
    private final Where whereGameVersion;

    private boolean hasStatement = false;
    private boolean hasGameFilter = false;

    SearchQuery(Dao<E, UUID> orm, QueryBuilder<E, UUID> queryBuilder, QueryBuilder<GameVersion, UUID> gameVersionQueryBuilder) {
        this.orm = orm;
        this.queryBuilder = queryBuilder;
        this.gameVersionQueryBuilder = gameVersionQueryBuilder;
        this.whereGameVersion = gameVersionQueryBuilder.where();
    }

    public List<E> execute() {
        if (!hasStatement && !hasGameFilter) halt(400, "No parameters were specified for search query");
        try {
            return queryBuilder.query();
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public long countResults() {
        try {
            queryBuilder.setCountOf(true);
            long count = orm.countOf(queryBuilder.prepare());
            queryBuilder.setCountOf(false);
            return count;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    private void addAndIfNecessary() {
        if (hasStatement) {
            where.and();
        } else {
            where = queryBuilder.where();
            hasStatement = true;
        }
    }

    public SearchQuery game(Stream<UUID> ids) {
        try {
            Iterator<UUID> i = ids.iterator();
            if (i.hasNext()) {
                hasGameFilter = true;
                queryBuilder.join(gameVersionQueryBuilder);
            }
            while (i.hasNext()) {
                whereGameVersion.eq("game_id", i.next());
                if (i.hasNext()) whereGameVersion.or();
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
        return this;
    }

    public SearchQuery foreignEntity(String entityName, Stream<UUID> ids) {
        try {
            Iterator<UUID> i = ids.iterator();
            if (i.hasNext()) {
                addAndIfNecessary();
            }
            while (i.hasNext()) {
                where.eq(entityName + "_id", i.next());
                if (i.hasNext()) where.or();
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
        return this;
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

    public SearchQuery before(Date date) {
        try {
            addAndIfNecessary();
            where.le("serverTime", date);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery after(Date date) {
        try {
            addAndIfNecessary();
            where.ge("serverTime", date);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery beforeUserTime(Date date) {
        try {
            addAndIfNecessary();
            where.le("userTime", date);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery afterUserTime(Date date) {
        try {
            addAndIfNecessary();
            where.ge("userTime", date);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery sections(String sections) {
        addAndIfNecessary();
        where.raw("sections ~ '" + sections + "'");
        return this;
    }

    public SearchQuery paginate(long page, long perPage) {
        try {
            // Page number is 1-based
            queryBuilder.offset((page - 1) * perPage).limit(perPage);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

}
