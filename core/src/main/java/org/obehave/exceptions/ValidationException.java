package org.obehave.exceptions;

/**
 * @author Markus Möslinger
 */
public class ValidationException extends IllegalArgumentException {
    public ValidationException(String s) {
        super(s);
    }
}
