package org.obehave.persistence;

import org.junit.Test;
import org.obehave.model.Action;
import org.obehave.model.modifier.ModifierFactory;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class ActionDaoTest extends DatabaseTest {
    @Test
    public void persistingAction() throws SQLException {
        Action a1 = new Action("Jumping");
        a1.setAlias("Ho");
        Action a2 = new Action("Flying", Action.Type.STATE);

        a1.setRecurring(5);
        final ModifierFactory modifierFactory = Daos.get().modifierFactory().queryForName("Slow Or Fast");
        a2.setModifierFactory(modifierFactory);

        Daos.get().action().create(a1);
        Daos.get().action().create(a2);

        assertAction(Daos.get().action().queryForSameId(a1), "Jumping", "Ho", 5, null, Action.Type.POINT);
        assertAction(Daos.get().action().queryForSameId(a2), "Flying", null, 0, modifierFactory, Action.Type.STATE);
    }

    private void assertAction(Action a, String name, String alias, int recurring, ModifierFactory modifierFactory, Action.Type type) {
        assertEquals(name, a.getName());
        assertEquals(alias, a.getAlias());
        assertEquals(recurring, a.getRecurring());
        assertEquals(modifierFactory, a.getModifierFactory());
        assertEquals(type, a.getType());
    }
}
