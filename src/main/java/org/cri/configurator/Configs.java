
package org.cri.configurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 *
 * @author TTAK
 */
public class Configs {

    public static Config getSimpleConfig(Path path, String ... requiredOptions) throws IOException {
        SimpleLinuxLikeConfig config = new SimpleLinuxLikeConfig();
        config.read(path);
        
        Arrays.asList(requiredOptions).forEach(option -> {
            if(!config.config.containsKey(option)){
                throw new IllegalStateException("Missing property : " + option);
            }
        });
        
        return config;
        
    }

    private static class SimpleLinuxLikeConfig implements Config<String, String> {

        private final HashMap<String, String> config = new HashMap<>();

        public SimpleLinuxLikeConfig() {
        }

        @Override
        public void put(String key, String value) {
            config.put(key, value);
        }

        @Override
        public String get(String K) {
            return config.get(K);
        }

        @Override
        public void read(Path path) throws IOException {
            Files.lines(path).filter(line -> !(line.startsWith("#") || 
                                               line.isEmpty()))
                             .forEach(line -> {
                String[] splittedLine = line.split(" ");
                if (splittedLine.length < 2) {
                    String error = "Error parsing the file";
                    if (splittedLine.length == 1) {
                        throw new IllegalArgumentException(error
                                + " : missing value near key "
                                + splittedLine[0]);
                    }
                }
                config.put(splittedLine[0], splittedLine[1]);
            });
        }

        @Override
        public void write(Path path) throws IOException {
            Files.write(path,
                    config.entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + " " + entry.getValue()).collect(Collectors.toList()),
                    StandardOpenOption.CREATE);
        }
    }
}
