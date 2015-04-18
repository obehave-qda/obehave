package org.obehave.view.util;

/**
 * Created by Markus.Moeslinger on 15.04.2015.
 */
public class ControllableTimeProvider implements StopWatch.TimeProvider {
    private int time= 0;

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public long getTime() {
        return time;
    }
}
