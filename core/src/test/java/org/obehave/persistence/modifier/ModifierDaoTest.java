package org.obehave.persistence.modifier;

import org.junit.BeforeClass;
import org.obehave.model.modifier.Modifier;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;
import org.obehave.persistence.DatabaseTest;
import org.obehave.persistence.ModifierDao;
import org.obehave.persistence.ModifierFactoryDao;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class ModifierDaoTest extends DatabaseTest {
    protected static ModifierDao dao;

    private static ModifierFactoryDao modifierFactoryDao;

    @BeforeClass
    public static void prepare() throws SQLException {
        dao = Daos.get().modifier();
        modifierFactoryDao = Daos.get().modifierFactory();
    }

    protected static void assertModifier(Modifier modifier, ModifierFactory modifierFactory, Object object) {
        assertEquals(modifierFactory, modifier.getModifierFactory());
        assertEquals(object, modifier.get());
    }

    protected ModifierFactory queryModifierFactory(String name) throws SQLException {
        return modifierFactoryDao.queryForName(name);
    }
}
