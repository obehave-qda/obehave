package org.obehave.persistence.modifier;

import org.junit.Test;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;
import org.obehave.persistence.SubjectDao;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Markus Möslinger
 */
public class SubjectModifierFactoryDaoTest extends ModifierFactoryDaoTest {
    @Test
    public void persistingModifierFactory() throws SQLException {
        SubjectDao subjectDao = Daos.get().subject();

        Subject s1 = new Subject("Hans");
        Subject s2 = new Subject("Kurt");
        subjectDao.create(s1);
        subjectDao.create(s2);

        ModifierFactory mf = new ModifierFactory(s1, s2);
        dao.create(mf);

        ModifierFactory loadedModifierFactory = dao.queryForSameId(mf);

        assertModifierFactory(loadedModifierFactory, mf.getType(), mf.getName(), 0, 0, mf.getAlias(), null, Arrays.asList(s1, s2));

        Subject s3 = new Subject("Peter");
        subjectDao.create(s3);

        loadedModifierFactory.addValidSubjects(s3);
        dao.update(loadedModifierFactory);
        dao.refresh(loadedModifierFactory);
        assertModifierFactory(loadedModifierFactory, mf.getType(), mf.getName(), 0, 0, mf.getAlias(), null, Arrays.asList(s1, s2, s3));
    }

    @Test
    public void loadingPersistedModifierFactory() throws SQLException {
        Subject s1 = new Subject("Subject1");
        Subject s2 = new Subject("Subject2");

        ModifierFactory loadedModifierFactory = dao.queryForName("Subject One Or Two");

        assertModifierFactory(loadedModifierFactory, ModifierFactory.Type.SUBJECT_MODIFIER_FACTORY, "Subject One Or Two",
                0, 0, null, null, Arrays.asList(s1, s2));
    }
}
