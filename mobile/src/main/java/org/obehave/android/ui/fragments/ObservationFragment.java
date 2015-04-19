package org.obehave.android.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.joda.time.DateTime;
import org.obehave.android.R;
import org.obehave.android.application.MyApplication;
import org.obehave.android.ui.activities.ObservationActivity;
import org.obehave.android.ui.views.SubjectCheckBoxView;
import org.obehave.android.util.ListHelper;
import org.obehave.model.Observation;
import org.obehave.model.Subject;
import org.obehave.util.DisplayWrapper;
import org.obehave.util.I18n;

import java.util.List;

public class ObservationFragment extends Fragment {

    private final String LOG_TAG = getClass().getSimpleName();

    private LinearLayout llSubjectsContainer;
    private EditText etName;
    private EditText etInterval;
    private TextView lblInvolvedSubjects;
    private Spinner spFocalSubject;
    private Button btnCreate;
    private SubjectCheckBoxView renderer;

    public static ObservationFragment newInstance() {
        ObservationFragment fragment = new ObservationFragment();

        return fragment;
    }

    public ObservationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_observation_info, container, false);
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
            Log.d(LOG_TAG, "Missing Name");
            errors = true;
        }

        if(etInterval.getText().length() < 1){
            etInterval.setError(I18n.get("android.ui.observation.validation.missinginterval"));
            Log.d(LOG_TAG, "Missing Interval");
            errors = true;
        }

        if(renderer.getSelectedSubjects().isEmpty()){
            lblInvolvedSubjects.setError(I18n.get("android.ui.validationError.missingsubjects"));
            Log.d(LOG_TAG, "SelectedSubject");
            errors = true;
        }

        return !errors;
    }

    private void resetValidation(){
        etName.setError(null);
        etInterval.setError(null);
        lblInvolvedSubjects.setError(null);
    }

    private void createObservation(){
        resetValidation();
        if(validateObservation()){
            Subject focalSubject = (Subject) ((DisplayWrapper) spFocalSubject.getSelectedItem()).get();
            Observation observation = new Observation(etName.getText().toString());
            observation.setDateTime(DateTime.now());
            List<Subject> subjects = renderer.getSelectedSubjects();
            if(!subjects.contains(focalSubject)){
                subjects.add(focalSubject);
            }
            observation.setParticipatingSubjects(renderer.getSelectedSubjects());
            observation.setFocalSubject(focalSubject);
            returnResult(observation);
        }
        else {
            Log.d(LOG_TAG, "Validation error");
        }
    }

    private void returnResult(Observation observation) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ObservationActivity.RESULT_OBSERVATION, observation);
        resultIntent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, resultIntent);
        getActivity().finish();
    }

    private void initComponents(View rootView) {
        MyApplication app = (MyApplication) getActivity().getApplication();
        llSubjectsContainer = (LinearLayout) rootView.findViewById(R.id.subjectContainer);
        spFocalSubject = (Spinner) rootView.findViewById(R.id.spFocalSubject);
        etInterval = (EditText) rootView.findViewById(R.id.etInterval);
        etName = (EditText) rootView.findViewById(R.id.etName);
        btnCreate = (Button) rootView.findViewById(R.id.btnCreate);
        lblInvolvedSubjects = (TextView) rootView.findViewById(R.id.labelInvolvedSubjects);
        ArrayAdapter<DisplayWrapper> spinnerAdapter = new ArrayAdapter<DisplayWrapper>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, (ListHelper.convertToDisplayWrapperList(app.getStudy().getSubjectsList())));
        spFocalSubject.setAdapter(spinnerAdapter);
        renderer = new SubjectCheckBoxView(getActivity(), llSubjectsContainer, app.getStudy().getSubjectsList());
        renderer.render();
    }

}