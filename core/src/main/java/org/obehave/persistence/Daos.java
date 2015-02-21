package org.obehave.persistence;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.*;
import org.obehave.model.modifier.EnumerationItem;
import org.obehave.model.modifier.Modifier;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.model.modifier.ValidSubject;

import java.sql.SQLException;

/**
 * Utility class for retrieving DAOs. Not sure if thread safe. Probably not.
 */
public class Daos {
    private static ConnectionSource connectionSource;

    private static ActionDao actionDao;
    private static CodingDao codingDao;
    private static ModifierDao modifierDao;
    private static ModifierFactoryDao modifierFactoryDao;
    private static NodeDao nodeDao;
    private static ObservationDao observationDao;
    private static SubjectDao subjectDao;
    private static EnumerationItemDao enumerationItemDao;
    private static ValidSubjectDao validSubjectDao;

    private Daos() {
        throw new AssertionError("Utility class");
    }

    public static void setConnectionSource(ConnectionSource connectionSource) throws SQLException {
        if (Daos.connectionSource == null || !Daos.connectionSource.equals(connectionSource)) {
            Daos.connectionSource = connectionSource;

            actionDao = null;
            codingDao = null;
            modifierDao = null;
            modifierFactoryDao = null;
            nodeDao = null;
            observationDao = null;
            subjectDao = null;
            enumerationItemDao = null;
            validSubjectDao = null;
        }
    }

    public static ActionDao action() throws SQLException {
        if (actionDao == null) {
            actionDao = DaoManager.createDao(connectionSource, Action.class);
        }

        return actionDao;
    }

    public static CodingDao coding() throws SQLException {
        if (codingDao == null) {
            codingDao = DaoManager.createDao(connectionSource, Coding.class);
        }

        return codingDao;
    }

    public static ModifierDao modifier() throws SQLException {
        if (modifierDao == null) {
            modifierDao = DaoManager.createDao(connectionSource, Modifier.class);
        }

        return modifierDao;
    }

    public static ModifierFactoryDao modifierFactory() throws SQLException {
        if (modifierFactoryDao == null) {
            modifierFactoryDao = DaoManager.createDao(connectionSource, ModifierFactory.class);
        }

        return modifierFactoryDao;
    }

    public static NodeDao node() throws SQLException {
        if (nodeDao == null) {
            nodeDao = DaoManager.createDao(connectionSource, Node.class);
        }

        return nodeDao;
    }

    public static ObservationDao observation() throws SQLException {
        if (observationDao == null) {
            observationDao = DaoManager.createDao(connectionSource, Observation.class);
        }

        return observationDao;
    }

    public static SubjectDao subject() throws SQLException {
        if (subjectDao == null) {
            subjectDao = DaoManager.createDao(connectionSource, Subject.class);
        }

        return subjectDao;
    }

    public static EnumerationItemDao enumerationItem() throws SQLException {
        if (enumerationItemDao == null) {
            enumerationItemDao = DaoManager.createDao(connectionSource, EnumerationItem.class);
        }

        return enumerationItemDao;
    }

    public static ValidSubjectDao validSubject() throws SQLException {
        if (validSubjectDao == null) {
            validSubjectDao = DaoManager.createDao(connectionSource, ValidSubject.class);
        }

        return validSubjectDao;
    }
}
