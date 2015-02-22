package org.obehave.exceptions;

import org.obehave.util.I18n;

import java.util.Arrays;

/**
 * @author Markus Möslinger
 */
public class Validate {
    private Validate() {
        throw new AssertionError(I18n.getString("exception.constructor.utility"));
    }

    public static void isNotNull(Object o, String name) {
        if (o == null) {
            throw new ValidationException(I18n.getString("exception.validate.isnotnull", name));
        }
    }

    public static void isNotEmpty(String s, String name) {
        if (s == null || s.isEmpty()) {
            throw new ValidationException(I18n.getString("exception.validate.isnotempty", name, s));
        }
    }

    public static void isBetween(int n, int min, int max) {
        if (n < min || n > max) {
            throw new ValidationException(I18n.getString("exception.validate.isbetween", n, min, max));
        }
    }

    public static <T> void isOneOf(T element, T... elements) {
        if (!Arrays.asList(elements).contains(element)) {
            throw new ValidationException(I18n.getString("exception.validate.isoneof", element, elements));
        }
    }
}
