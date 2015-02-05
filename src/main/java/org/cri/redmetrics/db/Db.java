package org.cri.redmetrics.db;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.cri.redmetrics.model.*;

import java.sql.SQLException;

public class Db {

    public static final Class[] ENTITY_TYPES = {
            Event.class,
            Snapshot.class,
            Game.class,
            GameVersion.class,
            Group.class,
            Player.class};
    
    private final String URL;
    private final String user;
    private final String password;


    public Db(String databaseURL, String user, String password) {
        this.URL = databaseURL;
        this.user = user;
        this.password = password;
        
    }

    public JdbcConnectionSource newConnectionSource() throws SQLException {
        return new JdbcConnectionSource(URL, user, password);
    }

}
