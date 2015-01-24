package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.obehave.android.R;

public class RunningCodingsFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static RunningCodingsFragment newInstance(int sectionNumber) {
        RunningCodingsFragment fragment = new RunningCodingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_running_codings, container, false);
        //String[] values = new String[] { "Amarok - spielt - Peter", "Amarok - markiert",  "Lessie - frisst"};

/*
        setListAdapter(new ArrayAdapter(this.getActivity(),
                R.layout.list_item_running,R.id.liListHeader, values));
*/
        return rootView;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
}
