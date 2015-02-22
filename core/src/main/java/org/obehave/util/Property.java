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

    static {
        final String propertyFile = "obehave.properties";
        try {
            log.debug("Loading properties");
            properties.load(Property.class.getClassLoader().getResourceAsStream(propertyFile));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load property file: " + propertyFile);
        }
    }

    private Property() {
        throw new AssertionError(I18n.getString("exception.constructor.utility"));
    }

    public static String getLanguage() {
        return trySystemPropertyFirst("language");
    }

    public static boolean isDatabaseDebug() {
        return trySystemPropertyFirst("database.debug").equals("true");
    }

    public static String getDatabaseDebugPortWeb() {
        return trySystemPropertyFirst("database.debug.port.web");
    }

    public static String getDatabaseDebugPortTcp() {
        return trySystemPropertyFirst("database.debug.port.tcp");
    }

    private static String trySystemPropertyFirst(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }
}
