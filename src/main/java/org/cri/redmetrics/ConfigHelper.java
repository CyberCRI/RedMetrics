package org.cri.redmetrics;

import org.cri.configurator.Config;
import org.cri.configurator.Configs;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by himmelattack on 23/02/15.
 */
public class ConfigHelper {

    public static Config<String, String> getDefaultConfig() {
        Config<String, String> config = null;
        String requiredOptions[] = {"databaseURL", "listenPort", "dbusername", "dbassword", "hostName"};

        try {
            config = Configs.getSimpleConfig(Paths.get("./redmetrics.conf"), requiredOptions);
            System.out.println("Using local redmetrics.conf");
        } catch (IOException ioex) {
            try {
                config = Configs.getSimpleConfig(Paths.get("/etc/redmetrics.conf"), requiredOptions);
                System.out.println("Using /etc/redmetrics.conf");
            } catch (IOException ex) {
                System.err.println("Error, missing redmetrics.conf");
            }
        }
        return config;
    }
}
