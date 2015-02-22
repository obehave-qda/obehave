package org.obehave.util;

import org.junit.Test;
import org.obehave.persistence.DatabaseTest;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class DatabasePropertiesTest extends DatabaseTest {
    private static final String KEY = "testproperty";
    private static final String KEY_EXISTING = "existingkey";
    private static final String VALUE_BEFORE = "valuebefore";
    private static final String VALUE_AFTER = "valueafter";

    @Test
    public void readAndWriteProperty() {
        DatabaseProperties.set(KEY, VALUE_BEFORE);
        assertEquals(VALUE_BEFORE, DatabaseProperties.get(KEY));
    }

    @Test
    public void updateProperty() {
        DatabaseProperties.set(KEY, VALUE_BEFORE);
        assertEquals(VALUE_BEFORE, DatabaseProperties.get(KEY));

        DatabaseProperties.set(KEY, VALUE_AFTER);
        assertEquals(VALUE_AFTER, DatabaseProperties.get(KEY));
    }

    @Test
    public void updateExistingProperty() {
        DatabaseProperties.set(KEY_EXISTING, VALUE_AFTER);
        assertEquals(VALUE_AFTER, DatabaseProperties.get(KEY_EXISTING));
    }
}
