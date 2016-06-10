package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ReferenceByXPathMarshallingStrategy extends AbstractTreeMarshallingStrategy {
    public static int ABSOLUTE;
    public static int RELATIVE;
    public static int SINGLE_NODE;
    private final int mode;

    static {
        RELATIVE = 0;
        ABSOLUTE = 1;
        SINGLE_NODE = 2;
    }

    public ReferenceByXPathMarshallingStrategy(int mode) {
        this.mode = mode;
    }

    protected TreeUnmarshaller createUnmarshallingContext(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
        return new ReferenceByXPathUnmarshaller(root, reader, converterLookup, mapper);
    }

    protected TreeMarshaller createMarshallingContext(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
        return new ReferenceByXPathMarshaller(writer, converterLookup, mapper, this.mode);
    }
}
