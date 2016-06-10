package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public class MapConverter extends AbstractCollectionConverter {
    private final Class type;

    public MapConverter(Mapper mapper) {
        this(mapper, null);
    }

    public MapConverter(Mapper mapper, Class type) {
        super(mapper);
        this.type = type;
        if (type != null && !Map.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(type + " not of type " + Map.class);
        }
    }

    public boolean canConvert(Class type) {
        if (this.type != null) {
            return type.equals(this.type);
        }
        return type.equals(HashMap.class) || type.equals(Hashtable.class) || type.getName().equals("java.util.LinkedHashMap") || type.getName().equals("java.util.concurrent.ConcurrentHashMap") || type.getName().equals("sun.font.AttributeMap");
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Map map = (Map) source;
        String entryName = mapper().serializedClass(Entry.class);
        for (Entry entry : map.entrySet()) {
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, entryName, entry.getClass());
            writeItem(entry.getKey(), context, writer);
            writeItem(entry.getValue(), context, writer);
            writer.endNode();
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Map map = (Map) createCollection(context.getRequiredType());
        populateMap(reader, context, map);
        return map;
    }

    protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map) {
        populateMap(reader, context, map, map);
    }

    protected void populateMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map, Map target) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            putCurrentEntryIntoMap(reader, context, map, target);
            reader.moveUp();
        }
    }

    protected void putCurrentEntryIntoMap(HierarchicalStreamReader reader, UnmarshallingContext context, Map map, Map target) {
        reader.moveDown();
        Object key = readItem(reader, context, map);
        reader.moveUp();
        reader.moveDown();
        Object value = readItem(reader, context, map);
        reader.moveUp();
        target.put(key, value);
    }

    protected Object createCollection(Class type) {
        if (this.type != null) {
            type = this.type;
        }
        return super.createCollection(type);
    }
}
