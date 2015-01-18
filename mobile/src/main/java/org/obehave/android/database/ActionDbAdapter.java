package org.obehave.android.database;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.obehave.model.Action;

import java.sql.SQLException;
import java.util.Collection;

public class ActionDbAdapter extends AbstractDbAdapter<Action> {

    private Dao<Action, Long> dao;
    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_NAME = "name";
    private static final String COLUMN_GROUP_ID = "group_id";
    private Long id;

    public ActionDbAdapter(Context context) throws SQLException {
        super(context);
        initDao();
    }

    public ActionDbAdapter(Context context, MyDatabaseHelper helper) throws SQLException {
        super(context, helper);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        dao = getHelper().getActionDao();
    }

    /**
     * This returns an Android Cursor to iterate through Actions, grouped by group_id.
     * This method is necessary for our CursorAdapters.
     * @param groupId
     * @return Cursor
     */

    public Cursor findByGroupId(Long groupId){
        Log.i(LOG_TAG, "findByGroupId");
        Cursor cursor = null;
        CloseableIterator iterator = null;
        try {
            PreparedQuery<Action> preparedQuery = getFindByGroupIdPreparedQuery(groupId);
            iterator = dao.iterator(preparedQuery);
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            cursor = results.getRawCursor();
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return cursor;
    }

    private PreparedQuery<Action> getFindByGroupIdPreparedQuery(Long groupId) throws SQLException {
        QueryBuilder<Action, Long> finderQuery = dao.queryBuilder();
        finderQuery.selectColumns(COLUMN_ID, COLUMN_NAME);
        if(groupId == null){
            finderQuery.where().isNull(COLUMN_GROUP_ID);
        }else {
            finderQuery.where().eq(COLUMN_GROUP_ID, groupId);
        }
        return finderQuery.prepare();
    }

    @Override
    public Cursor findAll() {
        Log.i(LOG_TAG, "findAll");
        Cursor cursor = null;
        CloseableIterator iterator = null;
        try {
            PreparedQuery<Action> preparedQuery = getFindAllPreparedQuery();
            iterator = dao.iterator(preparedQuery);
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            cursor = results.getRawCursor();
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        } catch(Exception e){
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
        }
        return cursor;
    }

    private PreparedQuery<Action> getFindAllPreparedQuery() throws SQLException {
        QueryBuilder<Action, Long> finderQuery = dao.queryBuilder();
        return finderQuery.selectColumns(COLUMN_ID, COLUMN_NAME).prepare();
    }

    @Override
    public Action findById(Long id) throws SQLException {
            return dao.queryForId(id);
    }

    @Override
    public Collection<Action> findAllByListOfIds() {
        return null;
    }

}
