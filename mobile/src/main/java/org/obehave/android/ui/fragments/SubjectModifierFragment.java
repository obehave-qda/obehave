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
import org.obehave.android.ui.adapters.SubjectModifierAdapter;
import org.obehave.android.ui.events.SubjectModifierSelectedEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Subject;

import java.util.*;

public class SubjectModifierFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = MyListFragment.class.getSimpleName();

    private ListAdapter adapter;
    private Map<Integer, Subject> selectedSubjects;
    private List<Subject> subjects;
    private Button acceptButton;

    public static SubjectModifierFragment newInstance(int sectionNumber, List<Subject> subjects) {
        SubjectModifierFragment fragment = new SubjectModifierFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setSubjects(subjects);
        /* which type of fragment should be loaded */

        return fragment;
    }

    public void setSubjects(List<Subject> subjects){
        this.subjects = subjects;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_subject_modifier, container, false);

        selectedSubjects = new HashMap<Integer, Subject>();
        acceptButton  = (Button) rootView.findViewById(R.id.accept);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                EventBusHolder.post(new SubjectModifierSelectedEvent(Collections.unmodifiableList(new ArrayList<Subject>(selectedSubjects.values()))));
            }
        });

        adapter = (SubjectModifierAdapter) new SubjectModifierAdapter(this.getActivity(), subjects);
        setListAdapter(adapter);

        return rootView;
    }


    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Subject subject = (Subject) getListAdapter().getItem(position);


        CheckBox cb  = (CheckBox) view.findViewById(R.id.lvCheckbox);
        cb.setChecked(!cb.isChecked());

        if(subject != null){
            if(selectedSubjects.containsKey(position)){
                selectedSubjects.remove(position);
            }
            else {
                selectedSubjects.put(position, subject);
            }

            Log.i(LOG_TAG, "" + subject.getName() + ", " + selectedSubjects.size());
        }
    }
}
