package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.ErrorReporter;
import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.core.util.HierarchicalStreams;
import com.thoughtworks.xstream.core.util.PrioritizedList;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.Iterator;

public class TreeUnmarshaller implements UnmarshallingContext {
    private ConverterLookup converterLookup;
    private DataHolder dataHolder;
    private Mapper mapper;
    protected HierarchicalStreamReader reader;
    private Object root;
    private FastStack types;
    private final PrioritizedList validationList;

    public TreeUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
        this.types = new FastStack(16);
        this.validationList = new PrioritizedList();
        this.root = root;
        this.reader = reader;
        this.converterLookup = converterLookup;
        this.mapper = mapper;
    }

    public Object convertAnother(Object parent, Class type) {
        return convertAnother(parent, type, null);
    }

    public Object convertAnother(Object parent, Class type, Converter converter) {
        type = this.mapper.defaultImplementationOf(type);
        if (converter == null) {
            converter = this.converterLookup.lookupConverterForType(type);
        } else if (!converter.canConvert(type)) {
            ConversionException e = new ConversionException("Explicit selected converter cannot handle type");
            e.add("item-type", type.getName());
            e.add("converter-type", converter.getClass().getName());
            throw e;
        }
        return convert(parent, type, converter);
    }

    protected Object convert(Object parent, Class type, Converter converter) {
        ConversionException conversionException;
        try {
            this.types.push(type);
            Object result = converter.unmarshal(this.reader, this);
            this.types.popSilently();
            return result;
        } catch (ConversionException conversionException2) {
            addInformationTo(conversionException2, type, converter, parent);
            throw conversionException2;
        } catch (Throwable e) {
            conversionException2 = new ConversionException(e);
            addInformationTo(conversionException2, type, converter, parent);
            throw conversionException2;
        }
    }

    private void addInformationTo(ErrorWriter errorWriter, Class type, Converter converter, Object parent) {
        errorWriter.add("class", type.getName());
        errorWriter.add("required-type", getRequiredType().getName());
        errorWriter.add("converter-type", converter.getClass().getName());
        if (converter instanceof ErrorReporter) {
            ((ErrorReporter) converter).appendErrors(errorWriter);
        }
        if (parent instanceof ErrorReporter) {
            ((ErrorReporter) parent).appendErrors(errorWriter);
        }
        this.reader.appendErrors(errorWriter);
    }

    public void addCompletionCallback(Runnable work, int priority) {
        this.validationList.add(work, priority);
    }

    public Object currentObject() {
        return this.types.size() == 1 ? this.root : null;
    }

    public Class getRequiredType() {
        return (Class) this.types.peek();
    }

    public Object get(Object key) {
        lazilyCreateDataHolder();
        return this.dataHolder.get(key);
    }

    public void put(Object key, Object value) {
        lazilyCreateDataHolder();
        this.dataHolder.put(key, value);
    }

    public Iterator keys() {
        lazilyCreateDataHolder();
        return this.dataHolder.keys();
    }

    private void lazilyCreateDataHolder() {
        if (this.dataHolder == null) {
            this.dataHolder = new MapBackedDataHolder();
        }
    }

    public Object start(DataHolder dataHolder) {
        this.dataHolder = dataHolder;
        Object result = convertAnother(null, HierarchicalStreams.readClassType(this.reader, this.mapper));
        Iterator validations = this.validationList.iterator();
        while (validations.hasNext()) {
            ((Runnable) validations.next()).run();
        }
        return result;
    }

    protected Mapper getMapper() {
        return this.mapper;
    }
}
