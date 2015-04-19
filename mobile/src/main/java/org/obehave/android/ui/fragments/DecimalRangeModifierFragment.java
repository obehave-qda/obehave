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
import org.obehave.android.application.MyApplication;
import org.obehave.android.ui.events.ItemSelectedEvent;
import org.obehave.android.util.ErrorDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.Action;
import org.obehave.model.modifier.Modifier;

public class DecimalRangeModifierFragment extends Fragment implements Updateable {

    private static final String ARG_ACTION = "org.obehave.action";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final String LOG_TAG = this.getClass().getSimpleName();

    private Action action;

    private Button btnAccept;
    private TextView tvValueLabel;
    private EditText etValue;
    private MyApplication app;

    public static DecimalRangeModifierFragment newInstance(int sectionNumber) {
        DecimalRangeModifierFragment fragment = new DecimalRangeModifierFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_decimal_range_modifier, container, false);
        app = (MyApplication) getActivity().getApplication();
        action = app.getCodingState().getAction();
        btnAccept = (Button) rootView.findViewById(R.id.accept);
        tvValueLabel = (TextView) rootView.findViewById(R.id.labelValueTextfield);
        etValue = (EditText) rootView.findViewById(R.id.txtValue);

        tvValueLabel.setText("The value should be between " + action.getModifierFactory().getFrom() + " and " + action.getModifierFactory().getTo() + ":");
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                String value = etValue.getText().toString();
                try {
                    Modifier modifier = action.getModifierFactory().create(value);
                    EventBusHolder.post(new ItemSelectedEvent(modifier));
                } catch (FactoryException e) {
                    ErrorDialog ed = new ErrorDialog(e, getActivity());
                    ed.invoke();
                }
            }
        });

        return rootView;
    }

    private void initArgs() {
        action = (Action) this.getArguments().getSerializable(ARG_ACTION);
    }

    @Override
    public void update() {

    }
}
