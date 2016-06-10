package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

public class CollectionConverter extends AbstractCollectionConverter {
    private final Class type;

    public CollectionConverter(Mapper mapper) {
        this(mapper, null);
    }

    public CollectionConverter(Mapper mapper, Class type) {
        super(mapper);
        this.type = type;
        if (type != null && !Collection.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(type + " not of type " + Collection.class);
        }
    }

    public boolean canConvert(Class type) {
        if (this.type != null) {
            return type.equals(this.type);
        }
        return type.equals(ArrayList.class) || type.equals(HashSet.class) || type.equals(LinkedList.class) || type.equals(Vector.class) || (JVM.is14() && type.getName().equals("java.util.LinkedHashSet"));
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        for (Object item : (Collection) source) {
            writeItem(item, context, writer);
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Collection collection = (Collection) createCollection(context.getRequiredType());
        populateCollection(reader, context, collection);
        return collection;
    }

    protected void populateCollection(HierarchicalStreamReader reader, UnmarshallingContext context, Collection collection) {
        populateCollection(reader, context, collection, collection);
    }

    protected void populateCollection(HierarchicalStreamReader reader, UnmarshallingContext context, Collection collection, Collection target) {
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            addCurrentElementToCollection(reader, context, collection, target);
            reader.moveUp();
        }
    }

    protected void addCurrentElementToCollection(HierarchicalStreamReader reader, UnmarshallingContext context, Collection collection, Collection target) {
        target.add(readItem(reader, context, collection));
    }

    protected Object createCollection(Class type) {
        if (this.type != null) {
            type = this.type;
        }
        return super.createCollection(type);
    }
}
