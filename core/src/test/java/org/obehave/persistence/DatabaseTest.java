package org.obehave.persistence;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.obehave.util.properties.AppProperties;
import org.obehave.util.properties.AppPropertiesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class DatabaseTest {
    private static final AppProperties PROPERTIES = AppPropertiesHolder.get();
    private static final boolean DEBUG_DATABASE = PROPERTIES.isDatabaseDebug();

    private static final Logger log = LoggerFactory.getLogger(DatabaseTest.class);

    private static Server tcpServer;
    private static Server webServer;

    private static ConnectionSource connectionSource;

    @BeforeClass
    public static void setUp() throws SQLException {
        if (DEBUG_DATABASE) {
            webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", PROPERTIES.databaseDebugPortWeb()).start();
            tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", PROPERTIES.databaseDebugPortTcp()).start();
        }

        connectionSource = new JdbcConnectionSource("jdbc:h2:mem:obehave;INIT=runscript from 'classpath:sql/create.sql'\\;RUNSCRIPT FROM 'classpath:sql/populate.sql'");
        Daos.asDefault(connectionSource);

        if (DEBUG_DATABASE) {
            log.debug("Started Webserver at {}", webServer.getURL());
            log.debug("Connection URL: jdbc:h2:{}/mem:obehave", tcpServer.getURL());
            log.debug("Make sure to use thread-only breakpoints when debugging tests!");
        }
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        if (DEBUG_DATABASE) {
            webServer.stop();
            tcpServer.stop();
        }
        connectionSource.close();
        Daos.get().close();
    }
}
