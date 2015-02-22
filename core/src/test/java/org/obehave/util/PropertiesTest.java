package org.obehave.util;

import org.junit.Test;
import org.obehave.persistence.DatabaseTest;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Markus Möslinger
 */
public class PropertiesTest extends DatabaseTest {
    @Test(expected = InvocationTargetException.class)
    public void cannotConstruct() throws ReflectiveOperationException {
        TestUtil.tryToCreateInstance(Properties.class);
    }
}
