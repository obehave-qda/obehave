package org.obehave.persistence.modifier;

import org.junit.Test;
import org.obehave.model.modifier.Modifier;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class EnumerationModifierDaoTest extends ModifierDaoTest {
    @Test
    public void persistingModifier() throws SQLException {
        Modifier modifier = new Modifier(queryModifierFactory("Slow Or Fast"), "Fast");

        dao.create(modifier);

        Modifier loadedModifier = dao.queryForSameId(modifier);

        assertModifier(loadedModifier, modifier.getModifierFactory(), modifier.get());
    }

    @Test
    public void loadPersistedModifier() throws SQLException {
        Modifier modifier = dao.queryForType(Modifier.Type.ENUMERATION_MODIFIER).get(0);

        assertModifier(modifier, queryModifierFactory("Slow Or Fast"), "Slow");
    }
}
