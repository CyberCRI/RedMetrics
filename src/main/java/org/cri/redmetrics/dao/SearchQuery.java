package org.cri.redmetrics.dao;

import com.j256.ormlite.stmt.Where;
import org.cri.redmetrics.model.Entity;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static spark.Spark.halt;

public class SearchQuery<E extends Entity> {

    private final Where where;

    private boolean hasStatement = false;

    SearchQuery(Where where) {
        this.where = where;
    }

    public List<E> execute() {
        if (!hasStatement) halt(400, "No parameters were specified for search query");
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

    public SearchQuery foreignEntity(String entityName, Stream<UUID> ids) {
        try {
            addAndIfNecessary();
            for (Iterator<UUID> i = ids.iterator(); i.hasNext(); ) {
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
            where.le("creationDate", date);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public SearchQuery after(Date date) {
        try {
            addAndIfNecessary();
            where.gt("creationDate", date);
            return this;
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

//    public SearchQuery values(String columnName, List<String> values) {
//        try {
//            addAndIfNecessary();
//            for (Iterator<String> i = values.iterator(); i.hasNext(); ) {
//                where.eq(columnName, i.next());
//                if (i.hasNext()) where.or();
//            }
//            return this;
//        } catch (SQLException e) {
//            throw new DbException(e);
//        }
//    }

}
