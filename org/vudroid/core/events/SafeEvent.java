package org.vudroid.core.events;

import java.lang.reflect.Method;

public abstract class SafeEvent<T> implements Event<T> {
    private final Class<?> listenerType;

    public abstract void dispatchSafely(T t);

    protected SafeEvent() {
        this.listenerType = getListenerType();
    }

    private Class<?> getListenerType() {
        for (Method method : getClass().getMethods()) {
            if ("dispatchSafely".equals(method.getName()) && !method.isSynthetic()) {
                return method.getParameterTypes()[0];
            }
        }
        throw new RuntimeException("Couldn't find dispatchSafely method");
    }

    public final void dispatchOn(Object listener) {
        if (this.listenerType.isAssignableFrom(listener.getClass())) {
            dispatchSafely(listener);
        }
    }
}
