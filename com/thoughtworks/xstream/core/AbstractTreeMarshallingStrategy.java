package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.MarshallingStrategy;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public abstract class AbstractTreeMarshallingStrategy implements MarshallingStrategy {
    protected abstract TreeMarshaller createMarshallingContext(HierarchicalStreamWriter hierarchicalStreamWriter, ConverterLookup converterLookup, Mapper mapper);

    protected abstract TreeUnmarshaller createUnmarshallingContext(Object obj, HierarchicalStreamReader hierarchicalStreamReader, ConverterLookup converterLookup, Mapper mapper);

    public Object unmarshal(Object root, HierarchicalStreamReader reader, DataHolder dataHolder, ConverterLookup converterLookup, Mapper mapper) {
        return createUnmarshallingContext(root, reader, converterLookup, mapper).start(dataHolder);
    }

    public void marshal(HierarchicalStreamWriter writer, Object obj, ConverterLookup converterLookup, Mapper mapper, DataHolder dataHolder) {
        createMarshallingContext(writer, converterLookup, mapper).start(obj, dataHolder);
    }
}
