package org.obehave.android.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.SubjectAdapter;
import org.obehave.android.ui.exceptions.UiException;
import org.obehave.android.ui.util.DataHolder;

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
        try {
            adapter = (SubjectAdapter) new SubjectAdapter(this.getActivity(), DataHolder.getInstance().getAllSubjects());
            setListAdapter(adapter);
        } catch (UiException e) {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setMessage(e.getMessage());
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
            e.printStackTrace();
        }


        return rootView;
    }
}
