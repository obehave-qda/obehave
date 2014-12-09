package org.obehave.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import org.obehave.android.R;
import org.obehave.android.adapters.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CodingFragment extends Fragment {

    public CodingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

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

    public static CodingFragment newInstance() {

        CodingFragment f = new CodingFragment();
        Bundle b = new Bundle();
        f.setArguments(b);

        return f;
    }
}
