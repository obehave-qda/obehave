package org.obehave.persistence;

import com.google.common.reflect.Reflection;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.obehave.persistence.impl.ActionDaoImpl;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DaosTest {
    private ConnectionSource connectionSource;
    private ConnectionSource secondConnectionSource;

    @Before
    public void prepare() throws SQLException {
        connectionSource = new JdbcConnectionSource("jdbc:h2:mem:firstconnection'");
        secondConnectionSource = new JdbcConnectionSource("jdbc:h2:mem:secondconnection'");

        // make sure that custom persisters are registered
        Reflection.initialize(Daos.class);

        // hack to open connections
        new ActionDaoImpl(connectionSource).queryRaw("select 1 from dual");
        new ActionDaoImpl(secondConnectionSource).queryRaw("select 1 from dual");
    }

    @After
    public void cleanup() throws SQLException {
        Daos.closeAll();
    }

    @Test
    public void getCustomConnection() {
        assertEquals(connectionSource, Daos.get(connectionSource).getConnectionSource());
    }

    @Test
    public void closeConnection() throws SQLException {
        Daos.get(connectionSource).close();
        assertFalse(connectionSource.isOpen());
    }

    @Test
    public void setDefaultConnection() {
        Daos.asDefault(connectionSource);
        assertEquals(connectionSource, Daos.get().getConnectionSource());
    }

    @Test
    public void getCustomConnectionAsDefault() {
        Daos.get(connectionSource).asDefault();
        assertEquals(connectionSource, Daos.get().getConnectionSource());
    }

    @Test(expected = IllegalStateException.class)
    public void cannotSetDefaultConnectionTwiceWithDifferentOpenConnections() {
        Daos.asDefault(connectionSource);
        Daos.asDefault(secondConnectionSource);
    }

    @Test
    public void setDefaultConnectionAfterPreviousDefaultConnectionWasClosed() throws SQLException {
        Daos.asDefault(connectionSource).close();
        Daos.asDefault(secondConnectionSource);
    }

    @Test
    public void setDefaultConnectionTwice() {
        Daos.asDefault(connectionSource);
        Daos.asDefault(connectionSource);
        assertEquals(connectionSource, Daos.get().getConnectionSource());
    }

    @Test
    public void setCustomConnectionAsDefaultIfThePreviousOneIsAlreadyClosed() throws SQLException {
        Daos.asDefault(connectionSource).close();
        Daos.get(secondConnectionSource).asDefault();
    }

    @Test
    public void closeAll() throws SQLException {
        assertTrue(Daos.get(connectionSource).getConnectionSource().isOpen());
        assertTrue(Daos.get(secondConnectionSource).getConnectionSource().isOpen());

        Daos.closeAll();

        assertFalse(connectionSource.isOpen());
        assertFalse(secondConnectionSource.isOpen());
    }
}
