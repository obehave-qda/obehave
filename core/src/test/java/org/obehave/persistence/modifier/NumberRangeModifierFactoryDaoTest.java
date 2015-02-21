package org.obehave.persistence.modifier;

import org.junit.Test;
import org.obehave.model.modifier.ModifierFactory;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Markus Möslinger
 */
public class NumberRangeModifierFactoryDaoTest extends ModifierFactoryDaoTest {
    @Test
    public void persistingModifierFactory() throws SQLException {
        ModifierFactory modifierFactory = new ModifierFactory(2, 7);
        modifierFactory.setName("Two To Seven");

        dao.create(modifierFactory);

        ModifierFactory loadedModifierFactory = dao.queryForName(modifierFactory.getName());
        assertNumberRangeModifier(loadedModifierFactory, modifierFactory.getName(), modifierFactory.getFrom(), modifierFactory.getTo());
    }

    @Test
    public void loadPersistedModifierFactory() throws SQLException {
        ModifierFactory modifierFactory = dao.queryForName("One To Five");

        assertNumberRangeModifier(modifierFactory, "One To Five", 1, 5);
    }

    private void assertNumberRangeModifier(ModifierFactory modifierFactory, String name, int from, int to) {
        assertEquals(name, modifierFactory.getName());
        assertEquals(from, modifierFactory.getFrom());
        assertEquals(to, modifierFactory.getTo());
        assertNull(modifierFactory.getAlias());
    }
}
