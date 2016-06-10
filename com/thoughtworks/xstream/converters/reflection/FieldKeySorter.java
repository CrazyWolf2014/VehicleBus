package com.thoughtworks.xstream.converters.reflection;

import java.util.Map;

public interface FieldKeySorter {
    Map sort(Class cls, Map map);
}
