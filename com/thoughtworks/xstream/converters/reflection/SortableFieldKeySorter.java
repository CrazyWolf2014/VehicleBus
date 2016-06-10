package com.thoughtworks.xstream.converters.reflection;

import com.thoughtworks.xstream.core.Caching;
import com.thoughtworks.xstream.core.util.OrderRetainingMap;
import com.thoughtworks.xstream.io.StreamException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SortableFieldKeySorter implements FieldKeySorter, Caching {
    private final Map map;

    private class FieldComparator implements Comparator {
        private final String[] fieldOrder;

        public FieldComparator(String[] fields) {
            this.fieldOrder = fields;
        }

        public int compare(String first, String second) {
            int firstPosition = -1;
            int secondPosition = -1;
            for (int i = 0; i < this.fieldOrder.length; i++) {
                if (this.fieldOrder[i].equals(first)) {
                    firstPosition = i;
                }
                if (this.fieldOrder[i].equals(second)) {
                    secondPosition = i;
                }
            }
            if (firstPosition != -1 && secondPosition != -1) {
                return firstPosition - secondPosition;
            }
            throw new StreamException("You have not given XStream a list of all fields to be serialized.");
        }

        public int compare(Object firstObject, Object secondObject) {
            return compare(((FieldKey) firstObject).getFieldName(), ((FieldKey) secondObject).getFieldName());
        }
    }

    public SortableFieldKeySorter() {
        this.map = new HashMap();
    }

    public Map sort(Class type, Map keyedByFieldKey) {
        if (!this.map.containsKey(type)) {
            return keyedByFieldKey;
        }
        Map orderRetainingMap = new OrderRetainingMap();
        FieldKey[] fieldKeys = (FieldKey[]) keyedByFieldKey.keySet().toArray(new FieldKey[keyedByFieldKey.size()]);
        Arrays.sort(fieldKeys, (Comparator) this.map.get(type));
        for (int i = 0; i < fieldKeys.length; i++) {
            orderRetainingMap.put(fieldKeys[i], keyedByFieldKey.get(fieldKeys[i]));
        }
        return orderRetainingMap;
    }

    public void registerFieldOrder(Class type, String[] fields) {
        this.map.put(type, new FieldComparator(fields));
    }

    public void flushCache() {
        this.map.clear();
    }
}
