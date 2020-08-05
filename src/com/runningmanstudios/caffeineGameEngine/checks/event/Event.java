package com.runningmanstudios.caffeineGameEngine.checks.event;

import java.io.Serializable;

/**
 * Event Class. extend to make an Event.
 * Classes that don't extend Event and EventBuilder are not Events.
 */
public abstract class Event implements Serializable {
    boolean canceled = false;

    public void cancel() {
        this.canceled = true;
    }

    public boolean isCancelled() {
        return this.canceled;
    }

    public abstract <T> T getTarget();
}
