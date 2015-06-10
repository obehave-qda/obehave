package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.obehave.android.R;
import org.obehave.android.application.MyApplication;
import org.obehave.android.ui.adapters.EnumerationModifierAdapter;
import org.obehave.android.ui.events.ItemSelectedEvent;
import org.obehave.android.util.ErrorDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.exceptions.FactoryException;
import org.obehave.model.Action;
import org.obehave.model.modifier.Modifier;

public class EnumerationModifierFragment extends BaseModifierFragment implements Updateable {

    private static final String ARG_ACTION = "org.obehave.action";

    private ListAdapter adapter;
    private Action action;
    private MyApplication app;

    public static EnumerationModifierFragment newInstance(int sectionNumber) {
        EnumerationModifierFragment fragment = new EnumerationModifierFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_modifier_listview, container, false);
        app = (MyApplication) getActivity().getApplication();
        action = app.getCodingState().getAction();
        initListview();

        return rootView;
    }


    private void initListview() {
        adapter = (EnumerationModifierAdapter) new EnumerationModifierAdapter(this.getActivity(), action.getModifierFactory().getValidValues());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        String value = (String) getListAdapter().getItem(position);

        try {
            Modifier modifier = action.getModifierFactory().create(value);
            EventBusHolder.post(new ItemSelectedEvent(modifier));
        } catch (FactoryException e) {
            ErrorDialog ed = new ErrorDialog(e, this.getActivity());
            ed.invoke();
        }
    }

    @Override
    public void update() {

    }
}
