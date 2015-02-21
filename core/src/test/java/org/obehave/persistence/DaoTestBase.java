package org.obehave.persistence;

import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.obehave.persistence.ormlite.ClassType;
import org.obehave.persistence.ormlite.ColorType;
import org.obehave.persistence.ormlite.FileType;
import org.obehave.persistence.ormlite.VersionDateTimeType;
import org.obehave.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class DaoTestBase {
    private static final boolean DEBUG_DATABASE = Property.isDatabaseDebug();

    private static final Logger log = LoggerFactory.getLogger(DaoTestBase.class);

    private static Server tcpServer;
    private static Server webServer;

    private static ConnectionSource connectionSource;

    @BeforeClass
    public static void setUp() throws SQLException {
        if (DEBUG_DATABASE) {
            webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", Property.getDatabaseDebugPortWeb()).start();
            tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", Property.getDatabaseDebugPortTcp()).start();
        }

        connectionSource = new JdbcConnectionSource("jdbc:h2:mem:obehave;INIT=runscript from 'classpath:sql/create.sql'\\;RUNSCRIPT FROM 'classpath:sql/populate.sql'");
        Daos.setConnectionSource(connectionSource);

        if (DEBUG_DATABASE) {
            log.debug("Started Webserver at {}", webServer.getURL());
            log.debug("Connection URL: jdbc:h2:{}/mem:obehave", tcpServer.getURL());
            log.debug("Make sure to use thread-only breakpoints when debugging tests!");
        }

        DataPersisterManager.registerDataPersisters(ColorType.getInstance());
        DataPersisterManager.registerDataPersisters(VersionDateTimeType.getInstance());
        DataPersisterManager.registerDataPersisters(ClassType.getInstance());
        DataPersisterManager.registerDataPersisters(FileType.getInstance());
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        if (DEBUG_DATABASE) {
            webServer.stop();
            tcpServer.stop();
        }
        connectionSource.close();
        Daos.setConnectionSource(null);
    }
}
