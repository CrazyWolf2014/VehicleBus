package com.thoughtworks.xstream.converters.basic;

import com.thoughtworks.xstream.core.util.WeakCache;
import java.util.Collections;
import java.util.Map;

public class StringConverter extends AbstractSingleValueConverter {
    private static final int LENGTH_LIMIT = 38;
    private final Map cache;
    private final int lengthLimit;

    public StringConverter(Map map, int lengthLimit) {
        this.cache = map;
        this.lengthLimit = lengthLimit;
    }

    public StringConverter(Map map) {
        this(map, LENGTH_LIMIT);
    }

    public StringConverter(int lengthLimit) {
        this(Collections.synchronizedMap(new WeakCache()), lengthLimit);
    }

    public StringConverter() {
        this((int) LENGTH_LIMIT);
    }

    public boolean canConvert(Class type) {
        return type.equals(String.class);
    }

    public Object fromString(String str) {
        if (this.cache == null || str == null || (this.lengthLimit >= 0 && str.length() > this.lengthLimit)) {
            return str;
        }
        String s = (String) this.cache.get(str);
        if (s != null) {
            return s;
        }
        this.cache.put(str, str);
        return str;
    }
}
