package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import org.obehave.android.R;
import org.obehave.android.database.AbstractDbAdapter;
import org.obehave.android.ui.activities.MainActivity;
import org.obehave.android.ui.adapters.MyCursorTreeAdapter;
import org.obehave.android.ui.events.ListViewItemSelectedEvent;
import org.obehave.events.EventBusHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ActionsFragment extends BaseFragment {

    private ExpandableListView lvExpandable;
    private MainActivity mainActivity;
    private MyCursorTreeAdapter cursorAdapter;

    public ActionsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expandable_listview, container, false);
        lvExpandable = (ExpandableListView) rootView.findViewById(R.id.lvExpandable);

        mainActivity = (MainActivity) getActivity();

        cursorAdapter = new MyCursorTreeAdapter(mainActivity, mainActivity.getActionGroupAdapter(), mainActivity.getActionAdapter());
        lvExpandable.setAdapter(cursorAdapter);

        Log.d(LOG_TAG, "Fragment loaded!");

        lvExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO: Pass the selected value
                EventBusHolder.post(new ListViewItemSelectedEvent<String>(parent, (String) "blubb", "Actions"));
                return true;
            }
        });


        return rootView;

    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("fragment", "ExpandableListview created!");

        // only on init
        if(savedInstanceState == null){

        }
    }
}
