package org.obehave.view.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;

public class StopWatch {
    /**
     * After how many ms should the stopwatch update?
     */
    private static final double PRECISION = 100;

    private static final long NOT_STARTED = -1;

    private final Timeline timeline;
    private final TimeProvider timeProvider;
    private final LongProperty elapsedTime = new SimpleLongProperty(this, "elapsedTime", 0);

    private long elapsed = 0;
    private long started = NOT_STARTED;

    /**
     * Creates a new {@code Timer} instance using {@link System#currentTimeMillis()} as {@link StopWatch.TimeProvider}
     */
    public StopWatch() {
        this(System::currentTimeMillis);
    }

    /**
     * Creates a new {@code Timer} instance using {@code timeProvider} as {@link StopWatch.TimeProvider}
     * @param timeProvider the timeProvider to use
     */
    public StopWatch(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(PRECISION), e -> {
            update();
        }));
    }

    /**
     * Starts the timer
     */
    public void start() {
        started = timeProvider.getTime();

        timeline.playFromStart();
    }

    /**
     * Stops the timer
     */
    public void stop() {
        timeline.stop();

        elapsed += current();
        started = NOT_STARTED;

        update();
    }

    public void toggle() {
        if (isRunning()) {
            stop();
        } else {
            start();
        }
    }

    public boolean isRunning() {
        return started != NOT_STARTED;
    }

    /**
     * Resets the timer to zero.
     * Keep in mind that your timer will continue to run, if it wasn't stopped with {@link StopWatch#stop()}
     */
    public void reset() {
        setElapsedTime(0);
    }

    public void setElapsedTime(long elapsedTime) {
        elapsed = elapsedTime;
        started = timeProvider.getTime();

        update();
    }

    public long getElapsedTime() {
        update();

        return elapsedTime.get();
    }

    public LongProperty elapsedTimeProperty() {
        return elapsedTime;
    }

    private long current() {
        return isRunning() ? timeProvider.getTime() - started : 0;
    }

    private void update() {
        elapsedTimeProperty().set(elapsed + current());
    }

    @FunctionalInterface
    public interface TimeProvider {
        long getTime();
    }
}
