package com.thoughtworks.xstream;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public interface MarshallingStrategy {
    void marshal(HierarchicalStreamWriter hierarchicalStreamWriter, Object obj, ConverterLookup converterLookup, Mapper mapper, DataHolder dataHolder);

    Object unmarshal(Object obj, HierarchicalStreamReader hierarchicalStreamReader, DataHolder dataHolder, ConverterLookup converterLookup, Mapper mapper);
}
