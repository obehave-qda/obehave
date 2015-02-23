package org.obehave.persistence;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.obehave.model.Color;
import org.obehave.model.Subject;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * @author Markus Möslinger
 */
public class SubjectDaoTest extends DatabaseTest {
    private static SubjectDao dao;
    private Subject subject;

    @BeforeClass
    public static void prepare() throws SQLException {
        dao = Daos.get().subject();
    }

    @Before
    public void prepareSubject() {
        subject = new Subject("Name1");
        subject.setColor(new Color(255, 240, 230, 220));
    }

    @Test
    public void readAndWrite() throws SQLException {
        dao.create(subject);

        Subject loadedSubject = dao.queryForSameId(subject);
        assertEquals("Name1", loadedSubject.getName());

        subject.setName("Name2");
        subject.setColor(new Color(200, 190, 0));
        dao.update(subject);

        loadedSubject = dao.queryForSameId(subject);
        assertEquals("Name2", loadedSubject.getName());
    }
}
