package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.obehave.android.R;
import org.obehave.android.ui.events.ReplaceFragmentByEvent;
import org.obehave.events.EventBusHolder;

public class CodingPlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static CodingPlaceholderFragment newInstance(int sectionNumber) {
        CodingPlaceholderFragment fragment = new CodingPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBusHolder.post(new ReplaceFragmentByEvent(SubjectFragment.newInstance(getArguments().getInt(ARG_SECTION_NUMBER))));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView =  inflater.inflate(R.layout.fragment_coding_placeholder, container, false);

        return rootView;

    }

}
