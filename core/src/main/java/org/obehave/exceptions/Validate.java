package org.obehave.exceptions;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author Markus Möslinger
 */
public class Validate {
    private Validate() {
        throw new AssertionError("No instantiation!");
    }

    public static void isNotNull(Object o) {
        if (o == null) {
            throw new ValidationException("Object " + o + " must not be null.");
        }
    }

    public static void isBetween(int n, int min, int max) {
        if (n < min || n > max) {
            throw new ValidationException("Integer " + n + " has to be between " + min + " and " + max);
        }
    }

    public static <T> void isOneOf(T n, T... numbers) {
        if (!Arrays.asList(numbers).contains(n)) {
            throw new ValidationException("Object " + n + " has to be in " + numbers);
        }
    }
}
