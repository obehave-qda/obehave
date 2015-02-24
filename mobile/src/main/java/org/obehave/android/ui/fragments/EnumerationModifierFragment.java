package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.EnumerationModifierAdapter;
import org.obehave.android.ui.events.EnumerationModifierSelectedEvent;
import org.obehave.events.EventBusHolder;

import java.util.*;

public class EnumerationModifierFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = MyListFragment.class.getSimpleName();

    private ListAdapter adapter;
    private Map<Integer, String> selectedValues;
    private List<String> values;
    private Button acceptButton;

    public static EnumerationModifierFragment newInstance(int sectionNumber, List<String> values) {
        EnumerationModifierFragment fragment = new EnumerationModifierFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setModifierValues(values);
        /* which type of fragment should be loaded */

        return fragment;
    }

    public void setModifierValues(List<String> values){
        this.values = values;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_subject_modifier, container, false);

        selectedValues = new HashMap<Integer, String>();
        acceptButton  = (Button) rootView.findViewById(R.id.accept);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                EventBusHolder.post(new EnumerationModifierSelectedEvent(Collections.unmodifiableList(new ArrayList<String>(selectedValues.values()))));
            }
        });

        adapter = (EnumerationModifierAdapter) new EnumerationModifierAdapter(this.getActivity(), values);
        setListAdapter(adapter);

        return rootView;
    }


    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        String value = (String) getListAdapter().getItem(position);


        CheckBox cb  = (CheckBox) view.findViewById(R.id.lvCheckbox);
        cb.setChecked(!cb.isChecked());

        if(value != null){
            if(selectedValues.containsKey(position)){
                selectedValues.remove(position);
            }
            else {
                selectedValues.put(position, value);
            }

            Log.i(LOG_TAG, "" + value + ", " + selectedValues.size());
        }
    }
}
