package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.obehave.android.R;
import org.obehave.android.database.DataHolder;
import org.obehave.android.events.NodeSelectedEvent;
import org.obehave.android.ui.adapters.NameAscendingComparator;
import org.obehave.android.ui.adapters.NameDescendingComparator;
import org.obehave.android.ui.adapters.SubjectAdapter;
import org.obehave.android.ui.events.SubjectSelectedEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Node;
import org.obehave.model.Subject;

public class SubjectFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_PARENT_NODE = "parent_node";

    private Spinner spSort;
    private ArrayAdapter<String> sortAdapter;
    private ListAdapter adapter;
    private Node parent;
    private String[] sortOrders = {
            "Standard",
            "Alphabetisch A-Z",
            "Alphabetisch Z-A"
    };


    private static final int DEFAULT_SORT_ORDER = 0;
    private static final int ALPHABETICAL_ASCENDING = 1;
    private static final int ALPHABETICAL_DESCENDING = 2;

    public static SubjectFragment newInstance(int sectionNumber, Node parent) {
        return createFragment(sectionNumber, parent);
    }


    public static SubjectFragment newInstance(int sectionNumber) {
        return createFragment(sectionNumber, DataHolder.subject().getRootNode());
    }

    private static SubjectFragment createFragment(int sectionNumber, Node parent){
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_PARENT_NODE, parent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_subject, container, false);

        //if(savedInstanceState == null)
        initParentNode();
        initSortComponent(rootView);
        initSortEventListeners();
        initListview();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
        if(savedInstanceState == null){

        }
        //restoreData(savedInstanceState);

    }

    private void restoreData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            parent = (Node) savedInstanceState.getSerializable(ARG_PARENT_NODE);
        }
    }

    private void initParentNode(){
        parent = (Node) this.getArguments().getSerializable(ARG_PARENT_NODE);
    }

    private void initSortComponent(View rootView){
        spSort = (Spinner) rootView.findViewById(R.id.spSort);
        sortAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sortOrders);
        spSort.setAdapter(sortAdapter);
    }

    private void initSortEventListeners(){
        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == DEFAULT_SORT_ORDER) {
                    ((SubjectAdapter) adapter).sortDefault();
                } else if (position == ALPHABETICAL_ASCENDING) {
                    ((SubjectAdapter) adapter).sortByName(new NameAscendingComparator());
                } else if (position == ALPHABETICAL_DESCENDING) {
                    ((SubjectAdapter) adapter).sortByName(new NameDescendingComparator());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void initListview(){
        adapter = new SubjectAdapter(this.getActivity(), DataHolder.subject().getData(parent), DataHolder.subject().getChildren(parent));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Object object = adapter.getItem(position);

        if (object instanceof Subject) {
            Subject subject = (Subject) object;
            EventBusHolder.post(new SubjectSelectedEvent(subject));
        } else if (object instanceof Node) {
            Node node = (Node) object;
            EventBusHolder.post(new NodeSelectedEvent(node, NodeSelectedEvent.NodeType.SUBJECT));
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
        outState.putSerializable(ARG_PARENT_NODE, parent);
    }
}
