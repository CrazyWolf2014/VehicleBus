package com.thoughtworks.xstream.io;

import java.util.Iterator;

public class AttributeNameIterator implements Iterator {
    private final int count;
    private int current;
    private final HierarchicalStreamReader reader;

    public AttributeNameIterator(HierarchicalStreamReader reader) {
        this.reader = reader;
        this.count = reader.getAttributeCount();
    }

    public boolean hasNext() {
        return this.current < this.count;
    }

    public Object next() {
        HierarchicalStreamReader hierarchicalStreamReader = this.reader;
        int i = this.current;
        this.current = i + 1;
        return hierarchicalStreamReader.getAttributeName(i);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
