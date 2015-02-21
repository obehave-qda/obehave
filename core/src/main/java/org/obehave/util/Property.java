package org.obehave.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Markus Möslinger
 */
public class Property {
    private static final Logger log = LoggerFactory.getLogger(Property.class);
    private final static Properties properties = new Properties();

    {
        try {
            log.debug("Loading properties");
            properties.load(getClass().getClassLoader().getResourceAsStream("obehave.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load property file");
        }
    }

    private Property() {
        throw new AssertionError("Can't instantiate utility classes");
    }

    public static boolean isDatabaseDebug() {
        return trySystemPropertyFirst("debugDatabase").equals("true");
    }

    private static String trySystemPropertyFirst(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }
}
