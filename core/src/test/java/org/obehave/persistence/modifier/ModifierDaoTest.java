package org.obehave.persistence.modifier;

import com.j256.ormlite.dao.DaoManager;
import org.junit.BeforeClass;
import org.obehave.model.modifier.Modifier;
import org.obehave.persistence.DaoTestBase;
import org.obehave.persistence.ModifierDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ModifierDaoTest extends DaoTestBase {
    protected static ModifierDao dao;

    @BeforeClass
    public static void prepare() throws SQLException {
        dao = DaoManager.createDao(connectionSource, Modifier.class);
    }
}
