package org.obehave.persistence.modifier;

import org.junit.BeforeClass;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;
import org.obehave.persistence.DatabaseTest;
import org.obehave.persistence.ModifierFactoryDao;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Markus Möslinger
 */
public class ModifierFactoryDaoTest extends DatabaseTest {
    protected static ModifierFactoryDao dao;

    @BeforeClass
    public static void prepare() throws SQLException {
        dao = Daos.get().modifierFactory();
    }

    protected static void assertModifierFactory(ModifierFactory modifierFactory, ModifierFactory.Type type, String name, int from, int to, String alias, List<String> validValues, List<Subject> validSubjects) {
        assertEquals(name, modifierFactory.getName());
        assertEquals(type, modifierFactory.getType());

        if (modifierFactory.getType() == ModifierFactory.Type.DECIMAL_RANGE_MODIFIER_FACTORY) {
            assertEquals(from, modifierFactory.getFrom());
            assertEquals(to, modifierFactory.getTo());
        } else {
            try {
                modifierFactory.getFrom();
                fail("No exception thrown");
            } catch (IllegalStateException okay) {

            }
            try {
                modifierFactory.getTo();
                fail("No exception thrown");
            } catch (IllegalStateException okay) {

            }
        }

        assertEquals(alias, modifierFactory.getAlias());

        if (modifierFactory.getType() == ModifierFactory.Type.ENUMERATION_MODIFIER_FACTORY) {
            assertEquals(validValues, modifierFactory.getValidValues());
        } else {
            try {
                modifierFactory.getValidValues();
                fail("No exception thrown");
            } catch (IllegalStateException okay) {

            }
        }

        if (modifierFactory.getType() == ModifierFactory.Type.SUBJECT_MODIFIER_FACTORY) {
            assertEquals(validSubjects, modifierFactory.getValidSubjects());
        } else {
            try {
                modifierFactory.getValidSubjects();
                fail("No exception thrown");
            } catch (IllegalStateException okay) {

            }
        }
    }
}
