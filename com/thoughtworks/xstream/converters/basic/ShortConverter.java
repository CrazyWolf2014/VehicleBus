package com.thoughtworks.xstream.converters.basic;

import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

public class ShortConverter extends AbstractSingleValueConverter {
    public boolean canConvert(Class type) {
        return type.equals(Short.TYPE) || type.equals(Short.class);
    }

    public Object fromString(String str) {
        int value = Integer.decode(str).intValue();
        if (value >= -32768 && value <= InBandBytestreamManager.MAXIMUM_BLOCK_SIZE) {
            return new Short((short) value);
        }
        throw new NumberFormatException("For input string: \"" + str + '\"');
    }
}
