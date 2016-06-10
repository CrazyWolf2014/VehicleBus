package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.util.HierarchicalStreams;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.Mapper.Null;

public abstract class AbstractCollectionConverter implements Converter {
    private final Mapper mapper;

    public abstract boolean canConvert(Class cls);

    public abstract void marshal(Object obj, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext);

    public abstract Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext);

    public AbstractCollectionConverter(Mapper mapper) {
        this.mapper = mapper;
    }

    protected Mapper mapper() {
        return this.mapper;
    }

    protected void writeItem(Object item, MarshallingContext context, HierarchicalStreamWriter writer) {
        if (item == null) {
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, mapper().serializedClass(null), Null.class);
            writer.endNode();
            return;
        }
        ExtendedHierarchicalStreamWriterHelper.startNode(writer, mapper().serializedClass(item.getClass()), item.getClass());
        context.convertAnother(item);
        writer.endNode();
    }

    protected Object readItem(HierarchicalStreamReader reader, UnmarshallingContext context, Object current) {
        return context.convertAnother(current, HierarchicalStreams.readClassType(reader, mapper()));
    }

    protected Object createCollection(Class type) {
        Class defaultType = mapper().defaultImplementationOf(type);
        try {
            return defaultType.newInstance();
        } catch (InstantiationException e) {
            throw new ConversionException("Cannot instantiate " + defaultType.getName(), e);
        } catch (IllegalAccessException e2) {
            throw new ConversionException("Cannot instantiate " + defaultType.getName(), e2);
        }
    }
}
