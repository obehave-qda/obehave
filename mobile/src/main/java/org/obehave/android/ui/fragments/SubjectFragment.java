package org.obehave.android.ui.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import org.obehave.android.R;
import org.obehave.android.ui.activities.MainActivity;
import org.obehave.android.ui.adapters.MyCursorTreeAdapter;
import org.obehave.android.ui.events.ListViewItemSelectedEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.SubjectGroup;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


public class SubjectFragment extends BaseFragment {

    private ExpandableListView lvExpandable;

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private MyCursorTreeAdapter cursorAdapter;
    private MainActivity mainActivity;

    public SubjectFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expandable_listview, container, false);

        lvExpandable = (ExpandableListView) rootView.findViewById(R.id.lvExpandable);
        mainActivity = (MainActivity) getActivity();

        cursorAdapter = new MyCursorTreeAdapter(mainActivity, mainActivity.getSubjectGroupAdapter(), mainActivity.getSubjectAdapter());
        lvExpandable.setAdapter(cursorAdapter);

        Log.d(LOG_TAG, "Fragment loaded!");

        lvExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO: Pass the selected value
                EventBusHolder.post(new ListViewItemSelectedEvent<String>(parent, (String) "blubb", "Subject"));
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // only on init
        if(savedInstanceState == null){

        }
    }

}
