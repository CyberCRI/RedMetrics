package org.cri.redmetrics.db;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.cri.redmetrics.model.*;

import java.sql.SQLException;

public class Db {

    public static final String URL = "jdbc:postgresql://localhost:5432/test";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "admin";
    public static final Class[] ENTITY_TYPES = {Address.class, Event.class, Game.class, Group.class, Player.class};

    public static JdbcConnectionSource newConnectionSource() throws SQLException {
        return new JdbcConnectionSource(URL, USERNAME, PASSWORD);
    }

}
