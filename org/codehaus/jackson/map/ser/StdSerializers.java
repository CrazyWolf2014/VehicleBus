package org.codehaus.jackson.map.ser;

import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSerializableSchema;
import org.codehaus.jackson.util.TokenBuffer;
import org.jivesoftware.smackx.FormField;

public class StdSerializers {

    @JacksonStdImpl
    public static final class SerializableSerializer extends SerializerBase<JsonSerializable> {
        protected static final SerializableSerializer instance;

        static {
            instance = new SerializableSerializer();
        }

        private SerializableSerializer() {
            super(JsonSerializable.class);
        }

        public void serialize(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            value.serialize(jgen, provider);
        }

        public final void serializeWithType(JsonSerializable value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            if (value instanceof JsonSerializableWithType) {
                ((JsonSerializableWithType) value).serializeWithType(jgen, provider, typeSer);
            } else {
                serialize(value, jgen, provider);
            }
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode objectNode = createObjectNode();
            String schemaType = "any";
            String objectProperties = null;
            String itemDefinition = null;
            if (typeHint != null) {
                Class<?> rawClass = TypeFactory.type(typeHint).getRawClass();
                if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
                    JsonSerializableSchema schemaInfo = (JsonSerializableSchema) rawClass.getAnnotation(JsonSerializableSchema.class);
                    schemaType = schemaInfo.schemaType();
                    if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
                        objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
                    }
                    if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
                        itemDefinition = schemaInfo.schemaItemDefinition();
                    }
                }
            }
            objectNode.put(SharedPref.TYPE, schemaType);
            if (objectProperties != null) {
                try {
                    objectNode.put("properties", (JsonNode) new ObjectMapper().readValue(objectProperties, JsonNode.class));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (itemDefinition != null) {
                try {
                    objectNode.put("items", (JsonNode) new ObjectMapper().readValue(itemDefinition, JsonNode.class));
                } catch (IOException e2) {
                    throw new IllegalStateException(e2);
                }
            }
            objectNode.put("optional", true);
            return objectNode;
        }
    }

    @JacksonStdImpl
    public static final class SerializableWithTypeSerializer extends SerializerBase<JsonSerializableWithType> {
        protected static final SerializableWithTypeSerializer instance;

        static {
            instance = new SerializableWithTypeSerializer();
        }

        private SerializableWithTypeSerializer() {
            super(JsonSerializableWithType.class);
        }

        public void serialize(JsonSerializableWithType value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            value.serialize(jgen, provider);
        }

        public final void serializeWithType(JsonSerializableWithType value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            value.serializeWithType(jgen, provider, typeSer);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode objectNode = createObjectNode();
            String schemaType = "any";
            String objectProperties = null;
            String itemDefinition = null;
            if (typeHint != null) {
                Class<?> rawClass = TypeFactory.type(typeHint).getRawClass();
                if (rawClass.isAnnotationPresent(JsonSerializableSchema.class)) {
                    JsonSerializableSchema schemaInfo = (JsonSerializableSchema) rawClass.getAnnotation(JsonSerializableSchema.class);
                    schemaType = schemaInfo.schemaType();
                    if (!"##irrelevant".equals(schemaInfo.schemaObjectPropertiesDefinition())) {
                        objectProperties = schemaInfo.schemaObjectPropertiesDefinition();
                    }
                    if (!"##irrelevant".equals(schemaInfo.schemaItemDefinition())) {
                        itemDefinition = schemaInfo.schemaItemDefinition();
                    }
                }
            }
            objectNode.put(SharedPref.TYPE, schemaType);
            if (objectProperties != null) {
                try {
                    objectNode.put("properties", (JsonNode) new ObjectMapper().readValue(objectProperties, JsonNode.class));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            if (itemDefinition != null) {
                try {
                    objectNode.put("items", (JsonNode) new ObjectMapper().readValue(itemDefinition, JsonNode.class));
                } catch (IOException e2) {
                    throw new IllegalStateException(e2);
                }
            }
            objectNode.put("optional", true);
            return objectNode;
        }
    }

    @JacksonStdImpl
    public static final class TokenBufferSerializer extends SerializerBase<TokenBuffer> {
        public TokenBufferSerializer() {
            super(TokenBuffer.class);
        }

        public void serialize(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            value.serialize(jgen);
        }

        public final void serializeWithType(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            typeSer.writeTypePrefixForScalar(value, jgen);
            serialize(value, jgen, provider);
            typeSer.writeTypeSuffixForScalar(value, jgen);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("any", true);
        }
    }

    @JacksonStdImpl
    public static final class CalendarSerializer extends ScalarSerializerBase<Calendar> {
        public static final CalendarSerializer instance;

        static {
            instance = new CalendarSerializer();
        }

        public CalendarSerializer() {
            super(Calendar.class);
        }

        public void serialize(Calendar value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            provider.defaultSerializeDateValue(value.getTimeInMillis(), jgen);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS) ? "number" : "string", true);
        }
    }

    @JacksonStdImpl
    public static final class FloatSerializer extends ScalarSerializerBase<Float> {
        static final FloatSerializer instance;

        static {
            instance = new FloatSerializer();
        }

        public FloatSerializer() {
            super(Float.class);
        }

        public void serialize(Float value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.floatValue());
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }
    }

    @JacksonStdImpl
    public static final class IntLikeSerializer extends ScalarSerializerBase<Number> {
        static final IntLikeSerializer instance;

        static {
            instance = new IntLikeSerializer();
        }

        public IntLikeSerializer() {
            super(Number.class);
        }

        public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.intValue());
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("integer", true);
        }
    }

    @JacksonStdImpl
    public static final class LongSerializer extends ScalarSerializerBase<Long> {
        static final LongSerializer instance;

        static {
            instance = new LongSerializer();
        }

        public LongSerializer() {
            super(Long.class);
        }

        public void serialize(Long value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.longValue());
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }
    }

    protected static abstract class NonTypedScalarSerializer<T> extends ScalarSerializerBase<T> {
        protected NonTypedScalarSerializer(Class<T> t) {
            super(t);
        }

        public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            serialize(value, jgen, provider);
        }
    }

    @JacksonStdImpl
    public static final class NumberSerializer extends ScalarSerializerBase<Number> {
        public static final NumberSerializer instance;

        static {
            instance = new NumberSerializer();
        }

        public NumberSerializer() {
            super(Number.class);
        }

        public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (value instanceof BigDecimal) {
                jgen.writeNumber((BigDecimal) value);
            } else if (value instanceof BigInteger) {
                jgen.writeNumber((BigInteger) value);
            } else if (value instanceof Double) {
                jgen.writeNumber(((Double) value).doubleValue());
            } else if (value instanceof Float) {
                jgen.writeNumber(((Float) value).floatValue());
            } else {
                jgen.writeNumber(value.toString());
            }
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }
    }

    @JacksonStdImpl
    public static final class SqlDateSerializer extends ScalarSerializerBase<Date> {
        public SqlDateSerializer() {
            super(Date.class);
        }

        public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(value.toString());
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }
    }

    @JacksonStdImpl
    public static final class SqlTimeSerializer extends ScalarSerializerBase<Time> {
        public SqlTimeSerializer() {
            super(Time.class);
        }

        public void serialize(Time value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(value.toString());
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }
    }

    @JacksonStdImpl
    public static final class UtilDateSerializer extends ScalarSerializerBase<java.util.Date> {
        public static final UtilDateSerializer instance;

        static {
            instance = new UtilDateSerializer();
        }

        public UtilDateSerializer() {
            super(java.util.Date.class);
        }

        public void serialize(java.util.Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            provider.defaultSerializeDateValue(value, jgen);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(provider.isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS) ? "number" : "string", true);
        }
    }

    @JacksonStdImpl
    public static final class BooleanSerializer extends NonTypedScalarSerializer<Boolean> {
        final boolean _forPrimitive;

        public BooleanSerializer(boolean forPrimitive) {
            super(Boolean.class);
            this._forPrimitive = forPrimitive;
        }

        public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeBoolean(value.booleanValue());
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode(FormField.TYPE_BOOLEAN, !this._forPrimitive);
        }
    }

    @JacksonStdImpl
    public static final class DoubleSerializer extends NonTypedScalarSerializer<Double> {
        static final DoubleSerializer instance;

        static {
            instance = new DoubleSerializer();
        }

        public DoubleSerializer() {
            super(Double.class);
        }

        public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.doubleValue());
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("number", true);
        }
    }

    @JacksonStdImpl
    public static final class IntegerSerializer extends NonTypedScalarSerializer<Integer> {
        public IntegerSerializer() {
            super(Integer.class);
        }

        public void serialize(Integer value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeNumber(value.intValue());
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("integer", true);
        }
    }

    @JacksonStdImpl
    public static final class StringSerializer extends NonTypedScalarSerializer<String> {
        public StringSerializer() {
            super(String.class);
        }

        public void serialize(String value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeString(value);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
            return createSchemaNode("string", true);
        }
    }

    protected StdSerializers() {
    }
}
