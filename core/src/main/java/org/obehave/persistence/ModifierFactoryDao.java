package org.obehave.persistence;

import com.j256.ormlite.dao.Dao;
import org.obehave.model.modifier.ModifierFactory;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public interface ModifierFactoryDao extends Dao<ModifierFactory, Long> {
    ModifierFactory queryForName(String name) throws SQLException;
}
