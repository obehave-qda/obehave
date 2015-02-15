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
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.services.ApplicationState;
import org.obehave.android.ui.util.ErrorDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Subject;
import org.obehave.model.modifier.SubjectModifierFactory;

import java.util.*;

public class SubjectModifierFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String LOG_TAG = MyListFragment.class.getSimpleName();

    private ListAdapter adapter;
    private Map<Integer, Subject> selectedSubjects;

    private Button acceptButton;

    public static SubjectModifierFragment newInstance(int sectionNumber) {
        SubjectModifierFragment fragment = new SubjectModifierFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_subject_modifier, container, false);

        selectedSubjects = new HashMap<Integer, Subject>();
        acceptButton  = (Button) rootView.findViewById(R.id.accept);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                EventBusHolder.post(new SubjectModifierSelectedEvent(Collections.unmodifiableList(new ArrayList<Subject>(selectedSubjects.values()))));
            }
        });

        try {
            List<Subject> subjects = loadSubjectsForSelection();
            adapter = (SubjectModifierAdapter) new SubjectModifierAdapter(this.getActivity(), subjects);
            setListAdapter(adapter);
        } catch (UiException e) {
            new ErrorDialog(e, getActivity()).invoke();
            e.printStackTrace();
        }

        return rootView;
    }

    private List<Subject> loadSubjectsForSelection() throws UiException {
        if(ApplicationState.getInstance().getAction() == null){
            // FIXME: Error Message should be in resource file.
            throw new UiException("Es wurde keine Aktion ausgewählt!");
        }

        if(ApplicationState.getInstance().getAction().getModifierFactory() == null){
            // FIXME: Error Message should be in resource file.
            throw new UiException("Es wurde keine ModifierFactory ausgewählt!");
        }

        SubjectModifierFactory subjectModifierFactory = (SubjectModifierFactory) ApplicationState.getInstance().getAction().getModifierFactory();

        return subjectModifierFactory.getValidSubjects();
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
