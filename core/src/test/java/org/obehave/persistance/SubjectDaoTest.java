package org.obehave.persistance;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.spring.DaoFactory;
import com.j256.ormlite.table.TableUtils;
import org.junit.Test;
import org.obehave.model.Subject;

/**
 * Created by Markus.Moeslinger on 04.11.2014.
 */
public class SubjectDaoTest {
    @Test
    public void firstTest() throws Exception {
        Class.forName("org.h2.Driver");
        JdbcConnectionSource cs = new JdbcConnectionSource("jdbc:h2:mem:testdb;");

        TableUtils.createTable(cs, Subject.class);

        Dao<Subject, Long> dao = DaoFactory.createDao(cs, Subject.class);

        Subject s = new Subject();
        s.setName("Hubert");
        System.out.println("Create: " + dao.create(s));

        for (Subject ss : dao.queryForAll()) {
            System.out.println("Found: " + ss.getName());
        }




    }
}
