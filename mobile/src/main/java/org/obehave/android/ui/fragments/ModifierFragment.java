package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.obehave.android.R;
import org.obehave.android.database.AbstractDbAdapter;
import org.obehave.android.ui.activities.MainActivity;
import org.obehave.android.ui.adapters.MyCheckboxCursorAdapter;
import org.obehave.model.domain.Action;


public class ModifierFragment extends BaseFragment {

    private ListView lvGroups;
    private MainActivity mainActivity;
    private AbstractDbAdapter modifierDbAdapter;

    public ModifierFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkbox_listview, container, false);

        lvGroups = (ListView) rootView.findViewById(R.id.lvSimple);
        lvGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemClick", "" + position + "   " + id);
            }
        });

        mainActivity = (MainActivity) getActivity();
        modifierDbAdapter = mainActivity.getActionAdapter();

        MyCheckboxCursorAdapter<Action> adapter = new MyCheckboxCursorAdapter(getActivity(), modifierDbAdapter);
        lvGroups.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){

        }
    }
}
