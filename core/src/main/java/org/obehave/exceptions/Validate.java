package org.obehave.exceptions;

import org.obehave.util.I18n;

import java.util.Arrays;
import java.util.Collection;

/**
 * Simple utility class to gather common validations for arguments
 */
public class Validate {
    private Validate() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
    }

    /**
     * Validates if an object is not null.
     * @param o the object to check
     * @param name the name to include in the exception
     * @throws org.obehave.exceptions.ValidationException if the object is null
     */
    public static void isNotNull(Object o, String name) {
        if (o == null) {
            throw new ValidationException(I18n.get("exception.validate.isnotnull", name));
        }
    }

    /**
     * Validates if a string is neither null nor empty
     * @param s the string to check
     * @param name the name to include in the exception
     * @throws org.obehave.exceptions.ValidationException if the string is either null or empty
     */
    public static void isNotEmpty(String s, String name) {
        if (s == null || s.isEmpty()) {
            throw new ValidationException(I18n.get("exception.validate.isnotempty", name, s));
        }
    }

    /**
     * Validates if n is between min and max (both including: {@code min} &lt;= {@code n} &lt;= {@code max}).
     * So, the range where n has to be in is [{@code min}; {@code max}]
     * @param n the integer to check
     * @param min the lower bound
     * @param max the upper bound
     * @throws org.obehave.exceptions.ValidationException if {@code n}
     * is either lower than {@code min} or higher than {@code max}
     */
    public static void isBetween(int n, int min, int max) {
        if (n < min || n > max) {
            throw new ValidationException(I18n.get("exception.validate.isbetween", n, min, max));
        }
    }

    /**
     * Validates if an object is equal to at least one given valid object
     * @param element the object to check
     * @param elements the elements which should contain {@code element}
     * @throws org.obehave.exceptions.ValidationException if {@code element} isn't part of {@code elements},
     * or if {@code element} is null
     */
    public static void isOneOf(Object element, Object... elements) {
        if (!Arrays.asList(elements).contains(element)) {
            throw new ValidationException(I18n.get("exception.validate.isoneof", element, elements));
        }
    }

    /**
     * Validates if a collection has exactly one element.
     * @param collection the collection to validate
     * @param <T> a collection or any of it's subtypes
     * @return the collection itself
     * @throws org.obehave.exceptions.ValidationException if there are no or more than one elements in the collection
     */
    public static <T extends Collection> T hasOnlyOneElement(T collection) {
        isNotNull(collection, "Collection");

        if (collection.size() != 1) {
            throw new ValidationException(I18n.get("exception.validate.onlyoneelement", collection.size()));
        }

        return collection;
    }
}
