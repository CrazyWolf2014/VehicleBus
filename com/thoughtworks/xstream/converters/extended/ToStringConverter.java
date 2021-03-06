package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ToStringConverter extends AbstractSingleValueConverter {
    private final Class clazz;
    private final Constructor ctor;

    public ToStringConverter(Class clazz) throws NoSuchMethodException {
        this.clazz = clazz;
        this.ctor = clazz.getConstructor(new Class[]{String.class});
    }

    public boolean canConvert(Class type) {
        return type.equals(this.clazz);
    }

    public String toString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public Object fromString(String str) {
        try {
            return this.ctor.newInstance(new Object[]{str});
        } catch (InstantiationException e) {
            throw new ConversionException("Unable to instantiate single String param constructor", e);
        } catch (IllegalAccessException e2) {
            throw new ConversionException("Unable to access single String param constructor", e2);
        } catch (InvocationTargetException e3) {
            throw new ConversionException("Unable to target single String param constructor", e3.getTargetException());
        }
    }
}
