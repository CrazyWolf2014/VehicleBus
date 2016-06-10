package com.thoughtworks.xstream.persistence;

import java.util.AbstractList;

public class XmlArrayList extends AbstractList {
    private final XmlMap map;

    public XmlArrayList(PersistenceStrategy persistenceStrategy) {
        this.map = new XmlMap(persistenceStrategy);
    }

    public int size() {
        return this.map.size();
    }

    public Object set(int index, Object element) {
        rangeCheck(index);
        Object value = get(index);
        this.map.put(new Integer(index), element);
        return value;
    }

    public void add(int index, Object element) {
        int size = size();
        if (index >= size + 1 || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int to;
        if (index != size) {
            to = index - 1;
        } else {
            to = index;
        }
        for (int i = size; i > to; i--) {
            this.map.put(new Integer(i + 1), this.map.get(new Integer(i)));
        }
        this.map.put(new Integer(index), element);
    }

    private void rangeCheck(int index) {
        int size = size();
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public Object get(int index) {
        rangeCheck(index);
        return this.map.get(new Integer(index));
    }

    public Object remove(int index) {
        int size = size();
        rangeCheck(index);
        Object value = this.map.get(new Integer(index));
        for (int i = index; i < size - 1; i++) {
            this.map.put(new Integer(i), this.map.get(new Integer(i + 1)));
        }
        this.map.remove(new Integer(size - 1));
        return value;
    }
}
