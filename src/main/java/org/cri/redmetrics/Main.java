package org.cri.redmetrics;

import org.cri.configurator.Config;
import org.cri.redmetrics.db.Db;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        Config<String, String> config = ConfigHelper.getDefaultConfig();

        Db db = new Db(config.get("databaseURL"), config.get("dbusername"), config.get("dbassword"));
        Server.hostName = config.get("hostName");
        Server server = new Server(Integer.parseInt(config.get("listenPort")), db);
        server.start();
    }

}
