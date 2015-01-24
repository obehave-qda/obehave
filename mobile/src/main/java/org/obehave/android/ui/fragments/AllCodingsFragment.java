package org.obehave.android.ui.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.obehave.android.R;

public class AllCodingsFragment extends ListFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_all_codings, container, false);
        String[] values = new String[] { "Amarok - spielt - Peter", "Amarok - markiert",  "Lessie - frisst"};


        setListAdapter(new ArrayAdapter(this.getActivity(),
                R.layout.list_item_running,R.id.liListHeader, values));

        return rootView;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
}
