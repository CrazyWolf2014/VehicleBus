package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractReferenceUnmarshaller extends TreeUnmarshaller {
    private static final Object NULL;
    private FastStack parentStack;
    private Map values;

    protected abstract Object getCurrentReferenceKey();

    protected abstract Object getReferenceKey(String str);

    static {
        NULL = new Object();
    }

    public AbstractReferenceUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
        super(root, reader, converterLookup, mapper);
        this.values = new HashMap();
        this.parentStack = new FastStack(16);
    }

    protected Object convert(Object parent, Class type, Converter converter) {
        if (this.parentStack.size() > 0) {
            Object parentReferenceKey = this.parentStack.peek();
            if (!(parentReferenceKey == null || this.values.containsKey(parentReferenceKey))) {
                this.values.put(parentReferenceKey, parent);
            }
        }
        String attributeName = getMapper().aliasForSystemAttribute("reference");
        String reference = attributeName == null ? null : this.reader.getAttribute(attributeName);
        if (reference != null) {
            Object cache = this.values.get(getReferenceKey(reference));
            if (cache == null) {
                ConversionException ex = new ConversionException("Invalid reference");
                ex.add("reference", reference);
                throw ex;
            } else if (cache == NULL) {
                return null;
            } else {
                return cache;
            }
        }
        Object currentReferenceKey = getCurrentReferenceKey();
        this.parentStack.push(currentReferenceKey);
        Object result = super.convert(parent, type, converter);
        if (currentReferenceKey != null) {
            Object obj;
            Map map = this.values;
            if (result == null) {
                obj = NULL;
            } else {
                obj = result;
            }
            map.put(currentReferenceKey, obj);
        }
        this.parentStack.popSilently();
        return result;
    }
}
