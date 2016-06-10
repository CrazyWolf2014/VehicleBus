package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.util.Fields;
import com.thoughtworks.xstream.core.util.PresortedSet;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class TreeSetConverter extends CollectionConverter {
    private static final Field sortedMapField;
    private transient TreeMapConverter treeMapConverter;

    /* renamed from: com.thoughtworks.xstream.converters.collections.TreeSetConverter.1 */
    class C13041 extends TreeMapConverter {

        /* renamed from: com.thoughtworks.xstream.converters.collections.TreeSetConverter.1.1 */
        class C08801 extends AbstractList {
            final /* synthetic */ Map val$target;

            C08801(Map map) {
                this.val$target = map;
            }

            public boolean add(Object object) {
                return this.val$target.put(object, object) != null;
            }

            public Object get(int location) {
                return null;
            }

            public int size() {
                return this.val$target.size();
            }
        }

        C13041(Mapper x0) {
            super(x0);
        }

        protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map, Map target) {
            TreeSetConverter.this.populateCollection(reader, context, new C08801(target));
        }

        protected void putCurrentEntryIntoMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map, Map target) {
            Object key = readItem(reader, context, map);
            target.put(key, key);
        }
    }

    static {
        sortedMapField = JVM.hasOptimizedTreeSetAddAll() ? Fields.locate(TreeSet.class, SortedMap.class, false) : null;
    }

    public TreeSetConverter(Mapper mapper) {
        super(mapper, TreeSet.class);
        readResolve();
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        this.treeMapConverter.marshalComparator(((SortedSet) source).comparator(), writer, context);
        super.marshal(source, writer, context);
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        TreeMap treeMap;
        Comparator comparator = null;
        TreeSet result = null;
        Comparator unmarshalledComparator = this.treeMapConverter.unmarshalComparator(reader, context, null);
        boolean inFirstElement = unmarshalledComparator instanceof Null;
        if (!inFirstElement) {
            comparator = unmarshalledComparator;
        }
        if (sortedMapField != null) {
            TreeSet possibleResult = comparator == null ? new TreeSet() : new TreeSet(comparator);
            try {
                TreeMap backingMap = sortedMapField.get(possibleResult);
                if (backingMap instanceof TreeMap) {
                    treeMap = backingMap;
                    result = possibleResult;
                } else {
                    treeMap = null;
                }
            } catch (IllegalAccessException e) {
                throw new ConversionException("Cannot get backing map of TreeSet", e);
            }
        }
        treeMap = null;
        if (treeMap == null) {
            PresortedSet set = new PresortedSet(comparator);
            result = comparator == null ? new TreeSet() : new TreeSet(comparator);
            if (inFirstElement) {
                addCurrentElementToCollection(reader, context, result, set);
                reader.moveUp();
            }
            populateCollection(reader, context, result, set);
            if (set.size() > 0) {
                result.addAll(set);
            }
        } else {
            this.treeMapConverter.populateTreeMap(reader, context, treeMap, unmarshalledComparator);
        }
        return result;
    }

    private Object readResolve() {
        this.treeMapConverter = new C13041(mapper());
        return this;
    }
}
