package com.thoughtworks.xstream.io;

public interface ExtendedHierarchicalStreamReader extends HierarchicalStreamReader {
    String peekNextChild();
}
