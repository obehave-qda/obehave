package org.obehave.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Class to retrieve properties either from environment or from properties file
 * <p/>
 * The usage of this class differs from {@link DatabaseProperties} - they could get streamlined in the future
 */
public class Properties {
    private static final String TRUE = "true";

    private static final Logger log = LoggerFactory.getLogger(Properties.class);
    private static final java.util.Properties properties = new java.util.Properties();

    static {
        final String propertyFile = "obehave.properties";
        try {
            log.debug("Loading properties");
            properties.load(Properties.class.getClassLoader().getResourceAsStream(propertyFile));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load property file: " + propertyFile);
        }
    }

    private Properties() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
    }

    public static String getLanguage() {
        return trySystemPropertyFirst("language");
    }

    public static File getSaveFolder() {
        return new File(trySystemPropertyFirst("defaultsavefolder"));
    }

    public static String getDatabaseConnectionStringWithInit(File path) {
        return getDatabaseConnectionString(path) + trySystemPropertyFirst("database.connectionstring.initsuffix");
    }


    public static String getDatabaseConnectionString(File path) {
        String absolutePath = FileUtil.removeSuffixIfThere(path, getDatabaseSuffix());
        return trySystemPropertyFirst("database.connectionstring", absolutePath);
    }

    public static String getDatabaseConnectionStringInitSuffix() {
        return trySystemPropertyFirst("database.connectionstring.initsuffix");
    }

    public static String getDatabaseSuffix() {
        return trySystemPropertyFirst("database.suffix");
    }

    public static boolean isUiErrorExceptionsShow() {
        return trySystemPropertyFirst("ui.error.exceptions.show").equals(TRUE);
    }

    public static boolean isDatabaseDebug() {
        return trySystemPropertyFirst("database.debug").equals(TRUE);
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

    private static String trySystemPropertyFirst(String key, Object... substitutions) {
        return MessageFormat.format(trySystemPropertyFirst(key), substitutions);
    }
}
