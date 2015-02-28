package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.obehave.android.R;
import org.obehave.android.events.NodeSelectedEvent;
import org.obehave.android.ui.adapters.ActionAdapter;
import org.obehave.android.ui.adapters.NameAscendingComparator;
import org.obehave.android.ui.adapters.NameDescendingComparator;
import org.obehave.android.ui.events.ActionSelectedEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;
import org.obehave.model.Node;

import java.util.List;

public class ActionFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Spinner spSort;
    private ArrayAdapter<String> sortAdapter;
    private ListAdapter adapter;
    private List<Action> actions;
    private List<Node> nodes;
    private String[] sortOrders = {
            "Standard",
            "Alphabetisch A-Z",
            "Alphabetisch Z-A"
    };

    private static final int DEFAULT_SORT_ORDER = 0;
    private static final int ALPHABETICAL_ASCENDING = 1;
    private static final int ALPHABETICAL_DESCENDING = 2;

    public static ActionFragment newInstance(int sectionNumber, List<Action> actions, List<Node> nodes) {
        ActionFragment fragment = new ActionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setActions(actions);
        fragment.setNodes(nodes);
        /* which type of fragment should be loaded */
        return fragment;
    }

    public void setActions(List<Action> actions){
        this.actions = actions;
    }

    public void setNodes(List<Node> nodes){
        this.nodes = nodes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_action, container, false);

        adapter = (ActionAdapter) new ActionAdapter(this.getActivity(), actions, nodes);
        sortAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, sortOrders);
        spSort = (Spinner) rootView.findViewById(R.id.spSort);
        spSort.setAdapter(sortAdapter);
        setListAdapter(adapter);

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == DEFAULT_SORT_ORDER) {
                    ((ActionAdapter) adapter).sortDefault();
                } else if (position == ALPHABETICAL_ASCENDING) {
                    ((ActionAdapter) adapter).sortByName(new NameAscendingComparator());
                } else if (position == ALPHABETICAL_DESCENDING) {
                    ((ActionAdapter) adapter).sortByName(new NameDescendingComparator());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Object object = adapter.getItem(position);

        if(object instanceof Action){
            Action action = (Action) object;
            EventBusHolder.post(new ActionSelectedEvent(action));
        }
        else if(object instanceof Node) {
            Node node = (Node) object;
            EventBusHolder.post(new NodeSelectedEvent(node, NodeSelectedEvent.NodeType.ACTION));
        }
    }
}
