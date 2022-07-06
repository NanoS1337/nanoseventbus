package me.nanos.eventbus;

public interface EventListener<AbstractEvent> {
    void fireEvent(AbstractEvent event);
}
