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
import org.obehave.android.ui.events.NodeSelectedEvent;
import org.obehave.android.ui.adapters.ActionAdapter;
import org.obehave.android.ui.adapters.NameAscendingComparator;
import org.obehave.android.ui.adapters.NameDescendingComparator;
import org.obehave.android.ui.events.ItemSelectedEvent;
import org.obehave.android.ui.fragments.behaviors.FragmentBehavior;
import org.obehave.android.ui.fragments.behaviors.SortType;
import org.obehave.android.ui.fragments.behaviors.SortableBehavior;
import org.obehave.android.ui.fragments.events.SortingChangedEvent;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Action;
import org.obehave.model.Node;

public class ActionFragment extends MyListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_PARENT_NODE = "parent_node";

    private ListAdapter adapter;
    private Node parent;

    private FragmentBehavior sortableBehavior;

    public static ActionFragment newInstance(int sectionNumber, Node parent) {
        return createFragment(sectionNumber, parent);
    }

    public static ActionFragment newInstance(int sectionNumber) {
        return createFragment(sectionNumber, DataHolder.action().getRootNode());
    }

    private static ActionFragment createFragment(int sectionNumber, Node parent){
        ActionFragment fragment = new ActionFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_action, container, false);

        initBehavior(rootView);
        initParentNode();
        initListview();

        return rootView;
    }

    private void initBehavior(View rootView) {
        sortableBehavior = new SortableBehavior();
        Bundle settings = new Bundle();
        settings.putInt(SortableBehavior.ARG_START_SORT_ORDER, MyApplication.getActionSortOrder());
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
        adapter = new ActionAdapter(this.getActivity(), DataHolder.action().getData(parent), DataHolder.action().getChildren(parent));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Object object = adapter.getItem(position);

        if (object instanceof Action) {
            Action action = (Action) object;
            MyApplication.selectItem(action);
            EventBusHolder.post(new ItemSelectedEvent(action));
        } else if (object instanceof Node) {
            Node node = (Node) object;
            EventBusHolder.post(new NodeSelectedEvent(node, NodeSelectedEvent.NodeType.ACTION));
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
            ((ActionAdapter) adapter).sortDefault();
            MyApplication.setActionSortOrder(SortType.DEFAULT);
        }
        else if (event.getSortType() == SortType.ALPHABETICAL_ASCENDING){
            ((ActionAdapter) adapter).sortByName(new NameAscendingComparator());
            MyApplication.setActionSortOrder(SortType.ALPHABETICAL_ASCENDING);
        }
        else if (event.getSortType() == SortType.ALPHABETICAL_DESCENDING){
            ((ActionAdapter) adapter).sortByName(new NameDescendingComparator());
        }

    }
}