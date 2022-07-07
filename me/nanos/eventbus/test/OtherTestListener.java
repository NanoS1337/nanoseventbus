package me.nanos.eventbus.test;

import me.nanos.eventbus.ClientEvent;
import me.nanos.eventbus.EventListener;

public class OtherTestListener {
    @ClientEvent
    public EventListener<TestEvent> onTestEvent = event -> {
        System.out.println("OH YEAH! This came second!");
    };
}
