package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.SubjectModifierAdapter;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.util.AppState;
import org.obehave.android.ui.util.ErrorDialog;
import org.obehave.model.Subject;
import org.obehave.model.modifier.SubjectModifierFactory;

import java.util.List;

public class SubjectModifierFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListAdapter adapter;

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
        if(AppState.getInstance().getAction() == null){
            // FIXME: Error Message should be in resource file.
            throw new UiException("Es wurde keine Aktion ausgewählt!");
        }

        if(AppState.getInstance().getAction().getModifierFactory() == null){
            // FIXME: Error Message should be in resource file.
            throw new UiException("Es wurde keine ModifierFactory ausgewählt!");
        }

        SubjectModifierFactory subjectModifierFactory = (SubjectModifierFactory) AppState.getInstance().getAction().getModifierFactory();

        return subjectModifierFactory.getValidSubjects();
    }


}
