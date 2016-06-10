package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.ScalarSerializerBase;

public class InetAddressSerializer extends ScalarSerializerBase<InetAddress> {
    public static final InetAddressSerializer instance;

    static {
        instance = new InetAddressSerializer();
    }

    public InetAddressSerializer() {
        super(InetAddress.class);
    }

    public void serialize(InetAddress value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        String str = value.toString().trim();
        int ix = str.indexOf(47);
        if (ix >= 0) {
            if (ix == 0) {
                str = str.substring(1);
            } else {
                str = str.substring(0, ix);
            }
        }
        jgen.writeString(str);
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("string", true);
    }
}
