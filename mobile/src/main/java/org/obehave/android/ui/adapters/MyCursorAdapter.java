package org.obehave.android.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.obehave.android.R;
import org.obehave.android.database.AbstractDbAdapter;

import java.sql.SQLException;

import static android.widget.AdapterView.*;

public class MyCursorAdapter<T> extends CursorAdapter{
    private final AbstractDbAdapter<T> dbAdapter;
    private LayoutInflater layoutInflater;
    private T selectedItem;
    private ListView listView;

    private final String LOG_TAG = getClass().getSimpleName();

    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_NAME = "name";



    private class ViewHolder{
        private Long id;
        private TextView textView;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    public MyCursorAdapter(Context context, AbstractDbAdapter dbAdapter) {
        super(context, dbAdapter.findAll(), 0);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dbAdapter = dbAdapter;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.list_item1, parent, false);
        ViewHolder holder = new ViewHolder();

        holder.setTextView((TextView) v.findViewById(R.id.lblListItem));
        v.setTag(holder);

        if(listView != null){
            listView = (ListView) parent;
            listView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Cursor c = (Cursor)listView.getItemAtPosition(position);
                    try {
                        selectedItem = dbAdapter.findById(getId(c));
                    } catch (SQLException e) {
                        Log.d(LOG_TAG, "Can't find Id");
                        Log.d(LOG_TAG, e.getMessage());
                        Log.d(LOG_TAG, e.getSQLState());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedItem = null;
                }
            });
        }
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.getTextView().setText(getColumnValue(cursor));
    }

    protected String getColumnValue(Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
    }

    protected long getId(Cursor cursor){
        return cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
    }

    public T getSelectedItem() {
        return selectedItem;
    }

}
