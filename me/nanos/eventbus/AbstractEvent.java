package me.nanos.eventbus;

import me.nanos.eventbus.test.TestEvent;

public class AbstractEvent {
    public boolean cancelled = false;

    public void cancel() {
        cancelled = true;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
