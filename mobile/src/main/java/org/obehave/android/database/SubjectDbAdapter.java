package org.obehave.android.database;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.obehave.model.Subject;

import java.sql.SQLException;
import java.util.Collection;

public class SubjectDbAdapter extends AbstractDbAdapter<Subject> {

    private Dao<Subject, Long> dao;
    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_NAME = "name";
    private static final String COLUMN_GROUP_ID = "group_id";

    public SubjectDbAdapter(Context context) throws SQLException {
        super(context);
        initDao();
    }

    public SubjectDbAdapter(Context context, MyDatabaseHelper helper) throws SQLException {
        super(context, helper);
        initDao();
    }

    @Override
    protected void initDao() throws SQLException {
        dao = getHelper().getSubjectDao();
    }

    /**
     * This returns an Android Cursor to iterate through the SubjectGroups.
     * This method is necessary for our CursorAdapters.
     * @param groupId
     * @return Cursor
     */

    public Cursor findByGroupId(Long groupId){
        Log.i(LOG_TAG, "findByGroupId");
        Cursor cursor = null;
        CloseableIterator iterator = null;
        try {
            PreparedQuery<Subject> preparedQuery = getFindByGroupIdPreparedQuery(groupId);
            iterator = dao.iterator(preparedQuery);
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            cursor = results.getRawCursor();
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return cursor;
    }

    private PreparedQuery<Subject> getFindByGroupIdPreparedQuery(Long groupId) throws SQLException {
        QueryBuilder<Subject, Long> finderQuery = dao.queryBuilder();
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
            PreparedQuery<Subject> preparedQuery = getFindAllPreparedQuery();
            iterator = dao.iterator(preparedQuery);
            AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
            cursor = results.getRawCursor();
        } catch (SQLException e) {
            Log.e(LOG_TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if(iterator != null){
                iterator.closeQuietly();
            }
        }
        return cursor;
    }

    private PreparedQuery<Subject> getFindAllPreparedQuery() throws SQLException {
        QueryBuilder<Subject, Long> finderQuery = dao.queryBuilder();
        return finderQuery.selectColumns(COLUMN_ID, COLUMN_NAME).prepare();
    }

    @Override
    public Subject findById(Long id) throws SQLException {
        return dao.queryForId(id);
    }

    @Override
    public Collection<Subject> findAllByListOfIds() {
        return null;
    }
}
