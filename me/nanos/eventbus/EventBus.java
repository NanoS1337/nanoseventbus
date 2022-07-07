package me.nanos.eventbus;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public enum EventBus {
    INSTANCE;

    public final Map<Object, Map<Field, Class<? extends AbstractEvent>>> listeners = new HashMap<>();

    public void post(AbstractEvent event) {
        getListeners(event).forEach(listener -> listener.fireEvent(event));
    }

    public List<EventListener<AbstractEvent>> getListeners(AbstractEvent event) {
        Map<EventPriority, EventListener<AbstractEvent>> temp = new HashMap<>();

        listeners.forEach((listener, fields) -> {
            fields.forEach((field, eventClass) -> {
                if(event.getClass().isAssignableFrom(eventClass)) {
                    try {
                        EventListener<AbstractEvent> eventListener = (EventListener<AbstractEvent>) field.get(listener);
                        temp.put(field.getAnnotation(ClientEvent.class).priority(), eventListener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });

        return temp.keySet().stream().sorted(Comparator.comparingInt(EventPriority::getComparator).reversed()).map(temp::get).collect(Collectors.toList());
    }

    public void addListener(Object listener) {
        if(listeners.containsKey(listener))
            return;

        Class<?> listenerClass = listener.getClass();
        Map<Field, Class<? extends AbstractEvent>> events = new HashMap<>();

        for (Field field : listenerClass.getFields()) {
            if (field.isAnnotationPresent(ClientEvent.class)) {
                if(field.getType().isAssignableFrom(EventListener.class)) {
                    try {
                        EventListener eventListener = (EventListener) field.get(listener);
                        events.put(field, (Class<? extends AbstractEvent>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        listeners.put(listener, events);
    }

    public void removeListener(Object listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public boolean isRegistered(Object listener) {
        return listeners.containsKey(listener);
    }
}
