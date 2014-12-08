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

import java.util.ArrayList;

public class GroupFragment extends Fragment {

    ListView lvGroups;
    private String[] mockedData = {
        "Subject Group 1",
        "Subject Group 2",
        "Subject Group 3",
        "Subject Group 4",
        "Subject Group 5"
    };

    public GroupFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_listview, container, false);

        Log.d("fragment", "GroupFragment loaded!");

        return rootView;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("fragment", "GroupFragment created!");
        if(savedInstanceState == null){
             lvGroups = (ListView) getView().findViewById(R.id.lvGroups);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_textview_for_listview);
        for(int i = 0; i < mockedData.length; i++){
            arrayAdapter.add(mockedData[i]);
        }

        lvGroups.setAdapter(arrayAdapter);

    }

}
