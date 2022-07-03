package me.nanos.eventbus.test;

import me.nanos.eventbus.EventBus;

import java.util.Random;

public class Test {
    public static void main(String[] args) {
        TestListener testListener = new TestListener();

        EventBus.INSTANCE.addListener(testListener);

        System.out.println("Started posting events...");
        for(int i = 0; i<100; i++) {
            System.out.println("Posting event! System time in nanos: " + System.nanoTime());
            EventBus.INSTANCE.post(new TestEvent(new Random().nextBoolean()));
        }

        System.out.println("Finished posting events!");
        EventBus.INSTANCE.removeListener(testListener);

        System.out.println("Listener size: " + EventBus.INSTANCE.listeners.size());

        if(EventBus.INSTANCE.listeners.size() > 0)
            System.err.println("Failed test. Debug and make sure all listeners are being removed.");
        else System.out.println("Passed test! All listeners removed as intended.");
    }
}
