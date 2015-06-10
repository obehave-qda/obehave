package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import org.obehave.android.R;
import org.obehave.android.ui.adapters.FileAdapter;
import org.obehave.android.ui.events.FileChoosenEvent;
import org.obehave.android.util.ErrorDialog;
import org.obehave.android.util.ExternalStorageHelper;
import org.obehave.events.EventBusHolder;
import org.obehave.util.I18n;

import java.io.File;
import java.io.IOException;

public class DirectoryListFragment extends BaseListFragment implements Updateable {

    private ListAdapter adapter;
    private String DATABASE_FOLDER = "obehave";

    public static DirectoryListFragment newInstance() {
        return createFragment();
    }

    private static DirectoryListFragment createFragment(){
        DirectoryListFragment fragment = new DirectoryListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_directory_list, container, false);
        try {
            ExternalStorageHelper.createFolderIfNotExists(DATABASE_FOLDER);
        } catch (IOException e) {
            ErrorDialog ed = new ErrorDialog(I18n.get("android.ui.study.error.notpossibletocreatedirectory", DATABASE_FOLDER), getActivity());
            ed.invoke();
        }

        initListView();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initListView(){
        adapter = new FileAdapter(getActivity(), ExternalStorageHelper.listFiles(DATABASE_FOLDER, ".h2.db"));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        File file = (File) adapter.getItem(position);
        EventBusHolder.post(new FileChoosenEvent(file));
    }

    @Override
    public void update() {

    }
}
