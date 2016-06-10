package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ThrowableConverter implements Converter {
    private Converter defaultConverter;
    private final ConverterLookup lookup;

    public ThrowableConverter(Converter defaultConverter) {
        this.defaultConverter = defaultConverter;
        this.lookup = null;
    }

    public ThrowableConverter(ConverterLookup lookup) {
        this.lookup = lookup;
    }

    public boolean canConvert(Class type) {
        return Throwable.class.isAssignableFrom(type);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Throwable throwable = (Throwable) source;
        if (throwable.getCause() == null) {
            try {
                throwable.initCause(null);
            } catch (IllegalStateException e) {
            }
        }
        throwable.getStackTrace();
        getConverter().marshal(throwable, writer, context);
    }

    private Converter getConverter() {
        return this.defaultConverter != null ? this.defaultConverter : this.lookup.lookupConverterForType(Object.class);
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return getConverter().unmarshal(reader, context);
    }
}
