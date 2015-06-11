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

    /**
     * Needed to periodically call update
     */
    private final Timeline timeline;

    /**
     * A simple interface to get the current time
     */
    private final TimeProvider timeProvider;

    /**
     * The time that has been measured with this stop watch. Excluding the result of current()
     */
    private final DoubleProperty elapsedTime = new SimpleDoubleProperty(this, "elapsedTime", 0);
    private final DoubleProperty rate = new SimpleDoubleProperty(this, "rate", 1);
    /**
     * Stores the timeline when the StopWatch was last started
     */
    private double started = NOT_STARTED;

    /**
     * Creates a new {@code Timer} instance using {@link System#nanoTime()}} as {@link StopWatch.TimeProvider}
     */
    public StopWatch() {
        this(() -> System.nanoTime() / 1000000);
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

        rate.addListener((observable, oldRate, newRate) -> update(oldRate.doubleValue()));
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

        update();

        started = NOT_STARTED;
    }

    public void toggle() {
        if (isRunning()) {
            stop();
        } else {
            start();
        }
    }

    /**
     * Returns true if the StopWatch is currently running
     * @return true if the StopWatch is running
     */
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
        elapsedTimeProperty().setValue(elapsedTime);

        update();
    }

    public double getElapsedTime() {
        update();

        return elapsedTime.get();
    }

    public DoubleProperty elapsedTimeProperty() {
        return elapsedTime;
    }

    public double getRate() {
        return rate.get();
    }

    public DoubleProperty rateProperty() {
        return rate;
    }

    /**
     * Sets the rate of the current timer. Running for 2 seconds with a rate of 2 means that the time will add 4 seconds
     * @param rate the rate to set
     */
    public void setRate(double rate) {
        this.rate.set(rate);
    }

    private double current() {
        return current(rate.get());
    }

    private double current(double rate) {
        return isRunning() ? (timeProvider.getTime() - started) * rate : 0;
    }

    private void update() {
        update(rate.get());
    }

    private void update(double rate) {
        elapsedTimeProperty().set(elapsedTimeProperty().get() + current(rate));

        started = timeProvider.getTime();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Elapsed: ").append(getElapsedTime());
        if (isRunning()) {
            sb.append(", Current: ").append(current()).append(" (with rate: ").append(rate).append(")");
        }
        sb.append(" (using time provider: ").append(timeProvider).append(")");
        return sb.toString();
    }

    @FunctionalInterface
    public interface TimeProvider {
        long getTime();
    }
}
