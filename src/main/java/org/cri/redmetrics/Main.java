package org.cri.redmetrics;

import java.sql.SQLException;

import org.cri.configurator.Config;
import org.cri.redmetrics.db.Db;

public class Main {

    public static void main(String[] args) throws SQLException {
        Config<String, String> config = ConfigHelper.getDefaultConfig();

        Db db = new Db(config.get("databaseURL"), config.get("dbusername"), config.get("dbassword"));
        Server.hostName = config.get("hostName");
        Server server = new Server(Integer.parseInt(config.get("listenPort")), db);
        server.start();
    }

}
