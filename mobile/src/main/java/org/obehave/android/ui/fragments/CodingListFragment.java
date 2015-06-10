package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.common.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.obehave.android.R;
import org.obehave.android.application.MyApplication;
import org.obehave.android.ui.adapters.CodingAdapter;
import org.obehave.android.ui.events.TimerStartEvent;
import org.obehave.android.ui.events.TimerStopEvent;
import org.obehave.android.ui.events.TimerTaskEvent;
import org.obehave.android.util.DateTimeHelper;
import org.obehave.android.util.ErrorDialog;
import org.obehave.events.EventBusHolder;
import org.obehave.exceptions.ServiceException;
import org.obehave.model.Action;
import org.obehave.model.Coding;
import org.obehave.model.Subject;
import org.obehave.service.CodingService;
import org.obehave.util.I18n;

import java.util.ArrayList;
import java.util.List;

public class CodingListFragment extends ListFragment implements Updateable{

    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_CODING_TYPE = "arg_coding";
    private static final String STATE_DATA = "state_data";

    private static final int MENUITEM_STOP_CODING = 0;
    private static final int MENUITEM_EDIT_CODING = 1;
    private static final int MENUITEM_DELETE_CODING = 2;

    public static final int CODING_TYPE_ALL = 0;
    public static final int CODING_TYPE_RUNNING = 1;

    private CodingAdapter adapter;
    private int codingType;

    private TextView txtTimer;
    private Button btnStartTimer;
    private Button btnStopTimer;

    private Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
            timerHandler.postDelayed(this, 200);
        }
    };
    private MyApplication app;


    public static CodingListFragment newInstance(int sectionNumber, int codingType) {
        CodingListFragment fragment = new CodingListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_CODING_TYPE, codingType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_DATA, adapter.getCodings());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBusHolder.register(this);
        initAdapter();
        setListAdapter(adapter);
    }

    private void initAdapter() {
        List<Coding> codingList = new ArrayList<Coding>();
        if (app.getObservation() != null) {
            if (codingType == CODING_TYPE_ALL) {
                codingList =  app.getObservation().getCodings();
            } else if (codingType == CODING_TYPE_RUNNING) {
                codingList = app.getObservation().getOpenCodings();

            }
        }

        adapter = new CodingAdapter(getActivity(), codingList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(I18n.get("android.coding.contextmenu.header"));
            Coding codingItem = (Coding) getListAdapter().getItem(info.position);

            if(codingItem.isOpen()){
                menu.add(Menu.NONE, 0, MENUITEM_STOP_CODING, I18n.get("android.coding.contextmenu.stop"));
            }
            if(!codingItem.isOpen()) {
                menu.add(Menu.NONE, 1, MENUITEM_EDIT_CODING, I18n.get("android.coding.contextmenu.edit"));
            }
            menu.add(Menu.NONE, 2, MENUITEM_DELETE_CODING, I18n.get("android.coding.contextmenu.delete"));

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Coding codingItem = (Coding) getListAdapter().getItem(info.position);
        try {
            if (item.getItemId() == MENUITEM_DELETE_CODING) {
                app.getCodingService().delete(codingItem);
            }
            else if(item.getItemId() == MENUITEM_EDIT_CODING){
                // return to main activity / which should open another activity for editing

            }
            adapter.notifyDataSetChanged();
        } catch (ServiceException e) {
            ErrorDialog ed = new ErrorDialog(I18n.get("android.ui.delete.error"), getActivity());
            ed.invoke();
        }
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_codings_list, container, false);
        app = (MyApplication) getActivity().getApplication();
        txtTimer = (TextView) rootView.findViewById(R.id.tvTimer);
        btnStartTimer = (Button) rootView.findViewById(R.id.btnStartTimer);
        btnStopTimer = (Button) rootView.findViewById(R.id.btnStopTimer);

        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHolder.post(new TimerStartEvent());
            }
        });

        btnStopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBusHolder.post(new TimerStopEvent());
            }
        });

        Bundle args = getArguments();
        codingType = args.getInt(ARG_CODING_TYPE);
        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        EventBusHolder.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        timerHandler.postDelayed(timerRunnable, 500);
        EventBusHolder.register(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Coding coding = (Coding) adapter.getItem(position);
        if(coding.isOpen()){
            try {
                CodingService codingService = app.getCodingService();
                Subject s = coding.getSubject();
                Action a = coding.getAction();
                String m = coding.getModifier()!=null?coding.getModifier().getBuildString():null;
                long endMs = DateTimeHelper.diffMs(coding.getObservation().getDateTime(), DateTime.now());
                codingService.endCoding(s, a, m, endMs);
                updateCodings();

            } catch (ServiceException e) {
                ErrorDialog ed = new ErrorDialog(e, getActivity());
                ed.invoke();
            }
        }
    }

    public void updateCodings(){
        if( app.getObservation() != null) {

            if (codingType == CODING_TYPE_ALL) {
                adapter.setCodings(app.getObservation().getCodings());
            } else if (codingType == CODING_TYPE_RUNNING) {
                adapter.setCodings(app.getObservation().getOpenCodings());
            }
        }
    }


    @Subscribe
    public void runIntervalTasks(TimerTaskEvent event){
        updateTimer(event.getStartTime());
    }

    public void updateTimer(DateTime startTime) {
        txtTimer.setText(DateTimeHelper.formatToTimer(startTime, DateTime.now()));
    }

    @Override
    public void update() {
        updateCodings();
    }
}
