package org.codehaus.jackson.xc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import javax.activation.DataHandler;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.SerializerBase;
import org.codehaus.jackson.node.ObjectNode;
import org.xbill.DNS.KEYRecord.Flags;

public class DataHandlerJsonSerializer extends SerializerBase<DataHandler> {
    public DataHandlerJsonSerializer() {
        super(DataHandler.class);
    }

    public void serialize(DataHandler value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[Flags.EXTEND];
        InputStream in = value.getInputStream();
        for (int len = in.read(buffer); len > 0; len = in.read(buffer)) {
            out.write(buffer, 0, len);
        }
        jgen.writeBinary(out.toByteArray());
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        ObjectNode o = createSchemaNode("array", true);
        o.put("items", createSchemaNode("string"));
        return o;
    }
}
