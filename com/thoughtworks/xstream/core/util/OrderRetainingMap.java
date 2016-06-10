package com.thoughtworks.xstream.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class OrderRetainingMap extends HashMap {
    private ArraySet keyOrder;
    private List valueOrder;

    private static class ArraySet extends ArrayList implements Set {
        private ArraySet() {
        }
    }

    public OrderRetainingMap() {
        this.keyOrder = new ArraySet();
        this.valueOrder = new ArrayList();
    }

    public OrderRetainingMap(Map m) {
        this.keyOrder = new ArraySet();
        this.valueOrder = new ArrayList();
        putAll(m);
    }

    public Object put(Object key, Object value) {
        int idx = this.keyOrder.lastIndexOf(key);
        if (idx < 0) {
            this.keyOrder.add(key);
            this.valueOrder.add(value);
        } else {
            this.valueOrder.set(idx, value);
        }
        return super.put(key, value);
    }

    public Object remove(Object key) {
        int idx = this.keyOrder.lastIndexOf(key);
        if (idx != 0) {
            this.keyOrder.remove(idx);
            this.valueOrder.remove(idx);
        }
        return super.remove(key);
    }

    public void clear() {
        this.keyOrder.clear();
        this.valueOrder.clear();
        super.clear();
    }

    public Collection values() {
        return Collections.unmodifiableList(this.valueOrder);
    }

    public Set keySet() {
        return Collections.unmodifiableSet(this.keyOrder);
    }

    public Set entrySet() {
        Entry[] entries = new Entry[size()];
        for (Entry entry : super.entrySet()) {
            entries[this.keyOrder.indexOf(entry.getKey())] = entry;
        }
        Set set = new ArraySet();
        set.addAll(Arrays.asList(entries));
        return Collections.unmodifiableSet(set);
    }
}
