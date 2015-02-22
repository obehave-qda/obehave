package org.obehave.persistence.modifier;

import org.junit.Test;
import org.obehave.model.modifier.ModifierFactory;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class NumberRangeModifierFactoryDaoTest extends ModifierFactoryDaoTest {
    @Test
    public void persistingModifierFactory() throws SQLException {
        ModifierFactory mf = new ModifierFactory(2, 7);
        mf.setName("Two To Seven");

        dao.create(mf);

        ModifierFactory loadedModifierFactory = dao.queryForName(mf.getName());
        assertModifierFactory(loadedModifierFactory, mf.getType(), mf.getName(), mf.getFrom(),
                mf.getTo(), mf.getAlias(), null, null);
    }

    @Test
    public void loadPersistedModifierFactory() throws SQLException {
        ModifierFactory modifierFactory = dao.queryForName("One To Five");

        assertModifierFactory(modifierFactory, ModifierFactory.Type.DECIMAL_RANGE_MODIFIER_FACTORY, "One To Five", 1, 5, null, null, null);
    }
}
