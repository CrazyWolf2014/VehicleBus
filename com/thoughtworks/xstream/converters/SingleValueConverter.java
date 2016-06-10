package com.thoughtworks.xstream.converters;

public interface SingleValueConverter extends ConverterMatcher {
    Object fromString(String str);

    String toString(Object obj);
}
