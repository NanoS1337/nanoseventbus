package me.nanos.eventbus.test;

import me.nanos.eventbus.ClientEvent;
import me.nanos.eventbus.EventListener;
import me.nanos.eventbus.EventPriority;

public class TestListener {
    @ClientEvent(priority = EventPriority.HIGH)
    public EventListener<TestEvent> onTestEvent = event -> {
        if(event.isCancelled()) {
            System.out.println("Event was cancelled! Received at time: " + System.nanoTime());
            return;
        }

        System.out.println("YAY! This came first!");

        System.out.println("Received event at nano time: " + System.nanoTime());
        System.out.println("Data from event: " + event.getTest());
        System.out.println("End of listener");
    };
}
