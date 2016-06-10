package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.util.Fields;
import com.thoughtworks.xstream.core.util.HierarchicalStreams;
import com.thoughtworks.xstream.core.util.PresortedMap;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

public class TreeMapConverter extends MapConverter {
    private static final Comparator NULL_MARKER;
    private static final Field comparatorField;

    private static final class NullComparator extends Null implements Comparator {
        private NullComparator() {
        }

        public int compare(Object o1, Object o2) {
            Comparable c2 = (Comparable) o2;
            return ((Comparable) o1).compareTo(o2);
        }
    }

    static {
        NULL_MARKER = new NullComparator();
        comparatorField = Fields.locate(TreeMap.class, Comparator.class, false);
    }

    public TreeMapConverter(Mapper mapper) {
        super(mapper, TreeMap.class);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        marshalComparator(((SortedMap) source).comparator(), writer, context);
        super.marshal(source, writer, context);
    }

    protected void marshalComparator(Comparator comparator, HierarchicalStreamWriter writer, MarshallingContext context) {
        if (comparator != null) {
            writer.startNode("comparator");
            writer.addAttribute(mapper().aliasForSystemAttribute("class"), mapper().serializedClass(comparator.getClass()));
            context.convertAnother(comparator);
            writer.endNode();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        TreeMap result = comparatorField != null ? new TreeMap() : null;
        Comparator comparator = unmarshalComparator(reader, context, result);
        if (result == null) {
            result = comparator == null ? new TreeMap() : new TreeMap(comparator);
        }
        populateTreeMap(reader, context, result, comparator);
        return result;
    }

    protected Comparator unmarshalComparator(HierarchicalStreamReader reader, UnmarshallingContext context, TreeMap result) {
        if (!reader.hasMoreChildren()) {
            return null;
        }
        Comparator comparator;
        reader.moveDown();
        if (reader.getNodeName().equals("comparator")) {
            comparator = (Comparator) context.convertAnother(result, HierarchicalStreams.readClassType(reader, mapper()));
        } else if (!reader.getNodeName().equals("no-comparator")) {
            return NULL_MARKER;
        } else {
            comparator = null;
        }
        reader.moveUp();
        return comparator;
    }

    protected void populateTreeMap(HierarchicalStreamReader reader, UnmarshallingContext context, TreeMap result, Comparator comparator) {
        boolean inFirstElement = comparator == NULL_MARKER;
        if (inFirstElement) {
            comparator = null;
        }
        Comparator comparator2 = (comparator == null || !JVM.hasOptimizedTreeMapPutAll()) ? null : comparator;
        SortedMap sortedMap = new PresortedMap(comparator2);
        if (inFirstElement) {
            putCurrentEntryIntoMap(reader, context, result, sortedMap);
            reader.moveUp();
        }
        populateMap(reader, context, result, sortedMap);
        try {
            if (JVM.hasOptimizedTreeMapPutAll()) {
                if (!(comparator == null || comparatorField == null)) {
                    comparatorField.set(result, comparator);
                }
                result.putAll(sortedMap);
            } else if (comparatorField != null) {
                comparatorField.set(result, sortedMap.comparator());
                result.putAll(sortedMap);
                comparatorField.set(result, comparator);
            } else {
                result.putAll(sortedMap);
            }
        } catch (IllegalAccessException e) {
            throw new ConversionException("Cannot set comparator of TreeMap", e);
        }
    }
}
