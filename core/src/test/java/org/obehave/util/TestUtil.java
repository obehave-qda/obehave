package org.obehave.util;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Utility class which is only used in tests
 */
public class TestUtil {
    private TestUtil() {
        throw new AssertionError(I18n.get("exception.constructor.utility"));
    }

    /**
     * Tries to create a instance of a class with a private constructor
     * @param clazz the class to create
     * @throws ReflectiveOperationException if there were errors (there should better be!)
     */
    public static void tryToCreateInstance(Class<?> clazz) throws ReflectiveOperationException {
        final Constructor<?> privateConstructor = clazz.getDeclaredConstructor();

        assertEquals(1, clazz.getDeclaredConstructors().length);
        assertFalse(privateConstructor.isAccessible());

        privateConstructor.setAccessible(true);
        privateConstructor.newInstance();
    }
}
