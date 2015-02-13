package org.obehave.persistence;

import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.obehave.persistence.ormlite.ColorType;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class DaoTestBase {
    protected static ConnectionSource connectionSource;

    @BeforeClass
    public static void setUp() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:h2:mem:obehave;INIT=runscript from 'classpath:sql/create.sql'");
        DataPersisterManager.registerDataPersisters(ColorType.getInstance());
    }

    @AfterClass
    public static void tearDown() {
        connectionSource.closeQuietly();
    }
}
