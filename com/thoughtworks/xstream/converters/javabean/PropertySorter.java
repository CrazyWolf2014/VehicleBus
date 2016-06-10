package com.thoughtworks.xstream.converters.javabean;

import java.util.Map;

public interface PropertySorter {
    Map sort(Class cls, Map map);
}
