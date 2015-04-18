package org.obehave.view.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StopWatchTest {
    private ControllableTimeProvider timeProvider;
    private StopWatch stopWatch;

    @Before
    public void prepare() {
        timeProvider = new ControllableTimeProvider();
        stopWatch = new StopWatch(timeProvider);
    }

    @Test
    public void twoMsElapsed() {
        stopWatch.start();

        timeProvider.forward(1);
        assertEquals(1, stopWatch.getElapsedTime());

        timeProvider.forward(1);
        assertEquals(2, stopWatch.getElapsedTime());
    }

    @Test
    public void stopWatchDoesntHaveToBeStartedAtTimeZero() {
        timeProvider.forward(100);

        stopWatch.start();
        timeProvider.forward(50);
        stopWatch.stop();

        assertEquals(50, stopWatch.getElapsedTime());
    }

    @Test
    public void stopWatchStopsMultipleTimeSpansAndIgnoresTimeWhenStopWatchIsntStarted() {
        timeProvider.forward(200);

        stopWatch.start();
        timeProvider.forward(50);
        stopWatch.stop();

        timeProvider.forward(200);

        stopWatch.start();
        timeProvider.forward(50);
        stopWatch.stop();

        assertEquals(100, stopWatch.getElapsedTime());
    }

    @Test
    public void toggleWillStartStopStartStopTheStopWatch() {
        stopWatch.toggle();
        timeProvider.forward(10);
        stopWatch.toggle();
        timeProvider.forward(1);


        stopWatch.toggle();
        timeProvider.forward(10);
        stopWatch.toggle();
        timeProvider.forward(1);

        assertEquals(20, stopWatch.getElapsedTime());
    }

    @Test
    public void initialElapsedTimeIsZero() {
        assertEquals(0, stopWatch.getElapsedTime());
    }

    @Test
    public void resetWillReset() {
        stopWatch.start();
        timeProvider.forward(1000);
        stopWatch.stop();

        assertEquals(1000, stopWatch.getElapsedTime());
        stopWatch.reset();
        assertEquals(0, stopWatch.getElapsedTime());
    }

    @Test
    public void realStopWatch() throws InterruptedException {
        final long running = 50;
        final long tolerance = 4;

        StopWatch realStopWatch = new StopWatch();

        realStopWatch.start();
        Thread.sleep(running);

        final long elapsed = realStopWatch.getElapsedTime();
        System.out.println(elapsed);
        assertTrue(elapsed >= running && elapsed <= running + tolerance);

    }
}
