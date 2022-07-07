package me.nanos.eventbus;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum EventBus {
    INSTANCE;

    public final Map<Object, Map<Field, Class<? extends AbstractEvent>>> listeners = new HashMap<>();

    public void post(AbstractEvent AbstractEvent) {
        getListeners(AbstractEvent).forEach(listener -> listener.fireEvent(AbstractEvent));
    }

    public List<EventListener<AbstractEvent>> getListeners(AbstractEvent AbstractEvent) {
        Map<EventListener<AbstractEvent>, Integer> temp = new HashMap<>();

        listeners.forEach((listener, fields) -> fields.forEach((field, AbstractEventClass) -> {
            if(AbstractEvent.getClass().isAssignableFrom(AbstractEventClass)) {
                try {
                    EventListener<AbstractEvent> eventListener = (EventListener<AbstractEvent>) field.get(listener);
                    temp.put(eventListener, field.getAnnotation(ClientEvent.class).priority());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));

        return temp.keySet().stream().sorted(Comparator.comparingInt(temp::get).reversed()).collect(Collectors.toList());
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
