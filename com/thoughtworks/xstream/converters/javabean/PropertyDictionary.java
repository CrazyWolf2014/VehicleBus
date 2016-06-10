package com.thoughtworks.xstream.converters.javabean;

import com.thoughtworks.xstream.converters.reflection.MissingFieldException;
import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.core.Caching;
import com.thoughtworks.xstream.core.util.OrderRetainingMap;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PropertyDictionary implements Caching {
    private transient Map propertyNameCache;
    private final PropertySorter sorter;

    public PropertyDictionary() {
        this(new NativePropertySorter());
    }

    public PropertyDictionary(PropertySorter sorter) {
        this.propertyNameCache = Collections.synchronizedMap(new HashMap());
        this.sorter = sorter;
    }

    public Iterator serializablePropertiesFor(Class type) {
        Collection beanProperties = new ArrayList();
        for (PropertyDescriptor descriptor : buildMap(type).values()) {
            if (!(descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null)) {
                beanProperties.add(new BeanProperty(type, descriptor.getName(), descriptor.getPropertyType()));
            }
        }
        return beanProperties.iterator();
    }

    public BeanProperty property(Class cls, String name) {
        PropertyDescriptor descriptor = (PropertyDescriptor) buildMap(cls).get(name);
        if (descriptor == null) {
            throw new MissingFieldException(cls.getName(), name);
        } else if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
            return null;
        } else {
            return new BeanProperty(cls, descriptor.getName(), descriptor.getPropertyType());
        }
    }

    public Iterator propertiesFor(Class type) {
        return buildMap(type).values().iterator();
    }

    public PropertyDescriptor propertyDescriptor(Class type, String name) {
        PropertyDescriptor descriptor = (PropertyDescriptor) buildMap(type).get(name);
        if (descriptor != null) {
            return descriptor;
        }
        throw new MissingFieldException(type.getName(), name);
    }

    private Map buildMap(Class type) {
        Map nameMap = (Map) this.propertyNameCache.get(type);
        if (nameMap != null) {
            return nameMap;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type, Object.class);
            nameMap = new OrderRetainingMap();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                nameMap.put(descriptor.getName(), descriptor);
            }
            nameMap = this.sorter.sort(type, nameMap);
            this.propertyNameCache.put(type, nameMap);
            return nameMap;
        } catch (IntrospectionException e) {
            throw new ObjectAccessException("Cannot get BeanInfo of type " + type.getName(), e);
        }
    }

    public void flushCache() {
        this.propertyNameCache.clear();
    }
}
