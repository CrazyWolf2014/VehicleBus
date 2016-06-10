package com.thoughtworks.xstream.converters;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public interface Converter extends ConverterMatcher {
    void marshal(Object obj, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext);

    Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext);
}
