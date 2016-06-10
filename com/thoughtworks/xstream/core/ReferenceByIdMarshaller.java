package com.thoughtworks.xstream.core;

import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.path.Path;
import com.thoughtworks.xstream.mapper.Mapper;

public class ReferenceByIdMarshaller extends AbstractReferenceMarshaller {
    private final IDGenerator idGenerator;

    public interface IDGenerator {
        String next(Object obj);
    }

    public ReferenceByIdMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper, IDGenerator idGenerator) {
        super(writer, converterLookup, mapper);
        this.idGenerator = idGenerator;
    }

    public ReferenceByIdMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
        this(writer, converterLookup, mapper, new SequenceGenerator(1));
    }

    protected String createReference(Path currentPath, Object existingReferenceKey) {
        return existingReferenceKey.toString();
    }

    protected Object createReferenceKey(Path currentPath, Object item) {
        return this.idGenerator.next(item);
    }

    protected void fireValidReference(Object referenceKey) {
        String attributeName = getMapper().aliasForSystemAttribute(LocaleUtil.INDONESIAN);
        if (attributeName != null) {
            this.writer.addAttribute(attributeName, referenceKey.toString());
        }
    }
}
