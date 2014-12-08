package org.obehave.android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.obehave.android.R;

public class SimpleListViewFragment extends Fragment {

    private ListView lvGroups;
    private ArrayAdapter<String> arrayAdapter;

    private String[] mockedData = {
        "Subject Group 1",
        "Subject Group 2",
        "Subject Group 3",
        "Subject Group 4",
        "Subject Group 5"
    };

    public SimpleListViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_simple_listview, container, false);

        lvGroups = (ListView) rootView.findViewById(R.id.lvSimple);
        Log.d("fragment", "GroupFragment loaded!");

        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_textview_for_listview, mockedData);

        lvGroups.setAdapter(arrayAdapter);

        return rootView;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("fragment", "GroupFragment created!");

        // only on init
        if(savedInstanceState == null){

        }
        /*


        lvGroups.setAdapter(arrayAdapter);
        */

    }

}
