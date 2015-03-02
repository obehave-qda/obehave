package org.obehave.exceptions;

import org.junit.Test;
import org.obehave.util.TestUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Markus Möslinger
 */
public class ValidateTest {
    @Test(expected = InvocationTargetException.class)
    public void cannotConstruct() throws ReflectiveOperationException {
        TestUtil.tryToCreateInstance(Validate.class);
    }

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

    @Test
    public void hasOnlyOneElementDoesntThrowExceptionWhenOnlyOneElement() {
        Validate.hasOnlyOneElement(Collections.singletonList("Only Element"));
    }

    @Test(expected = ValidationException.class)
    public void hasOnlyOneElementDoesThrowExceptionWhenMoreThanOneElement() {
        Validate.hasOnlyOneElement(Arrays.asList("First", "Second"));
    }

    @Test(expected = ValidationException.class)
    public void hasOnlyOneElementDoesThrowExceptionWhenNoElement() {
        Validate.hasOnlyOneElement(Collections.emptyList());
    }

    @Test(expected = ValidationException.class)
    public void hasOnlyOneElementDoesThrowExceptionWhenNullPassed() {
        Validate.hasOnlyOneElement(null);
    }
}
