package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.common.eventbus.Subscribe;
import org.obehave.android.R;
import org.obehave.android.application.MyApplication;
import org.obehave.android.database.DataHolder;
import org.obehave.android.events.NodeSelectedEvent;
import org.obehave.android.ui.adapters.NameAscendingComparator;
import org.obehave.android.ui.adapters.NameDescendingComparator;
import org.obehave.android.ui.adapters.SubjectAdapter;
import org.obehave.android.ui.events.SubjectSelectedEvent;
import org.obehave.android.ui.fragments.behaviors.FragmentBehavior;
import org.obehave.android.ui.fragments.behaviors.SortType;
import org.obehave.android.ui.fragments.behaviors.SortableBehavior;
import org.obehave.android.ui.fragments.events.SortingChangedEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Node;
import org.obehave.model.Subject;

public class SubjectFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_PARENT_NODE = "parent_node";

    private ListAdapter adapter;
    private Node parent;

    private FragmentBehavior sortableBehavior;

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

        initBehavior(rootView);
        initParentNode();
        initListview();

        return rootView;
    }

    private void initBehavior(View rootView) {
        sortableBehavior = new SortableBehavior();
        Bundle settings = new Bundle();
        settings.putInt(SortableBehavior.ARG_START_SORT_ORDER, MyApplication.getSubjectSortOrder());
        sortableBehavior.init(getActivity(), this, rootView, settings);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "onActivityCreated");
    }

    private void initParentNode(){
        parent = (Node) this.getArguments().getSerializable(ARG_PARENT_NODE);
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
            MyApplication.selectItem(subject);
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

    @Override
    public void onStart() {
        super.onStart();
        EventBusHolder.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusHolder.unregister(this);
    }

    @Subscribe
    public void sortingChanged(SortingChangedEvent event){
        if (event.getSortType() == SortType.DEFAULT){
            ((SubjectAdapter) adapter).sortDefault();
            MyApplication.setSubjectSortOrder(SortType.DEFAULT);
        }
        else if (event.getSortType() == SortType.ALPHABETICAL_ASCENDING){
            ((SubjectAdapter) adapter).sortByName(new NameAscendingComparator());
            MyApplication.setSubjectSortOrder(SortType.ALPHABETICAL_ASCENDING);
        }
        else if (event.getSortType() == SortType.ALPHABETICAL_DESCENDING){
            ((SubjectAdapter) adapter).sortByName(new NameDescendingComparator());
        }

    }
}
