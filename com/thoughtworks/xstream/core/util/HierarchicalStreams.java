package com.thoughtworks.xstream.core.util;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

public class HierarchicalStreams {
    public static Class readClassType(HierarchicalStreamReader reader, Mapper mapper) {
        String classAttribute = readClassAttribute(reader, mapper);
        if (classAttribute == null) {
            return mapper.realClass(reader.getNodeName());
        }
        return mapper.realClass(classAttribute);
    }

    public static String readClassAttribute(HierarchicalStreamReader reader, Mapper mapper) {
        String attributeName = mapper.aliasForSystemAttribute("resolves-to");
        String classAttribute = attributeName == null ? null : reader.getAttribute(attributeName);
        if (classAttribute != null) {
            return classAttribute;
        }
        attributeName = mapper.aliasForSystemAttribute("class");
        if (attributeName != null) {
            return reader.getAttribute(attributeName);
        }
        return classAttribute;
    }
}
