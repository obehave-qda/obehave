package org.obehave.persistence;

import org.junit.Test;
import org.obehave.util.TestUtil;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Markus Möslinger
 */
public class DaosTest {
    @Test(expected = InvocationTargetException.class)
    public void cannotConstruct() throws ReflectiveOperationException {
        TestUtil.tryToCreateInstance(Daos.class);
    }
}
