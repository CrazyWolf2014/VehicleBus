package com.thoughtworks.xstream.converters.basic;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.xmlpull.v1.XmlPullParser;

public class CharConverter implements Converter, SingleValueConverter {
    public boolean canConvert(Class type) {
        return type.equals(Character.TYPE) || type.equals(Character.class);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.setValue(toString(source));
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String nullAttribute = reader.getAttribute("null");
        if (nullAttribute == null || !nullAttribute.equals("true")) {
            return fromString(reader.getValue());
        }
        return new Character('\u0000');
    }

    public Object fromString(String str) {
        if (str.length() == 0) {
            return new Character('\u0000');
        }
        return new Character(str.charAt(0));
    }

    public String toString(Object obj) {
        return ((Character) obj).charValue() == '\u0000' ? XmlPullParser.NO_NAMESPACE : obj.toString();
    }
}
