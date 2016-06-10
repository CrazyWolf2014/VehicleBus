package com.thoughtworks.xstream.converters;

public interface MarshallingContext extends DataHolder {
    void convertAnother(Object obj);

    void convertAnother(Object obj, Converter converter);
}
