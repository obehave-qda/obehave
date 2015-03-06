package org.obehave.util;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.junit.Test;
import org.obehave.persistence.Daos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class TestStudyCreator {
    private static final Logger log = LoggerFactory.getLogger(TestStudyCreator.class);

    private void create(File path) throws SQLException {
        log.info("Creating teststudy at {}", path.getAbsolutePath());

        Daos.asDefault(new JdbcConnectionSource(Properties.getDatabaseConnectionStringWithInit(path)));
        Daos.get().node().executeRaw("runscript from 'classpath:sql/populate.sql'");
        Daos.closeAll();

        log.info("Done creating teststudy at {}", path.getAbsolutePath());
    }

    @Test
    public void createStudy() throws SQLException {
        // We want to create the file @ obehave/studies instead of obehave/core/studies
        final File folder = new File("../studies");
        final String file = "teststudy2.h2.db";

        final File path = new File(folder, file);

        if (path.exists()) {
            log.info("Deleting {} - it already exists", path.getAbsolutePath());
            path.delete();
        }

        create(path);
    }
}
