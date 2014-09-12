package org.cri.redmetrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Console {

    private static final Logger logger = LoggerFactory.getLogger(Console.class);

    public static void log(String message) {
        logger.info(message);
    }
}
