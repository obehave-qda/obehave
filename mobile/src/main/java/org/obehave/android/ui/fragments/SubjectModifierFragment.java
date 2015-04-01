package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.SubjectModifierAdapter;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;
import org.obehave.model.Subject;
import org.obehave.model.modifier.ModifierFactory;

import java.util.List;

public class SubjectModifierFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_ACTION = "org.obehave.action";

    private Action action;
    private ListAdapter adapter;
    private List<Subject> subjects;

    public static SubjectModifierFragment newInstance(int sectionNumber, Action action) {
        SubjectModifierFragment fragment = new SubjectModifierFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_ACTION, action);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_subject_modifier, container, false);

        initArgs();
        initListview();

        return rootView;
    }

    private void initArgs() {
        action = (Action) this.getArguments().getSerializable(ARG_ACTION);
    }

    private void initListview() {
        adapter = (SubjectModifierAdapter) new SubjectModifierAdapter(this.getActivity(), action.getModifierFactory().getValidSubjects());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Subject subject = (Subject) getListAdapter().getItem(position);

        EventBusHolder.post(new ModifierFactory(subject));
    }
}
