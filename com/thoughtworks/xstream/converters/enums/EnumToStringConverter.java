package com.thoughtworks.xstream.converters.enums;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class EnumToStringConverter<T extends Enum<T>> extends AbstractSingleValueConverter {
    private final Class<T> enumType;
    private final Map<String, T> strings;
    private final EnumMap<T, String> values;

    public EnumToStringConverter(Class<T> type) {
        this(type, extractStringMap(type), null);
    }

    public EnumToStringConverter(Class<T> type, Map<String, T> strings) {
        this(type, strings, buildValueMap(type, strings));
    }

    private EnumToStringConverter(Class<T> type, Map<String, T> strings, EnumMap<T, String> values) {
        this.enumType = type;
        this.strings = strings;
        this.values = values;
    }

    private static <T extends Enum<T>> Map<String, T> extractStringMap(Class<T> type) {
        checkType(type);
        EnumSet<T> values = EnumSet.allOf(type);
        Map<String, T> strings = new HashMap(values.size());
        Iterator i$ = values.iterator();
        while (i$.hasNext()) {
            Enum value = (Enum) i$.next();
            if (strings.put(value.toString(), value) != null) {
                throw new IllegalArgumentException("Enum type " + type.getName() + " does not have unique string representations for its values");
            }
        }
        return strings;
    }

    private static <T> void checkType(Class<T> type) {
        if (!Enum.class.isAssignableFrom(type) && type != Enum.class) {
            throw new IllegalArgumentException("Converter can only handle enum types");
        }
    }

    private static <T extends Enum<T>> EnumMap<T, String> buildValueMap(Class<T> type, Map<String, T> strings) {
        EnumMap<T, String> values = new EnumMap(type);
        for (Entry<String, T> entry : strings.entrySet()) {
            values.put((Enum) entry.getValue(), entry.getKey());
        }
        return values;
    }

    public boolean canConvert(Class type) {
        return this.enumType.isAssignableFrom(type);
    }

    public String toString(Object obj) {
        Enum value = (Enum) Enum.class.cast(obj);
        return this.values == null ? value.toString() : (String) this.values.get(value);
    }

    public Object fromString(String str) {
        if (str == null) {
            return null;
        }
        Enum result = (Enum) this.strings.get(str);
        if (result != null) {
            return result;
        }
        throw new ConversionException("Invalid string representation for enum type " + this.enumType.getName() + ": <" + str + ">");
    }
}
