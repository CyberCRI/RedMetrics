package org.cri.redmetrics;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import org.cri.configurator.Config;
import org.cri.configurator.Configs;
import org.cri.redmetrics.db.Db;

public class Main {

    public static void main(String[] args) throws SQLException {
                
        Config<String, String> config;
        String requiredOptions[] = {"databaseURL", "listenPort", "dbusername", "dbassword", "hostName"};

        try {
            config = Configs.getSimpleConfig(Paths.get("./redmetrics.conf"), requiredOptions);
            System.out.println("Using local redmetrics.conf");
        } catch(IOException ioex) {
            try {
                config = Configs.getSimpleConfig(Paths.get("/etc/redmetrics.conf"), requiredOptions);
                System.out.println("Using /etc/redmetrics.conf");
            } catch (IOException ex) {
                System.err.println("Error, missing redmetrics.conf");
                return;
            }
        }

        Db db = new Db(config.get("databaseURL"), config.get("dbusername"), config.get("dbassword"));
        Server.hostName = config.get("hostName");
        Server server = new Server(Integer.parseInt(config.get("listenPort")), db);
        server.start();
    }

}
