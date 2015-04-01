package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.EnumerationModifierAdapter;
import org.obehave.android.ui.events.ModifierSelectedEvent;
import org.obehave.android.ui.events.ModifierType;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;

public class EnumerationModifierFragment extends BaseModifierFragment {

    private static final String ARG_ACTION = "org.obehave.action";

    private Action action;
    private ListAdapter adapter;

    public static EnumerationModifierFragment newInstance(int sectionNumber, Action action) {
        EnumerationModifierFragment fragment = new EnumerationModifierFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_modifier_listview, container, false);

        initArgs();
        initListview();

        return rootView;
    }

    private void initArgs() {
        action = (Action) this.getArguments().getSerializable(ARG_ACTION);
    }

    private void initListview() {
        adapter = (EnumerationModifierAdapter) new EnumerationModifierAdapter(this.getActivity(), action.getModifierFactory().getValidValues());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        String value = (String) getListAdapter().getItem(position);

        EventBusHolder.post(new ModifierSelectedEvent(value, ModifierType.ENUMERATION_MODIFIER));
    }
}
