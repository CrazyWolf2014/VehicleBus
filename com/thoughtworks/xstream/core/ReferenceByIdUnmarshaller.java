package com.thoughtworks.xstream.core;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;

public class ReferenceByIdUnmarshaller extends AbstractReferenceUnmarshaller {
    public ReferenceByIdUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
        super(root, reader, converterLookup, mapper);
    }

    protected Object getReferenceKey(String reference) {
        return reference;
    }

    protected Object getCurrentReferenceKey() {
        String attributeName = getMapper().aliasForSystemAttribute(LocaleUtil.INDONESIAN);
        return attributeName == null ? null : this.reader.getAttribute(attributeName);
    }
}
