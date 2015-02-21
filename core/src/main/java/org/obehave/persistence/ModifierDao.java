package org.obehave.persistence;

import com.j256.ormlite.dao.Dao;
import org.obehave.model.modifier.Modifier;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Markus Möslinger
 */
public interface ModifierDao extends Dao<Modifier, Long> {

    List<Modifier> queryForType(Modifier.Type type) throws SQLException;
}
