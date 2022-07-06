package me.nanos.eventbus;

import java.lang.reflect.Method;
import java.util.*;

public enum EventBus {
    INSTANCE;

    public final HashMap<Object, Map<Method, Class<? extends AbstractEvent>>> listeners = new HashMap<>();

    public void post(AbstractEvent event) {
        listeners.forEach((listener, methods) -> {
            methods.forEach((method, eventClass) -> {
                if(event.getClass().isAssignableFrom(eventClass)) {
                    try {
                        method.invoke(listener, event);
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
        Map<Method, Class<? extends AbstractEvent>> methods = new HashMap<>();

        for (Method method : listenerClass.getMethods()) {
            if (method.isAnnotationPresent(ClientEvent.class)) {
                if (method.getParameterCount() == 1 && AbstractEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    methods.put(method, (Class<? extends AbstractEvent>) method.getParameterTypes()[0]);
                }
            }
        }

        listeners.put(listener, methods);
    }

    public void removeListener(Object listener) {
        listeners.remove(listener);
    }
}
