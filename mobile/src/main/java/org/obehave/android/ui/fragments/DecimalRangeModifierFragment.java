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
import org.obehave.android.ui.events.ModifierSelectedEvent;
import org.obehave.android.ui.events.ModifierType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;

public class DecimalRangeModifierFragment extends Fragment {

    private static final String ARG_ACTION = "org.obehave.action";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Action action;

    private Button acceptButton;
    private TextView label;
    private EditText value;

    public static DecimalRangeModifierFragment newInstance(int sectionNumber, Action action) {
        DecimalRangeModifierFragment fragment = new DecimalRangeModifierFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_decimal_range_modifier, container, false);
        initArgs();
        acceptButton  = (Button) rootView.findViewById(R.id.accept);
        label = (TextView) rootView.findViewById(R.id.labelValueTextfield);
        value = (EditText) rootView.findViewById(R.id.txtValue);

        label.setText("Your Value should be between " + action.getModifierFactory().getFrom() + " and " + action.getModifierFactory().getTo() + ":");
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                EventBusHolder.post(new ModifierSelectedEvent(value.getText().toString(), ModifierType.ENUMERATION_MODIFIER));
            }
        });

        return rootView;
    }

    private void initArgs() {
        action = (Action) this.getArguments().getSerializable(ARG_ACTION);
    }
}
