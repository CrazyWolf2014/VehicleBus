package com.ifoer.event;

import java.util.Iterator;
import java.util.LinkedList;

public class ScrollEventAdapter {
    LinkedList<OnScrollCompleteListener> listeners;

    public ScrollEventAdapter() {
        this.listeners = new LinkedList();
    }

    public void notifyEvent(ScrollEvent e) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((OnScrollCompleteListener) it.next()).onScrollComplete(e);
        }
    }

    public void addListener(OnScrollCompleteListener l) {
        this.listeners.add(l);
    }
}
