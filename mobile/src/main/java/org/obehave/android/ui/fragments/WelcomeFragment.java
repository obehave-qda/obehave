package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.obehave.android.R;
import org.obehave.android.ui.events.OpenFileChooserEvent;
import org.obehave.events.EventBusHolder;

public class WelcomeFragment extends Fragment {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Button btnChooseFile;
    private Button btnImport;

    public WelcomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);
        btnChooseFile = (Button) rootView.findViewById(R.id.btnChooseStudyFile);
        btnImport = (Button) rootView.findViewById(R.id.btnChooseStudyWLAN);
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHolder.post(new OpenFileChooserEvent());
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new UnsupportedOperationException();
            }
        });

        return rootView;
    }
}
