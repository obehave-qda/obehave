package org.obehave.android.database;

import android.content.Context;
import android.database.Cursor;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;

public abstract class AbstractDbAdapter<T> {
    private Context context;
    private MyDatabaseHelper helper;

    protected final String LOG_TAG = getClass().getSimpleName();

    public AbstractDbAdapter(Context context){
        this.setContext(context);
    }

    public AbstractDbAdapter(Context context, MyDatabaseHelper helper){
        this.setContext(context);
        this.setHelper(helper);
    }

    public MyDatabaseHelper getHelper() {
        return helper;
    }

    public void setHelper(MyDatabaseHelper helper) {
        this.helper = helper;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected abstract void initDao() throws SQLException;

    public abstract Cursor findByGroupId(Long groupId);

    public abstract Cursor findAll();

    public abstract T findById(Long id) throws SQLException;

    public abstract Collection<T> findAllByListOfIds();
}
