package org.obehave.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple wrapper for {@link ResourceBundle}, to shorten access to I18n strings
 */
public class I18n {
    private static final Locale configuredLocale = new Locale(Properties.getLanguage());

    private I18n() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
    }

    public static ResourceBundle bundle() {
        return bundle(configuredLocale);
    }

    public static ResourceBundle bundle(Locale locale) {
        return ResourceBundle.getBundle("language.lang", locale);
    }

    public static String get(String key) {
        return bundle().getString(key);
    }

    public static String get(String key, Object... substitutions) {
        return MessageFormat.format(get(key), substitutions);
    }
}
