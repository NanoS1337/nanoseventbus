package me.nanos.eventbus.test;

import me.nanos.eventbus.Listen;

public class TestListener {
    @Listen(TestEvent.class)
    public void onTestEvent(TestEvent event) {
        if(event.isCancelled()) {
            System.out.println("Event was cancelled! Received at time: " + System.nanoTime());
            return;
        }

        System.out.println("Received event at nano time: " + System.nanoTime());
        System.out.println("Data from event: " + event.getTest());
        System.out.println("End of listener");
    }
}
