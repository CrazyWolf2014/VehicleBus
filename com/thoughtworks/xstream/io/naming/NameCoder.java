package com.thoughtworks.xstream.io.naming;

public interface NameCoder {
    String decodeAttribute(String str);

    String decodeNode(String str);

    String encodeAttribute(String str);

    String encodeNode(String str);
}
