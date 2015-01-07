package org.obehave.android.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorTreeAdapter;
import org.obehave.android.R;
import org.obehave.android.database.AbstractDbAdapter;
public class MyCursorTreeAdapter extends SimpleCursorTreeAdapter {

    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_NAME = "name";
    private AbstractDbAdapter itemDbAdapter;

    public MyCursorTreeAdapter(Context context, AbstractDbAdapter groupDbAdapter, AbstractDbAdapter itemDbAdapter) {
        super(context, groupDbAdapter.findAll(), R.layout.list_group, new String[]{COLUMN_NAME}, new int[] {R.id.lblListHeader}, R.layout.list_item1, new String[]{COLUMN_NAME}, new int[] {R.id.lblListItem});
        this.itemDbAdapter = itemDbAdapter;

    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        long id = groupCursor.getLong(groupCursor.getColumnIndex(COLUMN_ID));
        if(id != -1l) {
            return itemDbAdapter.findByGroupId(id);
        }

        return itemDbAdapter.findByGroupId(null);
    }

}