package com.thoughtworks.xstream.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

public class PresortedMap implements SortedMap {
    private final Comparator comparator;
    private final ArraySet set;

    /* renamed from: com.thoughtworks.xstream.core.util.PresortedMap.1 */
    class C08961 implements Entry {
        final /* synthetic */ Object val$key;
        final /* synthetic */ Object val$value;

        C08961(Object obj, Object obj2) {
            this.val$key = obj;
            this.val$value = obj2;
        }

        public Object getKey() {
            return this.val$key;
        }

        public Object getValue() {
            return this.val$value;
        }

        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }
    }

    private static class ArraySet extends ArrayList implements Set {
        private ArraySet() {
        }
    }

    private static class ArraySetComparator implements Comparator {
        private Entry[] array;
        private final ArrayList list;

        ArraySetComparator(ArrayList list) {
            this.list = list;
        }

        public int compare(Object object1, Object object2) {
            int i = 0;
            if (this.array == null || this.list.size() != this.array.length) {
                Entry[] a = new Entry[this.list.size()];
                if (this.array != null) {
                    System.arraycopy(this.array, 0, a, 0, this.array.length);
                }
                if (this.array != null) {
                    i = this.array.length;
                }
                while (i < this.list.size()) {
                    a[i] = (Entry) this.list.get(i);
                    i++;
                }
                this.array = a;
            }
            int idx1 = Integer.MAX_VALUE;
            int idx2 = Integer.MAX_VALUE;
            i = 0;
            while (i < this.array.length && (idx1 >= Integer.MAX_VALUE || idx2 >= Integer.MAX_VALUE)) {
                if (idx1 == Integer.MAX_VALUE && object1 == this.array[i].getKey()) {
                    idx1 = i;
                }
                if (idx2 == Integer.MAX_VALUE && object2 == this.array[i].getKey()) {
                    idx2 = i;
                }
                i++;
            }
            return idx1 - idx2;
        }
    }

    public PresortedMap() {
        this(null, new ArraySet());
    }

    public PresortedMap(Comparator comparator) {
        this(comparator, new ArraySet());
    }

    private PresortedMap(Comparator comparator, ArraySet set) {
        if (comparator == null) {
            comparator = new ArraySetComparator(set);
        }
        this.comparator = comparator;
        this.set = set;
    }

    public Comparator comparator() {
        return this.comparator;
    }

    public Set entrySet() {
        return this.set;
    }

    public Object firstKey() {
        throw new UnsupportedOperationException();
    }

    public SortedMap headMap(Object toKey) {
        throw new UnsupportedOperationException();
    }

    public Set keySet() {
        Set keySet = new ArraySet();
        Iterator iterator = this.set.iterator();
        while (iterator.hasNext()) {
            keySet.add(((Entry) iterator.next()).getKey());
        }
        return keySet;
    }

    public Object lastKey() {
        throw new UnsupportedOperationException();
    }

    public SortedMap subMap(Object fromKey, Object toKey) {
        throw new UnsupportedOperationException();
    }

    public SortedMap tailMap(Object fromKey) {
        throw new UnsupportedOperationException();
    }

    public Collection values() {
        Set values = new ArraySet();
        Iterator iterator = this.set.iterator();
        while (iterator.hasNext()) {
            values.add(((Entry) iterator.next()).getValue());
        }
        return values;
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    public Object put(Object key, Object value) {
        this.set.add(new C08961(key, value));
        return null;
    }

    public void putAll(Map m) {
        for (Object add : m.entrySet()) {
            this.set.add(add);
        }
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public int size() {
        return this.set.size();
    }
}
