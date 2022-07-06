package me.nanos.eventbus;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public enum EventBus {
    INSTANCE;

    public final Map<Object, Map<Field, Class<? extends AbstractEvent>>> listeners = new HashMap<>();

    public void post(AbstractEvent event) {
        listeners.forEach((listener, fields) -> {
            fields.forEach((field, eventClass) -> {
                if(event.getClass().isAssignableFrom(eventClass)) {
                    try {
                        EventListener eventListener = (EventListener) field.get(listener);
                        eventListener.fireEvent(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });
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
}
