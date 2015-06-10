package org.obehave.util;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import org.obehave.persistence.Daos;
import org.obehave.util.properties.AppProperties;
import org.obehave.util.properties.AppPropertiesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.SQLException;

/**
 * Helper class to create a test study
 */
public class TestStudyCreator {
    private static final Logger log = LoggerFactory.getLogger(TestStudyCreator.class);
    private static final AppProperties PROPERTIES = AppPropertiesHolder.get();

    private void create(File path) throws SQLException {
        log.info("Creating teststudy at {}", path.getAbsolutePath());

        final String h2Path = FileUtil.removeSuffixIfThere(path, PROPERTIES.databaseFileSuffix());
        String connectionString = PROPERTIES.databaseConnectionInitString(h2Path);
        Daos.asDefault(new JdbcConnectionSource(connectionString));
        Daos.get().node().executeRaw("runscript from 'classpath:sql/populate.sql'");
        Daos.closeAll();

        log.info("Done creating teststudy at {}", path.getAbsolutePath());
    }

    // if you want to keep your teststudy intact after running unit tests, ignore this test
    // @Ignore
    // @Test
    public void createStudy() throws SQLException {
        // We want to create the file @ obehave/studies instead of obehave/core/studies
        final File folder = new File("../studies");

        // change the filename if you don't want to override stuff!
        final String file = "teststudy.h2.db";

        final File path = new File(folder, file);

        if (path.exists()) {
            log.info("Deleting {} - it already exists", path.getAbsolutePath());
            if (!path.delete()) {
                log.warn("Couldn't delete study at {}", path);
            }
        }

        create(path);
    }
}
