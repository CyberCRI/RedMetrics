package org.cri.redmetrics.util;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.cri.redmetrics.db.Db;
import org.cri.redmetrics.model.Entity;

import java.sql.SQLException;

public class DbUtils {

    public static void dropAllTables() throws SQLException {
        ConnectionSource cs = Db.newConnectionSource();
        for (Class<Entity> entityType : Db.ENTITY_TYPES) {
            TableUtils.dropTable(cs, entityType, true);
        }
    }

    public static void main(String[] args) throws SQLException {
        dropAllTables();
    }

}
