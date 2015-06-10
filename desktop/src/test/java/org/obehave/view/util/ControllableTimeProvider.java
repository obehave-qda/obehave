package org.obehave.view.util;

/**
 * Class to manipulate the time that {@link StopWatch} is using.
 */
public class ControllableTimeProvider implements StopWatch.TimeProvider {
    private int ms = 0;

    public void forward(int ms) {
        this.ms += ms;
    }

    @Override
    public long getTime() {
        return ms;
    }
}
