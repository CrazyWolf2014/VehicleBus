package com.thoughtworks.xstream.converters.basic;

import com.tencent.mm.sdk.platformtools.Util;

public class IntConverter extends AbstractSingleValueConverter {
    public boolean canConvert(Class type) {
        return type.equals(Integer.TYPE) || type.equals(Integer.class);
    }

    public Object fromString(String str) {
        long value = Long.decode(str).longValue();
        if (value >= -2147483648L && value <= Util.MAX_32BIT_VALUE) {
            return new Integer((int) value);
        }
        throw new NumberFormatException("For input string: \"" + str + '\"');
    }
}
