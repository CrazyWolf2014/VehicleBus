package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.core.util.ObjectIdDictionary;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class TreeMarshaller implements MarshallingContext {
    protected ConverterLookup converterLookup;
    private DataHolder dataHolder;
    private Mapper mapper;
    private ObjectIdDictionary parentObjects;
    protected HierarchicalStreamWriter writer;

    public static class CircularReferenceException extends ConversionException {
        public CircularReferenceException(String msg) {
            super(msg);
        }
    }

    public TreeMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
        this.parentObjects = new ObjectIdDictionary();
        this.writer = writer;
        this.converterLookup = converterLookup;
        this.mapper = mapper;
    }

    public void convertAnother(Object item) {
        convertAnother(item, null);
    }

    public void convertAnother(Object item, Converter converter) {
        if (converter == null) {
            converter = this.converterLookup.lookupConverterForType(item.getClass());
        } else if (!converter.canConvert(item.getClass())) {
            ConversionException e = new ConversionException("Explicit selected converter cannot handle item");
            e.add("item-type", item.getClass().getName());
            e.add("converter-type", converter.getClass().getName());
            throw e;
        }
        convert(item, converter);
    }

    protected void convert(Object item, Converter converter) {
        if (this.parentObjects.containsId(item)) {
            ConversionException e = new CircularReferenceException("Recursive reference to parent object");
            e.add("item-type", item.getClass().getName());
            e.add("converter-type", converter.getClass().getName());
            throw e;
        }
        this.parentObjects.associateId(item, XmlPullParser.NO_NAMESPACE);
        converter.marshal(item, this.writer, this);
        this.parentObjects.removeId(item);
    }

    public void start(Object item, DataHolder dataHolder) {
        this.dataHolder = dataHolder;
        if (item == null) {
            this.writer.startNode(this.mapper.serializedClass(null));
            this.writer.endNode();
            return;
        }
        ExtendedHierarchicalStreamWriterHelper.startNode(this.writer, this.mapper.serializedClass(item.getClass()), item.getClass());
        convertAnother(item);
        this.writer.endNode();
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

    protected Mapper getMapper() {
        return this.mapper;
    }
}
