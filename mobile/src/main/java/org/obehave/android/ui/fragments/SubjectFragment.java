package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.SubjectAdapter;
import org.obehave.android.ui.events.SubjectSelectedEvent;
import org.obehave.android.ui.util.DataHolder;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Subject;

public class SubjectFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListAdapter adapter;

    public static SubjectFragment newInstance(int sectionNumber) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        /* which type of fragment should be loaded */
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_subject, container, false);

        adapter = (SubjectAdapter) new SubjectAdapter(this.getActivity(), DataHolder.getInstance().getAllSubjects());
        setListAdapter(adapter);


        return rootView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Subject subject = (Subject) adapter.getItem(position);
        EventBusHolder.post(new SubjectSelectedEvent(subject));

    }
}
