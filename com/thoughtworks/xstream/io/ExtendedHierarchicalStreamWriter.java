package com.thoughtworks.xstream.io;

public interface ExtendedHierarchicalStreamWriter extends HierarchicalStreamWriter {
    void startNode(String str, Class cls);
}
