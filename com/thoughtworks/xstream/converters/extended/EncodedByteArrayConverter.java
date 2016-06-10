package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.ByteConverter;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class EncodedByteArrayConverter implements Converter, SingleValueConverter {
    private static final Base64Encoder base64;
    private static final ByteConverter byteConverter;

    static {
        base64 = new Base64Encoder();
        byteConverter = new ByteConverter();
    }

    public boolean canConvert(Class type) {
        return type.isArray() && type.getComponentType().equals(Byte.TYPE);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.setValue(toString(source));
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String data = reader.getValue();
        if (reader.hasMoreChildren()) {
            return unmarshalIndividualByteElements(reader, context);
        }
        return fromString(data);
    }

    private Object unmarshalIndividualByteElements(HierarchicalStreamReader reader, UnmarshallingContext context) {
        List<Byte> bytes = new ArrayList();
        boolean firstIteration = true;
        while (true) {
            if (!firstIteration && !reader.hasMoreChildren()) {
                break;
            }
            reader.moveDown();
            bytes.add(byteConverter.fromString(reader.getValue()));
            reader.moveUp();
            firstIteration = false;
        }
        byte[] result = new byte[bytes.size()];
        int i = 0;
        for (Byte b : bytes) {
            result[i] = b.byteValue();
            i++;
        }
        return result;
    }

    public String toString(Object obj) {
        return base64.encode((byte[]) obj);
    }

    public Object fromString(String str) {
        return base64.decode(str);
    }
}
