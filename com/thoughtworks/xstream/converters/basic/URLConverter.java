package com.thoughtworks.xstream.converters.basic;

import com.thoughtworks.xstream.converters.ConversionException;
import java.net.URL;

public class URLConverter extends AbstractSingleValueConverter {
    public boolean canConvert(Class type) {
        return type.equals(URL.class);
    }

    public Object fromString(String str) {
        try {
            return new URL(str);
        } catch (Throwable e) {
            throw new ConversionException(e);
        }
    }
}
