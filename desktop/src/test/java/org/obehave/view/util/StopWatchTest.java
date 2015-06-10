package org.obehave.view.util;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StopWatchTest {
    private ControllableTimeProvider time;
    private StopWatch stopWatch;

    @Before
    public void prepare() {
        time = new ControllableTimeProvider();
        stopWatch = new StopWatch(time);
    }

    @Test
    public void twoMsElapsed() {
        stopWatch.start();

        time.forward(1);
        assertEquals(1, stopWatch.getElapsedTime(), 0);

        time.forward(1);
        assertEquals(2, stopWatch.getElapsedTime(), 0);
    }

    @Test
    public void stopWatchDoesntHaveToBeStartedAtTimeZero() {
        time.forward(100);

        stopWatch.start();
        time.forward(50);
        stopWatch.stop();

        assertEquals(50, stopWatch.getElapsedTime(), 0);
    }

    @Test
    public void stopWatchStopsMultipleTimeSpansAndIgnoresTimeWhenStopWatchIsntStarted() {
        time.forward(200);

        stopWatch.start();
        time.forward(50);
        stopWatch.stop();

        time.forward(200);

        stopWatch.start();
        time.forward(50);
        stopWatch.stop();

        assertEquals(100, stopWatch.getElapsedTime(), 0);
    }

    @Test
    public void toggleWillStartStopStartStopTheStopWatch() {
        stopWatch.toggle();
        time.forward(10);
        stopWatch.toggle();
        time.forward(1);


        stopWatch.toggle();
        time.forward(10);
        stopWatch.toggle();
        time.forward(1);

        assertEquals(20, stopWatch.getElapsedTime(), 0);
    }

    @Test
    public void initialElapsedTimeIsZero() {
        assertEquals(0, stopWatch.getElapsedTime(), 0);
    }

    @Test
    public void resetWillReset() {
        stopWatch.start();
        time.forward(1000);
        stopWatch.stop();

        assertEquals(1000, stopWatch.getElapsedTime(), 0);
        stopWatch.reset();
        assertEquals(0, stopWatch.getElapsedTime(), 0);
    }

    @Test
    public void realStopWatch() throws InterruptedException {
        final long running = 50;
        final long tolerance = 4;

        StopWatch realStopWatch = new StopWatch();

        realStopWatch.start();
        Thread.sleep(running);

        final double elapsed = realStopWatch.getElapsedTime();
        System.out.println(elapsed);
        assertTrue(elapsed >= running && elapsed <= running + tolerance);
    }

    @Test
    public void rate2WillMultiplyTime() {
        stopWatch.setRate(2);
        stopWatch.start();
        time.forward(5);
        stopWatch.stop();

        assertEquals(10, stopWatch.getElapsedTime(), 0.0);
    }

    @Test
    public void changingRateWhileRunning() {
        // this should add 5 seconds
        stopWatch.start();
        time.forward(5);

        // this should add 10 seconds
        stopWatch.setRate(2);
        time.forward(5);

        stopWatch.stop();

        assertEquals(15, stopWatch.getElapsedTime(), 0.0);
    }
}
