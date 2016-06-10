package com.thoughtworks.xstream.converters.basic;

import org.xbill.DNS.KEYRecord;

public class ByteConverter extends AbstractSingleValueConverter {
    public boolean canConvert(Class type) {
        return type.equals(Byte.TYPE) || type.equals(Byte.class);
    }

    public Object fromString(String str) {
        int value = Integer.decode(str).intValue();
        if (value >= -128 && value <= KEYRecord.PROTOCOL_ANY) {
            return new Byte((byte) value);
        }
        throw new NumberFormatException("For input string: \"" + str + '\"');
    }
}
