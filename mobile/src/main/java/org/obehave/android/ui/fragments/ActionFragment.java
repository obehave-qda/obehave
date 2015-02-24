package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.ActionAdapter;
import org.obehave.android.ui.events.ActionSelectedEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;

import java.util.List;

public class ActionFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListAdapter adapter;
    private List<Action> actions;

    public static ActionFragment newInstance(int sectionNumber, List<Action> actions) {
        ActionFragment fragment = new ActionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setActions(actions);
        /* which type of fragment should be loaded */
        return fragment;
    }

    public void setActions(List<Action> actions){
        this.actions = actions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_subject, container, false);

        adapter = (ActionAdapter) new ActionAdapter(this.getActivity(), actions);
        setListAdapter(adapter);

        return rootView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Action action = (Action) adapter.getItem(position);
        EventBusHolder.post(new ActionSelectedEvent(action));
    }
}
