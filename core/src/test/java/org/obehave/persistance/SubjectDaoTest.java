package org.obehave.persistance;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.table.TableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.obehave.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by Markus.Moeslinger on 04.11.2014.
 */
public class SubjectDaoTest {
    private static final Logger log = LoggerFactory.getLogger(SubjectDaoTest.class);

    private JdbcConnectionSource cs;
    private Dao<Subject, Long> dao;
    private Subject testSubject;

    @Before
    public void prepare() throws Exception {
        Class.forName("org.h2.Driver");
        cs = new JdbcConnectionSource("jdbc:h2:mem:testdb;");

        TableUtils.createTable(cs, Subject.class);
        dao = DaoFactory.createDao(cs, Subject.class);

        testSubject = new Subject();
        testSubject.setName("Test");
    }

    @After
    public void cleanUp() throws Exception {
        cs.close();
    }

    @Test
    public void testLocalDateTime() throws Exception {
        LocalDateTime creation = LocalDate.now().atStartOfDay();
        LocalDateTime modified = creation.plusHours(1);

        log.info("Created timestamps.\ncreationTS: {}\nmodifiedTS: {}", creation, modified);

        testSubject.setCreationTS(creation);
        testSubject.setModifiedTS(modified);

        log.info("Saving subject {}", testSubject);
        int id = dao.create(testSubject);
        log.info("Saved subject has id {}", id);

        Subject loadedSubject = dao.queryForId(Long.valueOf(id));
        log.info("Loaded subject: {}", loadedSubject);

        log.info("Checking equalness of timestamps");
        assertThat(loadedSubject.getCreationTS(), is(equalTo(creation)));
        assertThat(loadedSubject.getModifiedTS(), is(equalTo(modified)));
    }
}
