package org.obehave.android.ui.events;

import org.joda.time.DateTime;

/**
 * Created by patrick on 20.02.2015.
 */
public class TimerTaskEvent {
    private final DateTime startTime;

    public TimerTaskEvent(DateTime startTime){
        this.startTime = startTime;
    }

    public DateTime getStartTime() {
        return startTime;
    }
}
