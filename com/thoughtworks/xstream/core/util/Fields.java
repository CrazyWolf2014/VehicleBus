package com.thoughtworks.xstream.core.util;

import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Fields {
    public static Field locate(Class definedIn, Class fieldType, boolean isStatic) {
        Field field = null;
        try {
            Field[] fields = definedIn.getDeclaredFields();
            int i = 0;
            while (i < fields.length) {
                if (Modifier.isStatic(fields[i].getModifiers()) == isStatic && fieldType.isAssignableFrom(fields[i].getType())) {
                    field = fields[i];
                }
                i++;
            }
            if (field != null) {
                field.setAccessible(true);
            }
        } catch (SecurityException e) {
        }
        return field;
    }

    public static Field find(Class type, String name) {
        try {
            Field result = type.getDeclaredField(name);
            if (!result.isAccessible()) {
                result.setAccessible(true);
            }
            return result;
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Could not access " + type.getName() + "." + name + " field: " + e.getMessage());
        }
    }

    public static void write(Field field, Object instance, Object value) {
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new ObjectAccessException("Could not write " + field.getType().getName() + "." + field.getName() + " field", e);
        }
    }

    public static Object read(Field field, Object instance) {
        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            throw new ObjectAccessException("Could not read " + field.getType().getName() + "." + field.getName() + " field", e);
        }
    }
}
