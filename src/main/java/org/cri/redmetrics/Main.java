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
        String requiredOptions[] = {"databaseURL", "listenPort", "dbusername", "dbassword"};
        try{
            config = Configs.getSimpleConfig(Paths.get("/etc/redmetrics.conf"), requiredOptions);
        }catch(IOException ioex){
            try {
                System.out.println("Warning, missing redmetrics.conf in /etc/, redmetrics will use local config file");
                config = Configs.getSimpleConfig(Paths.get("./redmetrics.conf"), requiredOptions);
            } catch (IOException ex) {
                System.err.println("Error, missing redmetrics.conf");
                return;
            }
        }
        
        new Server(Integer.parseInt(config.get("listenPort")),
                                    new Db(config.get("databaseURL"), 
                                           config.get("dbusername"),
                                           config.get("dbassword")))
                .start();
    }

}
