package org.vudroid.core.events;

import java.util.ArrayList;
import java.util.Iterator;

public class EventDispatcher {
    private final ArrayList<Object> listeners;

    public EventDispatcher() {
        this.listeners = new ArrayList();
    }

    public void dispatch(Event event) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            event.dispatchOn(it.next());
        }
    }

    public void addEventListener(Object listener) {
        this.listeners.add(listener);
    }

    public void removeEventListener(Object listener) {
        this.listeners.remove(listener);
    }
}
