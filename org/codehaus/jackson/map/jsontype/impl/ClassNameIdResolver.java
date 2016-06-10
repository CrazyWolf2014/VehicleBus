package org.codehaus.jackson.map.jsontype.impl;

import java.util.EnumMap;
import java.util.EnumSet;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class ClassNameIdResolver extends TypeIdResolverBase {
    public ClassNameIdResolver(JavaType baseType) {
        super(baseType);
    }

    public Id getMechanism() {
        return Id.CLASS;
    }

    public void registerSubtype(Class<?> cls, String name) {
    }

    public String idFromValue(Object value) {
        Class<?> cls = value.getClass();
        if (Enum.class.isAssignableFrom(cls) && !cls.isEnum()) {
            cls = cls.getSuperclass();
        }
        String str = cls.getName();
        if (!str.startsWith("java.util")) {
            return str;
        }
        if (value instanceof EnumSet) {
            return TypeFactory.collectionType(EnumSet.class, ClassUtil.findEnumType((EnumSet) value)).toCanonical();
        } else if (value instanceof EnumMap) {
            return TypeFactory.mapType(EnumMap.class, ClassUtil.findEnumType((EnumMap) value), Object.class).toCanonical();
        } else {
            String end = str.substring(9);
            if ((end.startsWith(".Arrays$") || end.startsWith(".Collections$")) && str.indexOf("List") >= 0) {
                return "java.util.ArrayList";
            }
            return str;
        }
    }

    public JavaType typeFromId(String id) {
        if (id.indexOf(60) > 0) {
            return TypeFactory.fromCanonical(id);
        }
        try {
            return TypeFactory.specialize(this._baseType, Class.forName(id, true, Thread.currentThread().getContextClassLoader()));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): no such class found");
        } catch (Exception e2) {
            throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): " + e2.getMessage(), e2);
        }
    }
}
