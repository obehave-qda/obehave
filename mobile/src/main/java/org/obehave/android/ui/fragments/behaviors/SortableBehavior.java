package org.obehave.android.ui.fragments.behaviors;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.obehave.android.R;
import org.obehave.android.ui.fragments.events.SortingChangedEvent;
import org.obehave.events.EventBusHolder;

public class SortableBehavior implements FragmentBehavior{

    public final static String ARG_START_SORT_ORDER = "org.obehave.android.start_sort_order";

    private Activity activity;
    private Fragment fragment;
    private View rootView;

    private Spinner spSort;
    private ArrayAdapter<String> sortAdapter;

    protected String sortValues[] = {
            "Standard",
            "Alphabetisch A-Z",
            "Alphabetisch Z-A"
    };

    @Override
    public void init(Activity activity, Fragment fragment, View rootView, Bundle settings) {
        this.activity = activity;
        this.fragment = fragment;
        this.rootView = rootView;

        initComponent();
        initEventListener();
        loadSettings(settings);
    }

    private void loadSettings(Bundle settings) {
        int defaultValue = settings.getInt(ARG_START_SORT_ORDER);
        spSort.setSelection(defaultValue);
        EventBusHolder.post(defaultValue);
    }

    private void initComponent(){
        spSort = (Spinner) rootView.findViewById(R.id.spSort);
        sortAdapter = new ArrayAdapter<String>(this.activity, android.R.layout.simple_spinner_item, sortValues);
        spSort.setAdapter(sortAdapter);
    }

    private void initEventListener(){
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == SortType.DEFAULT) {
                    EventBusHolder.post(new SortingChangedEvent(SortType.DEFAULT));
                } else if (position == SortType.ALPHABETICAL_ASCENDING) {
                    EventBusHolder.post(new SortingChangedEvent(SortType.ALPHABETICAL_ASCENDING));
                } else if (position == SortType.ALPHABETICAL_DESCENDING) {
                    EventBusHolder.post(new SortingChangedEvent(SortType.ALPHABETICAL_DESCENDING));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
