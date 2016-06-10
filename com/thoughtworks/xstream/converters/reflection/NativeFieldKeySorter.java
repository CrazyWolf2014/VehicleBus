package com.thoughtworks.xstream.converters.reflection;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class NativeFieldKeySorter implements FieldKeySorter {

    /* renamed from: com.thoughtworks.xstream.converters.reflection.NativeFieldKeySorter.1 */
    class C08861 implements Comparator {
        C08861() {
        }

        public int compare(Object o1, Object o2) {
            FieldKey fieldKey1 = (FieldKey) o1;
            FieldKey fieldKey2 = (FieldKey) o2;
            int i = fieldKey1.getDepth() - fieldKey2.getDepth();
            if (i == 0) {
                return fieldKey1.getOrder() - fieldKey2.getOrder();
            }
            return i;
        }
    }

    public Map sort(Class type, Map keyedByFieldKey) {
        Map map = new TreeMap(new C08861());
        map.putAll(keyedByFieldKey);
        return map;
    }
}
