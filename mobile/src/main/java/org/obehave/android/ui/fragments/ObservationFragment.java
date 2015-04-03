package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.obehave.android.R;
import org.obehave.android.database.DataHolder;
import org.obehave.android.ui.activities.ObservationActivity;
import org.obehave.android.util.LinearLayoutRender;
import org.obehave.service.Study;
import org.obehave.util.DisplayWrapper;
import org.obehave.util.I18n;

public class ObservationFragment extends Fragment {

    private Study study;
    private LinearLayout llSubjectsContainer;
    private EditText etName;
    private EditText etInterval;
    private TextView lblInvolvedSubjects;
    private Spinner spMainSubject;
    private Button btnCreate;
    private LinearLayoutRender renderer;

    public static ObservationFragment newInstance(Study study) {
        ObservationFragment fragment = new ObservationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ObservationActivity.ARG_STUDY, study);
        fragment.setArguments(args);

        return fragment;
    }

    public ObservationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_observation_info, container, false);
        initArgs();
        initComponents(rootView);
        initButtonListener();
        return rootView;
    }

    private void initButtonListener() {
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createObservation();
            }
        });
    }

    private boolean validateObservation(){
        boolean errors = false;
        if(etName.getText().length() < 1){
            etName.setError(I18n.get("android.ui.observation.validation.missingname"));
            errors = true;
        }

        if(etInterval.getText().length() < 1){
            etInterval.setError(I18n.get("android.ui.observation.validation.missinginterval"));
            errors = true;
        }

        if(renderer.getSelectedSubjects().isEmpty()){
            lblInvolvedSubjects.setError(I18n.get("android.ui.validationError.missingsubjects"));
            errors = true;
        }

        return errors;
    }

    private void resetValidation(){
        etName.setError(null);
        etInterval.setError(null);
        lblInvolvedSubjects.setError(null);
    }

    private void createObservation(){
        resetValidation();
        if(validateObservation()){

        }
    }

    private void initComponents(View rootView) {
        llSubjectsContainer = (LinearLayout) rootView.findViewById(R.id.subjectContainer);
        spMainSubject = (Spinner) rootView.findViewById(R.id.spMainSubject);
        etInterval = (EditText) rootView.findViewById(R.id.etInterval);
        etName = (EditText) rootView.findViewById(R.id.etName);
        btnCreate = (Button) rootView.findViewById(R.id.btnCreate);
        lblInvolvedSubjects = (TextView) rootView.findViewById(R.id.labelInvolvedSubjects);
        ArrayAdapter<DisplayWrapper> spinnerAdapter = new ArrayAdapter<DisplayWrapper>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, DataHolder.subject().getAllSubjectWrapped());
        spMainSubject.setAdapter(spinnerAdapter);
        renderer = new LinearLayoutRender(getActivity(), llSubjectsContainer, DataHolder.subject().getAllSubjects());
        renderer.render();
    }

    private void initArgs() {
        study = (Study) getArguments().getSerializable(ObservationActivity.ARG_STUDY);
    }
}