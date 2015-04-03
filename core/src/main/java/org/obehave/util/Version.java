package org.obehave.util;

/**
 * A class to hold a simple value - the current version of the compiled code.
 */
public class Version {
    private Version() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
    }

    public static final String CURRENT = "0.2.0-SNAPSHOT";
}
