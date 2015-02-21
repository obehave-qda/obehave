package org.obehave.persistence.modifier;

import com.j256.ormlite.dao.DaoManager;
import org.junit.BeforeClass;
import org.obehave.model.modifier.ModifierFactory;
import org.obehave.persistence.DaoTestBase;
import org.obehave.persistence.ModifierFactoryDao;

import java.sql.SQLException;

/**
 * @author Markus Möslinger
 */
public class ModifierFactoryDaoTest extends DaoTestBase {
    protected static ModifierFactoryDao dao;

    @BeforeClass
    public static void prepare() throws SQLException {
        dao = DaoManager.createDao(connectionSource, ModifierFactory.class);
    }
}
