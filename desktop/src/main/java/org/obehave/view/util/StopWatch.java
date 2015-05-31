package org.obehave.view.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class StopWatch {
    /**
     * After how many ms should the stopwatch update?
     */
    private static final double PRECISION = 100;

    private static final long NOT_STARTED = -1;

    private final Timeline timeline;
    private final TimeProvider timeProvider;

    private final DoubleProperty elapsedTime = new SimpleDoubleProperty(this, "elapsedTime", 0);
    private final DoubleProperty rate = new SimpleDoubleProperty(this, "rate", 1);

    private double elapsed = 0;
    private double started = NOT_STARTED;

    /**
     * Creates a new {@code Timer} instance using {@link System#currentTimeMillis()} as {@link StopWatch.TimeProvider}
     */
    public StopWatch() {
        this(System::currentTimeMillis);
    }

    /**
     * Creates a new {@code Timer} instance using {@code timeProvider} as {@link StopWatch.TimeProvider}
     *
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

    public void setElapsedTime(double elapsedTime) {
        elapsed = elapsedTime;
        started = timeProvider.getTime();

        update();
    }

    public double getElapsedTime() {
        update();

        return elapsedTime.get();
    }

    public DoubleProperty elapsedTimeProperty() {
        return elapsedTime;
    }



    private double current() {
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
