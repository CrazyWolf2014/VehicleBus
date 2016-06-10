package com.thoughtworks.xstream.converters.enums;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class EnumSingleValueConverter extends AbstractSingleValueConverter {
    private final Class<? extends Enum> enumType;

    public EnumSingleValueConverter(Class<? extends Enum> type) {
        if (Enum.class.isAssignableFrom(type) || type == Enum.class) {
            this.enumType = type;
            return;
        }
        throw new IllegalArgumentException("Converter can only handle defined enums");
    }

    public boolean canConvert(Class type) {
        return this.enumType.isAssignableFrom(type);
    }

    public String toString(Object obj) {
        return ((Enum) Enum.class.cast(obj)).name();
    }

    public Object fromString(String str) {
        return Enum.valueOf(this.enumType, str);
    }
}
