package org.obehave.persistence;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.exceptions.Validate;
import org.obehave.model.*;
import org.obehave.model.modifier.EnumerationItem;
import org.obehave.model.modifier.Modifier;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.model.modifier.ValidSubject;
import org.obehave.persistence.ormlite.ClassType;
import org.obehave.persistence.ormlite.ColorType;
import org.obehave.persistence.ormlite.FileType;
import org.obehave.persistence.ormlite.VersionDateTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for retrieving DAOs. Not sure if thread safe. Probably not.
 * But who cares, threads are evil.
 */
public class Daos {
    private static final Logger log = LoggerFactory.getLogger(Daos.class);

    private static Map<ConnectionSource, Daos> daos = new HashMap<>();
    private static Daos defaultInstance;

    private ConnectionSource connectionSource;

    private ActionDao actionDao;
    private CodingDao codingDao;
    private ModifierDao modifierDao;
    private ModifierFactoryDao modifierFactoryDao;
    private NodeDao nodeDao;
    private ObservationDao observationDao;
    private SubjectDao subjectDao;
    private EnumerationItemDao enumerationItemDao;
    private ValidSubjectDao validSubjectDao;
    private PropertyDao propertyDao;
    private SubjectInObservationDao subjectInObservationDao;

    private Daos(ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    static {
        log.debug("Register custom data persisters");
        DataPersisterManager.registerDataPersisters(ColorType.getInstance());
        DataPersisterManager.registerDataPersisters(VersionDateTimeType.getInstance());
        DataPersisterManager.registerDataPersisters(ClassType.getInstance());
        DataPersisterManager.registerDataPersisters(FileType.getInstance());
    }

    /**
     * Retrieves the default instance of this class, previously set via
     * {@link org.obehave.persistence.Daos#asDefault(com.j256.ormlite.support.ConnectionSource)} or
     * {@link org.obehave.persistence.Daos#asDefault()}
     *
     * @return the default instance, if previously set
     * @throws java.lang.IllegalStateException if no default instance was previously set
     */
    public static Daos get() {
        if (defaultInstance == null) {
            throw new IllegalStateException("No default instance set!");
        }

        return defaultInstance;
    }

    /**
     * Retrieves the cached instance configured with {@code connectionSource}, or a new one if there wasn't any before
     *
     * @param connectionSource the connectionSource to get the intance of {@code Daos} with
     * @return a new or already existing instance of {@code Daos}
     */
    public static Daos get(ConnectionSource connectionSource) {
        Validate.isNotNull(connectionSource, "ConnectionSource");

        Daos instance = daos.get(connectionSource);
        if (instance == null) {
            instance = new Daos(connectionSource);
            daos.put(connectionSource, instance);
        }

        return instance;
    }

    /**
     * Retrieves the matching instance for {@code connectionSource} and sets it as default, if there was none before.
     *
     * @param connectionSource the connectionsource to set as default
     * @throws java.lang.IllegalStateException if there was already another default
     */
    public static Daos asDefault(ConnectionSource connectionSource) {
        assertIfDefaultHasOpenConnection(connectionSource);

        defaultInstance = get(connectionSource);
        return defaultInstance;
    }

    /**
     * Sets this instance as a default for further calls to {@link org.obehave.persistence.Daos#get()}
     *
     * @return this
     * @throws java.lang.IllegalStateException if there was already another default
     */
    public Daos asDefault() {
        assertIfDefaultHasOpenConnection(connectionSource);

        defaultInstance = this;
        return this;
    }

    private static void assertIfDefaultHasOpenConnection(ConnectionSource cs) {
        if (defaultInstance != null && !defaultInstance.connectionSource.equals(cs)
                && defaultInstance.connectionSource.isOpen()) {
            throw new IllegalStateException("Default instance already set!");
        }
    }

    public ConnectionSource getConnectionSource() {
        return connectionSource;
    }

    /**
     * Checks if there is already a default instance set. If so, calls to
     *
     * @return true, if a default instance was already set
     * @{@link org.obehave.persistence.Daos#get()} will work,
     * while calls to {@link org.obehave.persistence.Daos#asDefault()} or
     * {@link Daos#asDefault(com.j256.ormlite.support.ConnectionSource)} won't.
     */
    public static boolean hasDefault() {
        return defaultInstance != null;
    }

    public void close() throws SQLException {
        if (connectionSource instanceof JdbcConnectionSource) {
            log.trace("Closing connection {}", ((JdbcConnectionSource) connectionSource).getUrl());
        } else {
            log.trace("Closing connection {}", connectionSource);
        }

        connectionSource.close();
        daos.remove(connectionSource);
    }

    /**
     * Closing all registered ConnectionSources. In case of an exception, the other connections will be closed
     * and the last exception will be thrown afterwards as a runtime exception
     *
     * @throws SQLException the last {@link java.sql.SQLException} that was thrown
     */
    public static void closeAll() {
        log.debug("Closing all connections");
        SQLException lastException = null;

        // avoiding ConcurrentModificationExceptions
        for (Daos daoToClose : new ArrayList<>(daos.values())) {
            try {
                daoToClose.close();
            } catch (SQLException e) {
                lastException = e;
                log.error("Error while closing connection", e);
            }
        }

        if (lastException != null) {
            throw new RuntimeException(lastException);
        }
    }

    public ActionDao action() throws SQLException {
        if (actionDao == null) {
            actionDao = DaoManager.createDao(connectionSource, Action.class);
        }

        return actionDao;
    }

    public CodingDao coding() throws SQLException {
        if (codingDao == null) {
            codingDao = DaoManager.createDao(connectionSource, Coding.class);
        }

        return codingDao;
    }

    public ModifierDao modifier() throws SQLException {
        if (modifierDao == null) {
            modifierDao = DaoManager.createDao(connectionSource, Modifier.class);
        }

        return modifierDao;
    }

    public ModifierFactoryDao modifierFactory() throws SQLException {
        if (modifierFactoryDao == null) {
            modifierFactoryDao = DaoManager.createDao(connectionSource, ModifierFactory.class);
        }

        return modifierFactoryDao;
    }

    public NodeDao node() throws SQLException {
        if (nodeDao == null) {
            nodeDao = DaoManager.createDao(connectionSource, Node.class);
        }

        return nodeDao;
    }

    public ObservationDao observation() throws SQLException {
        if (observationDao == null) {
            observationDao = DaoManager.createDao(connectionSource, Observation.class);
        }

        return observationDao;
    }

    public SubjectDao subject() throws SQLException {
        if (subjectDao == null) {
            subjectDao = DaoManager.createDao(connectionSource, Subject.class);
        }

        return subjectDao;
    }

    public EnumerationItemDao enumerationItem() throws SQLException {
        if (enumerationItemDao == null) {
            enumerationItemDao = DaoManager.createDao(connectionSource, EnumerationItem.class);
        }

        return enumerationItemDao;
    }

    public ValidSubjectDao validSubject() throws SQLException {
        if (validSubjectDao == null) {
            validSubjectDao = DaoManager.createDao(connectionSource, ValidSubject.class);
        }

        return validSubjectDao;
    }

    public PropertyDao property() throws SQLException {
        if (propertyDao == null) {
            propertyDao = DaoManager.createDao(connectionSource, PropertyDto.class);
        }

        return propertyDao;
    }

    public SubjectInObservationDao subjectInObservation() throws SQLException {
        if (subjectInObservationDao == null) {
            subjectInObservationDao = DaoManager.createDao(connectionSource, SubjectInObservation.class);
        }

        return subjectInObservationDao;
    }
}
