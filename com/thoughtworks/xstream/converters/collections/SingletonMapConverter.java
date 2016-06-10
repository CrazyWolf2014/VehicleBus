package com.thoughtworks.xstream.converters.collections;

import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.Collections;

public class SingletonMapConverter extends MapConverter {
    private static final Class MAP;

    static {
        MAP = Collections.singletonMap(Boolean.TRUE, null).getClass();
    }

    public SingletonMapConverter(Mapper mapper) {
        super(mapper);
    }

    public boolean canConvert(Class type) {
        return MAP == type;
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        reader.moveDown();
        reader.moveDown();
        Object key = readItem(reader, context, null);
        reader.moveUp();
        reader.moveDown();
        Object value = readItem(reader, context, null);
        reader.moveUp();
        reader.moveUp();
        return Collections.singletonMap(key, value);
    }
}
