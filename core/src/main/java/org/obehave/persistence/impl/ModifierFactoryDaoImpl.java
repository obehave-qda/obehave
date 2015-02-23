package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.modifier.EnumerationItem;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.model.modifier.ValidSubject;
import org.obehave.persistence.Daos;
import org.obehave.persistence.EnumerationItemDao;
import org.obehave.persistence.ModifierFactoryDao;
import org.obehave.persistence.ValidSubjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author Markus Möslinger
 */
public class ModifierFactoryDaoImpl extends BaseDaoImpl<ModifierFactory, Long> implements ModifierFactoryDao {
    private static final Logger log = LoggerFactory.getLogger(ModifierFactoryDaoImpl.class);

    public ModifierFactoryDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, ModifierFactory.class);
    }

    @Override
    public ModifierFactory queryForName(String name) throws SQLException {
        return queryBuilder().where().eq(ModifierFactory.COLUMN_NAME, name).queryForFirst();
    }

    @Override
    public int create(ModifierFactory data) throws SQLException {
        final int id = super.create(data);
        createCollections(data);
        return id;
    }

    @SuppressWarnings("unchecked")
    private void createCollections(ModifierFactory modifierFactory) throws SQLException {
        try {
            switch (modifierFactory.getType()) {
                case ENUMERATION_MODIFIER_FACTORY:
                    log.debug("Creating collection of valid enumerations for {}", modifierFactory);
                    Field validValues = ModifierFactory.class.getDeclaredField("validValues");
                    validValues.setAccessible(true);


                    createValidEnumerationValues((Collection<EnumerationItem>) validValues.get(modifierFactory));
                    break;
                case SUBJECT_MODIFIER_FACTORY:
                    log.debug("Creating collection of valid subjects for {}", modifierFactory);
                    Field validSubjects = ModifierFactory.class.getDeclaredField("validSubjects");
                    validSubjects.setAccessible(true);

                    createValidSubjectValues((Collection<ValidSubject>) validSubjects.get(modifierFactory));
                    break;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new SQLException("Couldn't retrieve fields via reflection", e);
        }
    }

    private void createValidEnumerationValues(Collection<EnumerationItem> enumerationItems) throws SQLException {
        EnumerationItemDao enumerationItemDao = Daos.get(getConnectionSource()).enumerationItem();

        for (EnumerationItem enumerationItem : enumerationItems) {
            enumerationItemDao.create(enumerationItem);
            log.trace("Created {}", enumerationItem);
        }
    }

    private void createValidSubjectValues(Collection<ValidSubject> validSubjects) throws SQLException {
        ValidSubjectDao validSubjectDao = Daos.get(getConnectionSource()).validSubject();

        for (ValidSubject validSubject : validSubjects) {
            validSubjectDao.create(validSubject);
            log.trace("Created {}", validSubject);
        }
    }
}
