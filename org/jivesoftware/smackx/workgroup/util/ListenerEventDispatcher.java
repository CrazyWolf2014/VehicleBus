package org.jivesoftware.smackx.workgroup.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.ListIterator;

public class ListenerEventDispatcher implements Runnable {
    protected transient boolean hasFinishedDispatching;
    protected transient boolean isRunning;
    protected transient ArrayList<TripletContainer> triplets;

    protected class TripletContainer {
        protected Object listenerInstance;
        protected Method listenerMethod;
        protected Object[] methodArguments;

        protected TripletContainer(Object obj, Method method, Object[] objArr) {
            this.listenerInstance = obj;
            this.listenerMethod = method;
            this.methodArguments = objArr;
        }

        protected Object getListenerInstance() {
            return this.listenerInstance;
        }

        protected Method getListenerMethod() {
            return this.listenerMethod;
        }

        protected Object[] getMethodArguments() {
            return this.methodArguments;
        }
    }

    public ListenerEventDispatcher() {
        this.triplets = new ArrayList();
        this.hasFinishedDispatching = false;
        this.isRunning = false;
    }

    public void addListenerTriplet(Object obj, Method method, Object[] objArr) {
        if (!this.isRunning) {
            this.triplets.add(new TripletContainer(obj, method, objArr));
        }
    }

    public boolean hasFinished() {
        return this.hasFinishedDispatching;
    }

    public void run() {
        this.isRunning = true;
        ListIterator listIterator = this.triplets.listIterator();
        while (listIterator.hasNext()) {
            TripletContainer tripletContainer = (TripletContainer) listIterator.next();
            try {
                tripletContainer.getListenerMethod().invoke(tripletContainer.getListenerInstance(), tripletContainer.getMethodArguments());
            } catch (Exception e) {
                System.err.println("Exception dispatching an event: " + e);
                e.printStackTrace();
            }
        }
        this.hasFinishedDispatching = true;
    }
}
