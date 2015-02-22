package org.obehave.util;

import org.junit.Test;
import org.obehave.persistence.DatabaseTest;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Markus Möslinger
 */
public class I18nTest extends DatabaseTest {
    @Test(expected = InvocationTargetException.class)
    public void cannotConstruct() throws ReflectiveOperationException {
        TestUtil.tryToCreateInstance(I18n.class);
    }
}
