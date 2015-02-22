package org.obehave.persistence.modifier;

import org.junit.Test;
import org.obehave.model.modifier.ModifierFactory;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifierFactoryDaoTest extends ModifierFactoryDaoTest {
    @Test
    public void persistingModifierFactory() throws SQLException {
        ModifierFactory mf = new ModifierFactory("West", "East");
        dao.create(mf);

        ModifierFactory loadedModifierFactory = dao.queryForSameId(mf);

        assertModifierFactory(loadedModifierFactory, mf.getType(), mf.getName(), 0, 0, mf.getAlias(), Arrays.asList("West", "East"), null);

        loadedModifierFactory.addValidValues("North");
        dao.update(loadedModifierFactory);
        dao.refresh(loadedModifierFactory);
        assertModifierFactory(loadedModifierFactory, mf.getType(), mf.getName(), 0, 0, mf.getAlias(), Arrays.asList("West", "East", "North"), null);
    }

    @Test
    public void loadingPersistedModifierFactory() throws SQLException {
        ModifierFactory loadedModifierFactory = dao.queryForName("Slow Or Fast");

        assertModifierFactory(loadedModifierFactory, ModifierFactory.Type.ENUMERATION_MODIFIER_FACTORY, "Slow Or Fast",
                0, 0, null, Arrays.asList("Slow", "Fast"), null);
    }
}
