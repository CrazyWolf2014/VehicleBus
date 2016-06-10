package com.thoughtworks.xstream.mapper;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.core.util.FastField;
import java.util.HashMap;
import java.util.Map;

public class LocalConversionMapper extends MapperWrapper {
    private transient AttributeMapper attributeMapper;
    private final Map localConverters;

    public LocalConversionMapper(Mapper wrapped) {
        super(wrapped);
        this.localConverters = new HashMap();
        readResolve();
    }

    public void registerLocalConverter(Class definedIn, String fieldName, Converter converter) {
        this.localConverters.put(new FastField(definedIn, fieldName), converter);
    }

    public Converter getLocalConverter(Class definedIn, String fieldName) {
        return (Converter) this.localConverters.get(new FastField(definedIn, fieldName));
    }

    public SingleValueConverter getConverterFromAttribute(Class definedIn, String attribute, Class type) {
        SingleValueConverter converter = getLocalSingleValueConverter(definedIn, attribute, type);
        return converter == null ? super.getConverterFromAttribute(definedIn, attribute, type) : converter;
    }

    public SingleValueConverter getConverterFromItemType(String fieldName, Class type, Class definedIn) {
        SingleValueConverter converter = getLocalSingleValueConverter(definedIn, fieldName, type);
        return converter == null ? super.getConverterFromItemType(fieldName, type, definedIn) : converter;
    }

    private SingleValueConverter getLocalSingleValueConverter(Class definedIn, String fieldName, Class type) {
        if (this.attributeMapper != null && this.attributeMapper.shouldLookForSingleValueConverter(fieldName, type, definedIn)) {
            Converter converter = getLocalConverter(definedIn, fieldName);
            if (converter != null && (converter instanceof SingleValueConverter)) {
                return (SingleValueConverter) converter;
            }
        }
        return null;
    }

    private Object readResolve() {
        this.attributeMapper = (AttributeMapper) lookupMapperOfType(AttributeMapper.class);
        return this;
    }
}
