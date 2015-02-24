package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.obehave.android.R;
import org.obehave.android.ui.events.DecimalRangeModifierSelectedEvent;
import org.obehave.events.EventBusHolder;

public class DecimalRangeModifierFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private final String LOG_TAG = this.getClass().getSimpleName();

    private int from;
    private int to;

    private Button acceptButton;
    private TextView label;
    private EditText value;

    public static DecimalRangeModifierFragment newInstance(int sectionNumber, int from, int to) {
        DecimalRangeModifierFragment fragment = new DecimalRangeModifierFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setRange(from, to);

        return fragment;
    }

    public void setRange(int from, int to){
        this.from = from;
        this.to = to;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_decimal_range_modifier, container, false);

        acceptButton  = (Button) rootView.findViewById(R.id.accept);
        label = (TextView) rootView.findViewById(R.id.labelValueTextfield);
        value = (EditText) rootView.findViewById(R.id.txtValue);

        label.setText("Your Value should be between " + from + " and " + to + ":");
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                EventBusHolder.post(new DecimalRangeModifierSelectedEvent(value.getText().toString()));
            }
        });

        return rootView;
    }
}
