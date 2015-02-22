package org.obehave.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Markus Möslinger
 */
public class I18n {
    private static final Locale configuredLocale = new Locale(Property.getLanguage());

    private I18n() {
        throw new AssertionError("Utility class");
    }

    public static ResourceBundle bundle() {
        return bundle(configuredLocale);
    }

    public static ResourceBundle bundle(Locale locale) {
        return ResourceBundle.getBundle("language.lang", locale);
    }

    public static String getString(String key) {
        return bundle().getString(key);
    }
}
