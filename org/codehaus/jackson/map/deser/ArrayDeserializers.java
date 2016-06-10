package org.codehaus.jackson.map.deser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ArrayBuilders.BooleanBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.ByteBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.DoubleBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.FloatBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.IntBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.LongBuilder;
import org.codehaus.jackson.map.util.ArrayBuilders.ShortBuilder;
import org.codehaus.jackson.map.util.ObjectBuffer;
import org.codehaus.jackson.type.JavaType;

public class ArrayDeserializers {
    static final ArrayDeserializers instance;
    HashMap<JavaType, JsonDeserializer<Object>> _allDeserializers;

    static abstract class ArrayDeser<T> extends StdDeserializer<T> {
        protected ArrayDeser(Class<T> cls) {
            super((Class) cls);
        }

        public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
            return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
        }
    }

    @JacksonStdImpl
    static final class BooleanDeser extends ArrayDeser<boolean[]> {
        public BooleanDeser() {
            super(boolean[].class);
        }

        public boolean[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                BooleanBuilder builder = ctxt.getArrayBuilders().getBooleanBuilder();
                boolean[] chunk = (boolean[]) builder.resetAndStart();
                int ix = 0;
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    boolean value = _parseBooleanPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (boolean[]) builder.appendCompletedChunk(chunk, ix);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                }
                return (boolean[]) builder.completeAndClearBuffer(chunk, ix);
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class ByteDeser extends ArrayDeser<byte[]> {
        public ByteDeser() {
            super(byte[].class);
        }

        public byte[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonToken t = jp.getCurrentToken();
            if (t == JsonToken.VALUE_STRING) {
                return jp.getBinaryValue(ctxt.getBase64Variant());
            }
            if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                Object ob = jp.getEmbeddedObject();
                if (ob == null) {
                    return null;
                }
                if (ob instanceof byte[]) {
                    return (byte[]) ob;
                }
            }
            if (jp.isExpectedStartArrayToken()) {
                ByteBuilder builder = ctxt.getArrayBuilders().getByteBuilder();
                byte[] chunk = (byte[]) builder.resetAndStart();
                int ix = 0;
                while (true) {
                    t = jp.nextToken();
                    if (t == JsonToken.END_ARRAY) {
                        return (byte[]) builder.completeAndClearBuffer(chunk, ix);
                    }
                    byte value;
                    if (t == JsonToken.VALUE_NUMBER_INT || t == JsonToken.VALUE_NUMBER_FLOAT) {
                        value = jp.getByteValue();
                    } else if (t != JsonToken.VALUE_NULL) {
                        break;
                    } else {
                        value = (byte) 0;
                    }
                    if (ix >= chunk.length) {
                        chunk = (byte[]) builder.appendCompletedChunk(chunk, ix);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                }
                throw ctxt.mappingException(this._valueClass.getComponentType());
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class CharDeser extends ArrayDeser<char[]> {
        public CharDeser() {
            super(char[].class);
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public char[] deserialize(org.codehaus.jackson.JsonParser r12, org.codehaus.jackson.map.DeserializationContext r13) throws java.io.IOException, org.codehaus.jackson.JsonProcessingException {
            /*
            r11 = this;
            r10 = 0;
            r7 = r12.getCurrentToken();
            r8 = org.codehaus.jackson.JsonToken.VALUE_STRING;
            if (r7 != r8) goto L_0x001b;
        L_0x0009:
            r0 = r12.getTextCharacters();
            r3 = r12.getTextOffset();
            r1 = r12.getTextLength();
            r4 = new char[r1];
            java.lang.System.arraycopy(r0, r3, r4, r10, r1);
        L_0x001a:
            return r4;
        L_0x001b:
            r8 = r12.isExpectedStartArrayToken();
            if (r8 == 0) goto L_0x0079;
        L_0x0021:
            r5 = new java.lang.StringBuilder;
            r8 = 64;
            r5.<init>(r8);
        L_0x0028:
            r7 = r12.nextToken();
            r8 = org.codehaus.jackson.JsonToken.END_ARRAY;
            if (r7 == r8) goto L_0x0070;
        L_0x0030:
            r8 = org.codehaus.jackson.JsonToken.VALUE_STRING;
            if (r7 == r8) goto L_0x003b;
        L_0x0034:
            r8 = java.lang.Character.TYPE;
            r8 = r13.mappingException(r8);
            throw r8;
        L_0x003b:
            r6 = r12.getText();
            r8 = r6.length();
            r9 = 1;
            if (r8 == r9) goto L_0x0068;
        L_0x0046:
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r9 = "Can not convert a JSON String of length ";
            r8 = r8.append(r9);
            r9 = r6.length();
            r8 = r8.append(r9);
            r9 = " into a char element of char array";
            r8 = r8.append(r9);
            r8 = r8.toString();
            r8 = org.codehaus.jackson.map.JsonMappingException.from(r12, r8);
            throw r8;
        L_0x0068:
            r8 = r6.charAt(r10);
            r5.append(r8);
            goto L_0x0028;
        L_0x0070:
            r8 = r5.toString();
            r4 = r8.toCharArray();
            goto L_0x001a;
        L_0x0079:
            r8 = org.codehaus.jackson.JsonToken.VALUE_EMBEDDED_OBJECT;
            if (r7 != r8) goto L_0x00b0;
        L_0x007d:
            r2 = r12.getEmbeddedObject();
            if (r2 != 0) goto L_0x0085;
        L_0x0083:
            r4 = 0;
            goto L_0x001a;
        L_0x0085:
            r8 = r2 instanceof char[];
            if (r8 == 0) goto L_0x008f;
        L_0x0089:
            r2 = (char[]) r2;
            r2 = (char[]) r2;
            r4 = r2;
            goto L_0x001a;
        L_0x008f:
            r8 = r2 instanceof java.lang.String;
            if (r8 == 0) goto L_0x009a;
        L_0x0093:
            r2 = (java.lang.String) r2;
            r4 = r2.toCharArray();
            goto L_0x001a;
        L_0x009a:
            r8 = r2 instanceof byte[];
            if (r8 == 0) goto L_0x00b0;
        L_0x009e:
            r8 = org.codehaus.jackson.Base64Variants.getDefaultVariant();
            r2 = (byte[]) r2;
            r2 = (byte[]) r2;
            r8 = r8.encode(r2, r10);
            r4 = r8.toCharArray();
            goto L_0x001a;
        L_0x00b0:
            r8 = r11._valueClass;
            r8 = r13.mappingException(r8);
            throw r8;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.map.deser.ArrayDeserializers.CharDeser.deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext):char[]");
        }
    }

    @JacksonStdImpl
    static final class DoubleDeser extends ArrayDeser<double[]> {
        public DoubleDeser() {
            super(double[].class);
        }

        public double[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                DoubleBuilder builder = ctxt.getArrayBuilders().getDoubleBuilder();
                double[] chunk = (double[]) builder.resetAndStart();
                int ix = 0;
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    double value = _parseDoublePrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (double[]) builder.appendCompletedChunk(chunk, ix);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                }
                return (double[]) builder.completeAndClearBuffer(chunk, ix);
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class FloatDeser extends ArrayDeser<float[]> {
        public FloatDeser() {
            super(float[].class);
        }

        public float[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                FloatBuilder builder = ctxt.getArrayBuilders().getFloatBuilder();
                float[] chunk = (float[]) builder.resetAndStart();
                int ix = 0;
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    float value = _parseFloatPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (float[]) builder.appendCompletedChunk(chunk, ix);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                }
                return (float[]) builder.completeAndClearBuffer(chunk, ix);
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class IntDeser extends ArrayDeser<int[]> {
        public IntDeser() {
            super(int[].class);
        }

        public int[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                IntBuilder builder = ctxt.getArrayBuilders().getIntBuilder();
                int[] chunk = (int[]) builder.resetAndStart();
                int ix = 0;
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    int value = _parseIntPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (int[]) builder.appendCompletedChunk(chunk, ix);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                }
                return (int[]) builder.completeAndClearBuffer(chunk, ix);
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class LongDeser extends ArrayDeser<long[]> {
        public LongDeser() {
            super(long[].class);
        }

        public long[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                LongBuilder builder = ctxt.getArrayBuilders().getLongBuilder();
                long[] chunk = (long[]) builder.resetAndStart();
                int ix = 0;
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    long value = _parseLongPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (long[]) builder.appendCompletedChunk(chunk, ix);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                }
                return (long[]) builder.completeAndClearBuffer(chunk, ix);
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class ShortDeser extends ArrayDeser<short[]> {
        public ShortDeser() {
            super(short[].class);
        }

        public short[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                ShortBuilder builder = ctxt.getArrayBuilders().getShortBuilder();
                short[] chunk = (short[]) builder.resetAndStart();
                int ix = 0;
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    short value = _parseShortPrimitive(jp, ctxt);
                    if (ix >= chunk.length) {
                        chunk = (short[]) builder.appendCompletedChunk(chunk, ix);
                        ix = 0;
                    }
                    int ix2 = ix + 1;
                    chunk[ix] = value;
                    ix = ix2;
                }
                return (short[]) builder.completeAndClearBuffer(chunk, ix);
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    @JacksonStdImpl
    static final class StringDeser extends ArrayDeser<String[]> {
        public StringDeser() {
            super(String[].class);
        }

        public String[] deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (jp.isExpectedStartArrayToken()) {
                ObjectBuffer buffer = ctxt.leaseObjectBuffer();
                Object[] chunk = buffer.resetAndStart();
                int ix = 0;
                while (true) {
                    JsonToken t = jp.nextToken();
                    if (t != JsonToken.END_ARRAY) {
                        String value = t == JsonToken.VALUE_NULL ? null : jp.getText();
                        if (ix >= chunk.length) {
                            chunk = buffer.appendCompletedChunk(chunk);
                            ix = 0;
                        }
                        int ix2 = ix + 1;
                        chunk[ix] = value;
                        ix = ix2;
                    } else {
                        String[] result = (String[]) buffer.completeAndClearBuffer(chunk, ix, String.class);
                        ctxt.returnObjectBuffer(buffer);
                        return result;
                    }
                }
            }
            throw ctxt.mappingException(this._valueClass);
        }
    }

    static {
        instance = new ArrayDeserializers();
    }

    private ArrayDeserializers() {
        this._allDeserializers = new HashMap();
        add(Boolean.TYPE, new BooleanDeser());
        add(Byte.TYPE, new ByteDeser());
        add(Short.TYPE, new ShortDeser());
        add(Integer.TYPE, new IntDeser());
        add(Long.TYPE, new LongDeser());
        add(Float.TYPE, new FloatDeser());
        add(Double.TYPE, new DoubleDeser());
        add(String.class, new StringDeser());
        add(Character.TYPE, new CharDeser());
    }

    public static HashMap<JavaType, JsonDeserializer<Object>> getAll() {
        return instance._allDeserializers;
    }

    private void add(Class<?> cls, JsonDeserializer<?> deser) {
        this._allDeserializers.put(TypeFactory.type((Type) cls), deser);
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
    }
}
