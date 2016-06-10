package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayConverter extends AbstractCollectionConverter {
    public ArrayConverter(Mapper mapper) {
        super(mapper);
    }

    public boolean canConvert(Class type) {
        return type.isArray();
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        int length = Array.getLength(source);
        for (int i = 0; i < length; i++) {
            writeItem(Array.get(source, i), context, writer);
        }
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        List<Object> items = new ArrayList();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            items.add(readItem(reader, context, null));
            reader.moveUp();
        }
        Object array = Array.newInstance(context.getRequiredType().getComponentType(), items.size());
        int i = 0;
        for (Object obj : items) {
            int i2 = i + 1;
            Array.set(array, i, obj);
            i = i2;
        }
        return array;
    }
}
