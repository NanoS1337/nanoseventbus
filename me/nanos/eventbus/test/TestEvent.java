package me.nanos.eventbus.test;

import me.nanos.eventbus.AbstractEvent;

public class TestEvent extends AbstractEvent {
    public final boolean test;

    public TestEvent(boolean test) {
        this.test = test;
    }

    public boolean getTest() {
        return test;
    }
}
