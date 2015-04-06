package org.obehave.util;

public class OneTimeAction implements Runnable {
    private boolean executed = false;
    private final Runnable action;

    public OneTimeAction(Runnable action) {
        this.action = action;
    }

    public boolean execute() {
        if (!executed) {
            action.run();

            return (executed = true);
        } else {
            return false;
        }
    }

    @Override
    public void run() {
        execute();
    }
}
