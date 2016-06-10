package com.thoughtworks.xstream.mapper;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AttributeMapper extends MapperWrapper {
    private ConverterLookup converterLookup;
    private final Map fieldNameToTypeMap;
    private final Set fieldToUseAsAttribute;
    private ReflectionProvider reflectionProvider;
    private final Set typeSet;

    public AttributeMapper(Mapper wrapped) {
        this(wrapped, null, null);
    }

    public AttributeMapper(Mapper wrapped, ConverterLookup converterLookup, ReflectionProvider refProvider) {
        super(wrapped);
        this.fieldNameToTypeMap = new HashMap();
        this.typeSet = new HashSet();
        this.fieldToUseAsAttribute = new HashSet();
        this.converterLookup = converterLookup;
        this.reflectionProvider = refProvider;
    }

    public void setConverterLookup(ConverterLookup converterLookup) {
        this.converterLookup = converterLookup;
    }

    public void addAttributeFor(String fieldName, Class type) {
        this.fieldNameToTypeMap.put(fieldName, type);
    }

    public void addAttributeFor(Class type) {
        this.typeSet.add(type);
    }

    private SingleValueConverter getLocalConverterFromItemType(Class type) {
        Converter converter = this.converterLookup.lookupConverterForType(type);
        if (converter == null || !(converter instanceof SingleValueConverter)) {
            return null;
        }
        return (SingleValueConverter) converter;
    }

    public SingleValueConverter getConverterFromItemType(String fieldName, Class type) {
        if (this.fieldNameToTypeMap.get(fieldName) == type) {
            return getLocalConverterFromItemType(type);
        }
        return null;
    }

    public SingleValueConverter getConverterFromItemType(String fieldName, Class type, Class definedIn) {
        if (shouldLookForSingleValueConverter(fieldName, type, definedIn)) {
            SingleValueConverter converter = getLocalConverterFromItemType(type);
            if (converter != null) {
                return converter;
            }
        }
        return super.getConverterFromItemType(fieldName, type, definedIn);
    }

    public boolean shouldLookForSingleValueConverter(String fieldName, Class type, Class definedIn) {
        if (this.typeSet.contains(type) || this.fieldNameToTypeMap.get(fieldName) == type) {
            return true;
        }
        if (fieldName == null || definedIn == null) {
            return false;
        }
        return this.fieldToUseAsAttribute.contains(this.reflectionProvider.getField(definedIn, fieldName));
    }

    public SingleValueConverter getConverterFromItemType(Class type) {
        if (this.typeSet.contains(type)) {
            return getLocalConverterFromItemType(type);
        }
        return null;
    }

    public SingleValueConverter getConverterFromAttribute(String attributeName) {
        Class type = (Class) this.fieldNameToTypeMap.get(attributeName);
        if (type != null) {
            return getLocalConverterFromItemType(type);
        }
        return null;
    }

    public SingleValueConverter getConverterFromAttribute(Class definedIn, String attribute) {
        return getConverterFromAttribute(definedIn, attribute, this.reflectionProvider.getField(definedIn, attribute).getType());
    }

    public SingleValueConverter getConverterFromAttribute(Class definedIn, String attribute, Class type) {
        if (shouldLookForSingleValueConverter(attribute, type, definedIn)) {
            SingleValueConverter converter = getLocalConverterFromItemType(type);
            if (converter != null) {
                return converter;
            }
        }
        return super.getConverterFromAttribute(definedIn, attribute, type);
    }

    public void addAttributeFor(Field field) {
        this.fieldToUseAsAttribute.add(field);
    }

    public void addAttributeFor(Class definedIn, String fieldName) {
        this.fieldToUseAsAttribute.add(this.reflectionProvider.getField(definedIn, fieldName));
    }
}
