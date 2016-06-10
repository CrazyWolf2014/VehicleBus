package com.thoughtworks.xstream.converters.basic;

import com.thoughtworks.xstream.converters.ConversionException;
import java.net.URI;

public class URIConverter extends AbstractSingleValueConverter {
    public boolean canConvert(Class type) {
        return type.equals(URI.class);
    }

    public Object fromString(String str) {
        try {
            return new URI(str);
        } catch (Throwable e) {
            throw new ConversionException(e);
        }
    }
}
