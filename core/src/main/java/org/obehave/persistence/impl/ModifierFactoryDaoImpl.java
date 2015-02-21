package org.obehave.persistence.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import org.obehave.model.modifier.EnumerationItem;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.Daos;
import org.obehave.persistence.EnumerationItemDao;
import org.obehave.persistence.ModifierFactoryDao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author Markus Möslinger
 */
public class ModifierFactoryDaoImpl extends BaseDaoImpl<ModifierFactory, Long> implements ModifierFactoryDao {
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
        Daos.setConnectionSource(connectionSource);

        try {
            if (modifierFactory.getType() == ModifierFactory.Type.ENUMERATION_MODIFIER_FACTORY) {
                Field validValues = ModifierFactory.class.getDeclaredField("validValues");
                validValues.setAccessible(true);


                createValidEnumerationValues((Collection<EnumerationItem>) validValues.get(modifierFactory));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new SQLException("Couldn't retrieve fields via reflection", e);
        }
    }

    private void createValidEnumerationValues(Collection<EnumerationItem> list) throws SQLException {
        EnumerationItemDao enumerationItemDao = Daos.enumerationItem();

        for (EnumerationItem item : list) {
            enumerationItemDao.create(item);
        }
    }
}
