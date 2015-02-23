package org.obehave.persistence;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.*;
import org.obehave.model.modifier.Modifier;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Markus.Moeslinger on 17.02.2015.
 */
public class CodingDaoTest extends DatabaseTest {
    private static CodingDao dao;
    private static SubjectDao subjectDao;
    private Coding coding;
    private Subject subject;
    private Action action;

    @BeforeClass
    public static void prepare() throws SQLException {
        dao = Daos.get().coding();
        subjectDao = Daos.get().subject();
    }

    @Before
    public void prepareData() {
        subject = new Subject("Name1");
        subject.setColor(new Color(255, 240, 230, 220));

        action = new Action("Playing");

        coding = new Coding(subject, action, 10);
    }

    @Test
    public void readAndWrite() throws SQLException {
        subjectDao.create(subject);
        dao.create(coding);

        Coding loadedCoding = dao.queryForSameId(coding);
        assertEquals(subject, loadedCoding.getSubject());
        assertEquals(action, loadedCoding.getAction());
    }

    @Test
    public void persistingEnumerationModifiedCoding() throws SQLException, FactoryException {
        createCodingWithLoadedActionAndInput("Running", "Slow", true);
    }

    @Test
    public void persistingSubjectModifiedCoding() throws SQLException, FactoryException {
        createCodingWithLoadedActionAndInput("Looking", "Subject1", false);
    }

    @Test
    public void persistingNumberModifiedCoding() throws SQLException, FactoryException {
        createCodingWithLoadedActionAndInput("Crouching", "3", true);
    }

    private void createCodingWithLoadedActionAndInput(String actionName, String modifierInput, boolean state) throws SQLException, FactoryException {
        Subject s1 = Daos.get().subject().queryForName("Subject1");
        Action a1 = Daos.get().action().queryForName(actionName);
        Observation o1 = Daos.get().observation().queryForName("Observation1");

        Coding c;
        if (!state) {
            c = new Coding(s1, a1, modifierInput, 100);
        } else {
            c = new Coding(s1, a1, modifierInput, 100, 200);
        }

        o1.addCoding(c);

        Daos.get().coding().create(c);
        assertCoding(Daos.get().coding().queryForSameId(c), c.getSubject(), c.getAction(), c.getModifier(), c.getObservation(), c.getStartMs(), c.getEndMs());
    }

    private void assertCoding(Coding c, Subject subject, Action action, Modifier modifier, Observation observation, long start, long end) {
        assertEquals(subject, c.getSubject());
        assertEquals(action, c.getAction());
        assertEquals(modifier, c.getModifier());
        assertEquals(observation, c.getObservation());
        assertEquals(start, c.getStartMs());
        assertEquals(end, c.getEndMs());
    }
}
