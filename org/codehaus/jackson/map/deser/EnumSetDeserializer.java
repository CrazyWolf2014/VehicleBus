package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.util.EnumSet;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.TypeDeserializer;

public final class EnumSetDeserializer extends StdDeserializer<EnumSet<?>> {
    final Class<Enum> _enumClass;
    final EnumDeserializer _enumDeserializer;

    public EnumSetDeserializer(EnumResolver enumRes) {
        super(EnumSet.class);
        this._enumDeserializer = new EnumDeserializer(enumRes);
        this._enumClass = enumRes.getEnumClass();
    }

    public EnumSet<?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.isExpectedStartArrayToken()) {
            EnumSet result = constructSet();
            while (true) {
                JsonToken t = jp.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    return result;
                }
                if (t == JsonToken.VALUE_NULL) {
                    break;
                }
                result.add(this._enumDeserializer.deserialize(jp, ctxt));
            }
            throw ctxt.mappingException(this._enumClass);
        }
        throw ctxt.mappingException(EnumSet.class);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    private EnumSet constructSet() {
        return EnumSet.noneOf(this._enumClass);
    }
}
