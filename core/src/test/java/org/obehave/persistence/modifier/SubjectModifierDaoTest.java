package org.obehave.persistence.modifier;

import org.junit.BeforeClass;
import org.junit.Test;
import org.obehave.model.Subject;
import org.obehave.model.modifier.Modifier;
import org.obehave.persistence.Daos;
import org.obehave.persistence.SubjectDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class SubjectModifierDaoTest extends ModifierDaoTest {
    private static SubjectDao subjectDao;

    @BeforeClass
    public static void prepareSubjectDao() throws SQLException {
        subjectDao = Daos.get().subject();
    }

    @Test
    public void persistingModifier() throws SQLException {
        Subject subject = new Subject("Subject for Modifier Test");
        subjectDao.create(subject);

        Modifier modifier = new Modifier(queryModifierFactory("Subject One Or Two"), subject, "Subject for Modifier Test");

        dao.create(modifier);

        Modifier loadedModifier = dao.queryForSameId(modifier);

        assertModifier(loadedModifier, modifier.getModifierFactory(), modifier.get());
    }

    @Test
    public void loadPersistedModifier() throws SQLException {
        Modifier modifier = dao.queryForType(Modifier.Type.SUBJECT_MODIFIER).get(0);

        assertModifier(modifier, queryModifierFactory("Subject One Or Two"), new Subject("Subject1"));
    }
}
