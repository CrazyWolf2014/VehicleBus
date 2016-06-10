package org.codehaus.jackson.map.deser;

import java.io.IOException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.type.JavaType;

@JacksonStdImpl
public class ArrayDeserializer extends ContainerDeserializer<Object[]> {
    protected final JavaType _arrayType;
    protected final Class<?> _elementClass;
    protected final JsonDeserializer<Object> _elementDeserializer;
    final TypeDeserializer _elementTypeDeserializer;
    protected final boolean _untyped;

    @Deprecated
    public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser) {
        this(arrayType, elemDeser, null);
    }

    public ArrayDeserializer(ArrayType arrayType, JsonDeserializer<Object> elemDeser, TypeDeserializer elemTypeDeser) {
        super(Object[].class);
        this._arrayType = arrayType;
        this._elementClass = arrayType.getContentType().getRawClass();
        this._untyped = this._elementClass == Object.class;
        this._elementDeserializer = elemDeser;
        this._elementTypeDeserializer = elemTypeDeser;
    }

    public JavaType getContentType() {
        return this._arrayType.getContentType();
    }

    public JsonDeserializer<Object> getContentDeserializer() {
        return this._elementDeserializer;
    }

    public Object[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (jp.isExpectedStartArrayToken()) {
            Object[] result;
            ObjectBuffer buffer = ctxt.leaseObjectBuffer();
            Object[] chunk = buffer.resetAndStart();
            int ix = 0;
            TypeDeserializer typeDeser = this._elementTypeDeserializer;
            while (true) {
                JsonToken t = jp.nextToken();
                if (t == JsonToken.END_ARRAY) {
                    break;
                }
                Object obj;
                if (t == JsonToken.VALUE_NULL) {
                    obj = null;
                } else if (typeDeser == null) {
                    obj = this._elementDeserializer.deserialize(jp, ctxt);
                } else {
                    obj = this._elementDeserializer.deserializeWithType(jp, ctxt, typeDeser);
                }
                if (ix >= chunk.length) {
                    chunk = buffer.appendCompletedChunk(chunk);
                    ix = 0;
                }
                int ix2 = ix + 1;
                chunk[ix] = obj;
                ix = ix2;
            }
            if (this._untyped) {
                result = buffer.completeAndClearBuffer(chunk, ix);
            } else {
                result = buffer.completeAndClearBuffer(chunk, ix, this._elementClass);
            }
            ctxt.returnObjectBuffer(buffer);
            return result;
        } else if (jp.getCurrentToken() == JsonToken.VALUE_STRING && this._elementClass == Byte.class) {
            return deserializeFromBase64(jp, ctxt);
        } else {
            throw ctxt.mappingException(this._arrayType.getRawClass());
        }
    }

    public Object[] deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return (Object[]) typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }

    protected Byte[] deserializeFromBase64(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        byte[] b = jp.getBinaryValue(ctxt.getBase64Variant());
        Byte[] result = new Byte[b.length];
        int len = b.length;
        for (int i = 0; i < len; i++) {
            result[i] = Byte.valueOf(b[i]);
        }
        return result;
    }
}
