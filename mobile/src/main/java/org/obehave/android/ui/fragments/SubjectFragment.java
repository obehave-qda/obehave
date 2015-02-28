package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.obehave.android.R;
import org.obehave.android.events.NodeSelectedEvent;
import org.obehave.android.ui.adapters.NameAscendingComparator;
import org.obehave.android.ui.adapters.NameDescendingComparator;
import org.obehave.android.ui.adapters.SubjectAdapter;
import org.obehave.android.ui.events.SubjectSelectedEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Node;
import org.obehave.model.Subject;

import java.util.List;

public class SubjectFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Spinner spSort;
    private ArrayAdapter<String> sortAdapter;
    private ListAdapter adapter;
    private List<Subject> subjects;
    private List<Node> nodes;
    private String[] sortOrders = {
            "Standard",
            "Alphabetisch A-Z",
            "Alphabetisch Z-A"
    };

    private static final int DEFAULT_SORT_ORDER = 0;
    private static final int ALPHABETICAL_ASCENDING = 1;
    private static final int ALPHABETICAL_DESCENDING = 2;

    public static SubjectFragment newInstance(int sectionNumber, List<Subject> subjects, List<Node> nodes) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setSubjects(subjects);
        fragment.setNodes(nodes);
        /* which type of fragment should be loaded */
        return fragment;
    }

    private void setSubjects(List<Subject> subjects){
        this.subjects = subjects;
    }

    private void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_subject, container, false);
        sortAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, sortOrders);
        spSort = (Spinner) rootView.findViewById(R.id.spSort);
        adapter = (SubjectAdapter) new SubjectAdapter(this.getActivity(), subjects, nodes);
        spSort.setAdapter(sortAdapter);
        setListAdapter(adapter);

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == DEFAULT_SORT_ORDER){
                    ((SubjectAdapter)adapter).sortDefault();
                }
                else if(position == ALPHABETICAL_ASCENDING){
                    ((SubjectAdapter)adapter).sortByName(new NameAscendingComparator());
                }
                else if(position == ALPHABETICAL_DESCENDING){
                    ((SubjectAdapter)adapter).sortByName(new NameDescendingComparator());
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

        if(object instanceof Subject){
            Subject subject = (Subject) object;
            EventBusHolder.post(new SubjectSelectedEvent(subject));
        }
        else if(object instanceof Node) {
            Node node = (Node) object;
            EventBusHolder.post(new NodeSelectedEvent(node, NodeSelectedEvent.NodeType.SUBJECT));
        }

    }
}
