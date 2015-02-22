package org.obehave.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.common.eventbus.Subscribe;
import org.joda.time.DateTime;
import org.obehave.android.R;
import org.obehave.android.ui.events.TimerStartEvent;
import org.obehave.android.ui.events.TimerStopEvent;
import org.obehave.android.ui.events.TimerTaskEvent;
import org.obehave.android.ui.util.DateTimeHelper;
import org.obehave.events.EventBusHolder;

public class RunningCodingsFragment extends CodingListBaseFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private TextView txtTimer;
    private Button btnStartTimer;
    private Button btnStopTimer;

    public static RunningCodingsFragment newInstance(int sectionNumber) {
        Log.d("RunningCodingsFragment", "new Instance");
        RunningCodingsFragment fragment = new RunningCodingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(LOG_TAG, "CREATE VIEW!");

        View rootView = inflater.inflate(R.layout.fragment_running_codings, container, false);
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

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
        EventBusHolder.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        //EventBusHolder.register(this);
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
        EventBusHolder.unregister(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }

    @Subscribe
    public void runIntervalTasks(TimerTaskEvent event){
        Log.d(LOG_TAG, "runIntervalTask");
        updateTimer(event.getStartTime());
    }

    @Override
    public void updateTimer(DateTime startTime) {
        txtTimer.setText(DateTimeHelper.formatToTimer(startTime, DateTime.now()));
    }
}
