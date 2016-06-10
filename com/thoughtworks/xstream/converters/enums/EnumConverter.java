package com.thoughtworks.xstream.converters.enums;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class EnumConverter implements Converter {
    public boolean canConvert(Class type) {
        return type.isEnum() || Enum.class.isAssignableFrom(type);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.setValue(((Enum) source).name());
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Object valueOf;
        Class type = context.getRequiredType();
        if (type.getSuperclass() != Enum.class) {
            type = type.getSuperclass();
        }
        String name = reader.getValue();
        try {
            valueOf = Enum.valueOf(type, name);
        } catch (IllegalArgumentException e) {
            Enum[] arr$ = (Enum[]) type.getEnumConstants();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                valueOf = arr$[i$];
                if (!valueOf.name().equalsIgnoreCase(name)) {
                    i$++;
                }
            }
            throw e;
        }
        return valueOf;
    }
}
