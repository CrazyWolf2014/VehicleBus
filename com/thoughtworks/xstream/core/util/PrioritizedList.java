package com.thoughtworks.xstream.core.util;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class PrioritizedList {
    private int lastId;
    private int lowestPriority;
    private final Set set;

    private static class PrioritizedItem implements Comparable {
        final int id;
        final int priority;
        final Object value;

        public PrioritizedItem(Object value, int priority, int id) {
            this.value = value;
            this.priority = priority;
            this.id = id;
        }

        public int compareTo(Object o) {
            PrioritizedItem other = (PrioritizedItem) o;
            if (this.priority != other.priority) {
                return other.priority - this.priority;
            }
            return other.id - this.id;
        }

        public boolean equals(Object obj) {
            return this.id == ((PrioritizedItem) obj).id;
        }
    }

    private static class PrioritizedItemIterator implements Iterator {
        private Iterator iterator;

        public PrioritizedItemIterator(Iterator iterator) {
            this.iterator = iterator;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public Object next() {
            return ((PrioritizedItem) this.iterator.next()).value;
        }
    }

    public PrioritizedList() {
        this.set = new TreeSet();
        this.lowestPriority = Integer.MAX_VALUE;
        this.lastId = 0;
    }

    public void add(Object item, int priority) {
        if (this.lowestPriority > priority) {
            this.lowestPriority = priority;
        }
        Set set = this.set;
        int i = this.lastId + 1;
        this.lastId = i;
        set.add(new PrioritizedItem(item, priority, i));
    }

    public Iterator iterator() {
        return new PrioritizedItemIterator(this.set.iterator());
    }
}
