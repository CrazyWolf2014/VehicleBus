package com.thoughtworks.xstream.core.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ObjectIdDictionary {
    private final Map map;
    private final ReferenceQueue queue;

    private interface Wrapper {
        boolean equals(Object obj);

        Object get();

        int hashCode();

        String toString();
    }

    private static class IdWrapper implements Wrapper {
        private final int hashCode;
        private final Object obj;

        public IdWrapper(Object obj) {
            this.hashCode = System.identityHashCode(obj);
            this.obj = obj;
        }

        public int hashCode() {
            return this.hashCode;
        }

        public boolean equals(Object other) {
            return this.obj == ((Wrapper) other).get();
        }

        public String toString() {
            return this.obj.toString();
        }

        public Object get() {
            return this.obj;
        }
    }

    private class WeakIdWrapper extends WeakReference implements Wrapper {
        private final int hashCode;

        public WeakIdWrapper(Object obj) {
            super(obj, ObjectIdDictionary.this.queue);
            this.hashCode = System.identityHashCode(obj);
        }

        public int hashCode() {
            return this.hashCode;
        }

        public boolean equals(Object other) {
            return get() == ((Wrapper) other).get();
        }

        public String toString() {
            Object obj = get();
            return obj == null ? "(null)" : obj.toString();
        }
    }

    public ObjectIdDictionary() {
        this.map = new HashMap();
        this.queue = new ReferenceQueue();
    }

    public void associateId(Object obj, Object id) {
        this.map.put(new WeakIdWrapper(obj), id);
        cleanup();
    }

    public Object lookupId(Object obj) {
        return this.map.get(new IdWrapper(obj));
    }

    public boolean containsId(Object item) {
        return this.map.containsKey(new IdWrapper(item));
    }

    public void removeId(Object item) {
        this.map.remove(new IdWrapper(item));
        cleanup();
    }

    public int size() {
        cleanup();
        return this.map.size();
    }

    private void cleanup() {
        while (true) {
            WeakIdWrapper wrapper = (WeakIdWrapper) this.queue.poll();
            if (wrapper != null) {
                this.map.remove(wrapper);
            } else {
                return;
            }
        }
    }
}
