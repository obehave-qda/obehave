package org.obehave.exceptions;

import org.junit.Test;

/**
 * @author Markus Möslinger
 */
public class ValidateTest {
    @Test(expected = ValidationException.class)
    public void isNotNullThrowsExceptionIfNull() {
        Validate.isNotNull(null, "Test");
    }

    @Test
    public void isNotNullDoesntThrowExceptionIfNotNull() {
        Validate.isNotNull("Not null", "Something");
    }

    @Test(expected = ValidationException.class)
    public void isNotEmtpyThrowsExceptionWhenStringIsNull() {
        Validate.isNotEmpty(null, "Test");
    }

    @Test(expected = ValidationException.class)
    public void isNotEmptyThrowsExceptionWhenStringIsEmpty() {
        Validate.isNotEmpty("", "Test");
    }

    @Test
    public void isNotEmptyDoesntThrowExcpetionWhenStringIsntEmpty() {
        Validate.isNotEmpty(" ", "Blanks aren't empty!");
    }

    @Test(expected = ValidationException.class)
    public void isBetweenThrowsExceptionIfLowerThanMin() {
        Validate.isBetween(0, 1, 2);
    }

    @Test(expected = ValidationException.class)
    public void isBetweenThrowsExceptionIfHigherThanMax() {
        Validate.isBetween(3, 0, 2);
    }

    @Test
    public void isBetweenDoesntThrowExceptionIfEqualThanMin() {
        Validate.isBetween(0, 0, 3);
    }

    @Test
    public void isBetweenDoesntThrowExceptionIfEqualToMax() {
        Validate.isBetween(3, 0, 3);
    }

    @Test
    public void isBetweenDoesntThrowExceptionIfBetwenMinAndMax() {
        Validate.isBetween(3, 0, 4);
    }

    @Test(expected = ValidationException.class)
    public void isOneOfThrowsExceptionIfItIsntOneOf() {
        Validate.isOneOf("a", "b", "c", "d");
    }

    @Test(expected = ValidationException.class)
    public void isOneOfThrowsExceptionIfElementIsNull() {
        Validate.isOneOf(null, 1, 2, 3);
    }

    @Test(expected = ValidationException.class)
    public void isOneOfThrowsExceptionIfElementsAreNull() {
        Validate.isOneOf("Oh");
    }

    @Test
    public void isOneOfDoesntThrowExceptionIfItsOneOf() {
        Validate.isOneOf("a", "a", "b", "c", "d");
    }
}
