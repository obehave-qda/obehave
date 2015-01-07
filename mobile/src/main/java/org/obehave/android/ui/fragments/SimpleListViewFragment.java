package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import org.obehave.android.R;

public class SimpleListViewFragment extends BaseFragment {

    private ListView lvGroups;

    public SimpleListViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simple_listview, container, false);

        lvGroups = (ListView) rootView.findViewById(R.id.lvSimple);
        Log.d(LOG_TAG, "loaded!");
        /*
        try {
            subjectAdapter = new SubjectAdapter(getActivity(), getHelper());
        } catch (SQLException e) {
            e.printStackTrace();
        }


        lvGroups.setAdapter(subjectAdapter);
         */

        return rootView;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("fragment", "GroupFragment created!");

        // only on init
        if(savedInstanceState == null){

        }
    }

}
