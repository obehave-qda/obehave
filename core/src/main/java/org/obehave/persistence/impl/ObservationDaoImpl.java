package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.Observation;
import org.obehave.model.SubjectInObservation;
import org.obehave.persistence.Daos;
import org.obehave.persistence.ObservationDao;
import org.obehave.persistence.SubjectInObservationDao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author Markus Möslinger
 */
public class ObservationDaoImpl extends BaseDaoImpl<Observation, Long> implements ObservationDao {
    public ObservationDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Observation.class);
    }

    @Override
    public Observation queryForName(String name) throws SQLException {
        return queryBuilder().where().eq(Observation.COLUMN_NAME, name).queryForFirst();
    }

    @Override
    public int create(Observation data) throws SQLException {
        final int id = super.create(data);
        createParticipatingSubjects(data);
        return id;
    }

    @SuppressWarnings("unchecked")
    private void createParticipatingSubjects(Observation data) throws SQLException {
        SubjectInObservationDao subjectInObservationDao = Daos.get(getConnectionSource()).subjectInObservation();

        try {
            Field participatingSubjects = Observation.class.getDeclaredField("participatingSubjects");
            participatingSubjects.setAccessible(true);

            for (SubjectInObservation subject : (Collection<SubjectInObservation>) participatingSubjects.get(data)) {
                subjectInObservationDao.createOrUpdate(subject);
            }
        } catch (NoSuchFieldException e) {
            throw new SQLException("Couldn't read participating subjects", e);
        } catch (IllegalAccessException e) {
            throw new SQLException("Couldn't read participating subjects", e);
        }

    }
}
