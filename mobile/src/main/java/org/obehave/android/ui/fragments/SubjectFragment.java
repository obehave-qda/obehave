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
import org.obehave.android.ui.adapters.NameAscendingComparator;
import org.obehave.android.ui.adapters.NameDescendingComparator;
import org.obehave.android.ui.adapters.SubjectAdapter;
import org.obehave.android.ui.events.ItemSelectedEvent;
import org.obehave.android.ui.events.NodeSelectedEvent;
import org.obehave.android.ui.fragments.behaviors.FragmentBehavior;
import org.obehave.android.ui.fragments.behaviors.SortType;
import org.obehave.android.ui.fragments.behaviors.SortableBehavior;
import org.obehave.events.EventBusHolder;
import org.obehave.model.Node;
import org.obehave.model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectFragment extends BaseListFragment{

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListAdapter adapter;
    private FragmentBehavior sortableBehavior;
    private MyApplication app;

    public static SubjectFragment newInstance(int sectionNumber) {
        return createFragment(sectionNumber);
    }


    private static SubjectFragment createFragment(int sectionNumber){
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_subject, container, false);
        app = (MyApplication) getActivity().getApplication();
        initBehavior(rootView);
        initListview();

        return rootView;
    }

    private void initBehavior(View rootView) {
        sortableBehavior = new SortableBehavior();
        Bundle settings = new Bundle();
        sortableBehavior.init(getActivity(), this, rootView, settings);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBusHolder.register(this);
        Log.d("MainActivity", "onActivityCreated SubjectFragment");
        initListview();
    }

    private void initListview(){
        List<Subject> subjects = new ArrayList<Subject>();
        if(app.getObservation() != null){
            subjects = app.getObservation().getParticipatingSubjects();
        }
        adapter = new SubjectAdapter(getActivity(), subjects, new ArrayList<Node>());
        setListAdapter(adapter);
        changeOrderOfListView(app.getSubjectSortOrder());
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Object object = adapter.getItem(position);

        if (object instanceof Subject) {
            Subject subject = (Subject) object;
            EventBusHolder.post(new ItemSelectedEvent(subject));
        } else if (object instanceof Node) {
            Node node = (Node) object;
            EventBusHolder.post(new NodeSelectedEvent(node, NodeSelectedEvent.NodeType.SUBJECT));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBusHolder.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusHolder.unregister(this);
    }

    @Subscribe
    private void changeOrderOfListView(int sortType){
        if (sortType == SortType.DEFAULT){
            ((SubjectAdapter) adapter).sortDefault();

        }
        else if (sortType == SortType.ALPHABETICAL_ASCENDING){
            ((SubjectAdapter) adapter).sortByName(new NameAscendingComparator());
        }
        else if (sortType == SortType.ALPHABETICAL_DESCENDING){
            ((SubjectAdapter) adapter).sortByName(new NameDescendingComparator());
        }

        app.setSubjectSortOrder(sortType);
    }
}
