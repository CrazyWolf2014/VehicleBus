package com.thoughtworks.xstream.converters.reflection;

import com.thoughtworks.xstream.core.Caching;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.util.OrderRetainingMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FieldDictionary implements Caching {
    private transient Map keyedByFieldKeyCache;
    private transient Map keyedByFieldNameCache;
    private final FieldKeySorter sorter;

    public FieldDictionary() {
        this(new ImmutableFieldKeySorter());
    }

    public FieldDictionary(FieldKeySorter sorter) {
        this.sorter = sorter;
        init();
    }

    private void init() {
        this.keyedByFieldNameCache = new HashMap();
        this.keyedByFieldKeyCache = new HashMap();
        this.keyedByFieldNameCache.put(Object.class, Collections.EMPTY_MAP);
        this.keyedByFieldKeyCache.put(Object.class, Collections.EMPTY_MAP);
    }

    public Iterator serializableFieldsFor(Class cls) {
        return fieldsFor(cls);
    }

    public Iterator fieldsFor(Class cls) {
        return buildMap(cls, true).values().iterator();
    }

    public Field field(Class cls, String name, Class definedIn) {
        Field field = fieldOrNull(cls, name, definedIn);
        if (field != null) {
            return field;
        }
        throw new MissingFieldException(cls.getName(), name);
    }

    public Field fieldOrNull(Class cls, String name, Class definedIn) {
        Map fields = buildMap(cls, definedIn != null);
        if (definedIn != null) {
            name = new FieldKey(name, definedIn, -1);
        }
        return (Field) fields.get(name);
    }

    private Map buildMap(Class type, boolean tupleKeyed) {
        Class cls = type;
        synchronized (this) {
            if (this.keyedByFieldNameCache.containsKey(type)) {
                Object obj;
                if (tupleKeyed) {
                    obj = this.keyedByFieldKeyCache.get(type);
                } else {
                    obj = this.keyedByFieldNameCache.get(type);
                }
                return (Map) obj;
            }
            Map map;
            List<Class> superClasses = new ArrayList();
            while (true) {
                if (Object.class.equals(cls)) {
                    break;
                }
                superClasses.add(0, cls);
                cls = cls.getSuperclass();
            }
            Map lastKeyedByFieldName = Collections.EMPTY_MAP;
            Map lastKeyedByFieldKey = Collections.EMPTY_MAP;
            for (Class cls2 : superClasses) {
                if (this.keyedByFieldNameCache.containsKey(cls2)) {
                    lastKeyedByFieldName = (Map) this.keyedByFieldNameCache.get(cls2);
                    lastKeyedByFieldKey = (Map) this.keyedByFieldKeyCache.get(cls2);
                } else {
                    int i;
                    Field field;
                    Map keyedByFieldName = new HashMap(lastKeyedByFieldName);
                    Map keyedByFieldKey = new OrderRetainingMap(lastKeyedByFieldKey);
                    Field[] fields = cls2.getDeclaredFields();
                    if (JVM.reverseFieldDefinition()) {
                        int i2 = fields.length >> 1;
                        while (true) {
                            i = i2 - 1;
                            if (i2 <= 0) {
                                break;
                            }
                            int idx = (fields.length - i) - 1;
                            field = fields[i];
                            fields[i] = fields[idx];
                            fields[idx] = field;
                            i2 = i;
                        }
                    }
                    i = 0;
                    while (true) {
                        int length = fields.length;
                        if (i >= r0) {
                            break;
                        }
                        field = fields[i];
                        if (!field.isAccessible()) {
                            field.setAccessible(true);
                        }
                        FieldKey fieldKey = new FieldKey(field.getName(), field.getDeclaringClass(), i);
                        Field existent = (Field) keyedByFieldName.get(field.getName());
                        if (existent == null || (existent.getModifiers() & 8) != 0 || (existent != null && (field.getModifiers() & 8) == 0)) {
                            keyedByFieldName.put(field.getName(), field);
                        }
                        keyedByFieldKey.put(fieldKey, field);
                        i++;
                    }
                    Map sortedFieldKeys = this.sorter.sort(cls2, keyedByFieldKey);
                    this.keyedByFieldNameCache.put(cls2, keyedByFieldName);
                    this.keyedByFieldKeyCache.put(cls2, sortedFieldKeys);
                    lastKeyedByFieldName = keyedByFieldName;
                    lastKeyedByFieldKey = sortedFieldKeys;
                }
            }
            if (tupleKeyed) {
                map = lastKeyedByFieldKey;
            } else {
                map = lastKeyedByFieldName;
            }
            return map;
        }
    }

    public synchronized void flushCache() {
        Set objectTypeSet = Collections.singleton(Object.class);
        this.keyedByFieldNameCache.keySet().retainAll(objectTypeSet);
        this.keyedByFieldKeyCache.keySet().retainAll(objectTypeSet);
        if (this.sorter instanceof Caching) {
            ((Caching) this.sorter).flushCache();
        }
    }

    protected Object readResolve() {
        init();
        return this;
    }
}
