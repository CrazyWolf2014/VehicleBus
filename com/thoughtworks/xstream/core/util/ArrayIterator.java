package com.thoughtworks.xstream.core.util;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayIterator implements Iterator {
    private final Object array;
    private int idx;
    private int length;

    public ArrayIterator(Object array) {
        this.array = array;
        this.length = Array.getLength(array);
    }

    public boolean hasNext() {
        return this.idx < this.length;
    }

    public Object next() {
        Object obj = this.array;
        int i = this.idx;
        this.idx = i + 1;
        return Array.get(obj, i);
    }

    public void remove() {
        throw new UnsupportedOperationException("Remove from array");
    }
}
