package org.obehave.android.database;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.obehave.model.ActionGroup;

import java.sql.SQLException;
import java.util.Collection;

public class ActionGroupDbAdapter extends AbstractDbAdapter<ActionGroup> {

    private Dao<ActionGroup, Long> dao;
    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_NAME = "name";

    public ActionGroupDbAdapter(Context context) throws SQLException {
        super(context);
        initDao();
    }

    public ActionGroupDbAdapter(Context context, MyDatabaseHelper helper) throws SQLException {
        super(context, helper);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        dao = getHelper().getActionGroupDao();
    }

    /**
     * This returns an Android Cursor to iterate through the SubjectGroups.
     * This method is necessary for our CursorAdapters.
     * @return Cursor
     */

    public Cursor findAll(){
        Log.i(LOG_TAG, "findAll");
        Cursor cursor = null;
        CloseableIterator iterator = null;
        try {
            PreparedQuery<ActionGroup> preparedQuery = getFindAllPreparedQuery();
            iterator = dao.iterator(preparedQuery);
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            cursor = results.getRawCursor();
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        } catch(Exception e){
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return cursor;
    }

    private PreparedQuery<ActionGroup> getFindAllPreparedQuery() throws SQLException {

        QueryBuilder<ActionGroup, Long> finderQuery = dao.queryBuilder();
        finderQuery.selectColumns(COLUMN_ID, COLUMN_NAME);
        return finderQuery.prepare();
    }

    @Override
    public Cursor findByGroupId(Long groupId) {
        throw new UnsupportedOperationException("The Method findByGroupId is not implemented!");
    }

    @Override
    public ActionGroup findById(Long id) throws SQLException {
        return dao.queryForId(id);
    }

    @Override
    public Collection<ActionGroup> findAllByListOfIds() {
        return null;
    }
}
