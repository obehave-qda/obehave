package org.obehave.persistence.modifier;

import org.junit.Test;
import org.obehave.model.modifier.Modifier;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class NumberModifierDaoTest extends ModifierDaoTest {
    @Test
    public void persistingModifier() throws SQLException {
        Modifier modifier = new Modifier(queryModifierFactory("One To Five"), BigDecimal.valueOf(3), "3");

        dao.create(modifier);
        Modifier loadedModifier = dao.queryForSameId(modifier);

        assertModifier(loadedModifier, modifier.getModifierFactory(), modifier.get());
    }

    @Test
    public void loadPersistedModifier() throws SQLException {
        // according to populate.sql, modifier 0 is a number modifier
        Modifier modifier = dao.queryForType(Modifier.Type.DECIMAL_MODIFIER).get(0);

        assertModifier(modifier, queryModifierFactory("One To Five"), BigDecimal.valueOf(2));
    }

}
