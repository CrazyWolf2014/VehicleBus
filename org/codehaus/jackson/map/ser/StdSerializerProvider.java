package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.ContextualSerializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.ser.impl.ReadOnlyClassToSerializerMap;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.RootNameLookup;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

public class StdSerializerProvider extends SerializerProvider {
    static final boolean CACHE_UNKNOWN_MAPPINGS = false;
    public static final JsonSerializer<Object> DEFAULT_KEY_SERIALIZER;
    public static final JsonSerializer<Object> DEFAULT_NULL_KEY_SERIALIZER;
    public static final JsonSerializer<Object> DEFAULT_UNKNOWN_SERIALIZER;
    protected DateFormat _dateFormat;
    protected JsonSerializer<Object> _keySerializer;
    protected final ReadOnlyClassToSerializerMap _knownSerializers;
    protected JsonSerializer<Object> _nullKeySerializer;
    protected JsonSerializer<Object> _nullValueSerializer;
    protected final RootNameLookup _rootNames;
    protected final SerializerCache _serializerCache;
    protected final SerializerFactory _serializerFactory;
    protected JsonSerializer<Object> _unknownTypeSerializer;

    private static final class WrappedSerializer extends JsonSerializer<Object> {
        protected final JsonSerializer<Object> _serializer;
        protected final TypeSerializer _typeSerializer;

        public WrappedSerializer(TypeSerializer typeSer, JsonSerializer<Object> ser) {
            this._typeSerializer = typeSer;
            this._serializer = ser;
        }

        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            this._serializer.serializeWithType(value, jgen, provider, this._typeSerializer);
        }

        public void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonProcessingException {
            this._serializer.serializeWithType(value, jgen, provider, typeSer);
        }

        public Class<Object> handledType() {
            return Object.class;
        }
    }

    /* renamed from: org.codehaus.jackson.map.ser.StdSerializerProvider.1 */
    static class C12811 extends SerializerBase<Object> {
        C12811(Class x0) {
            super(x0);
        }

        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonMappingException {
            if (provider.isEnabled(Feature.FAIL_ON_EMPTY_BEANS)) {
                failForEmpty(value);
            }
            jgen.writeStartObject();
            jgen.writeEndObject();
        }

        public final void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            if (provider.isEnabled(Feature.FAIL_ON_EMPTY_BEANS)) {
                failForEmpty(value);
            }
            typeSer.writeTypePrefixForObject(value, jgen);
            typeSer.writeTypeSuffixForObject(value, jgen);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            return null;
        }

        protected void failForEmpty(Object value) throws JsonMappingException {
            throw new JsonMappingException("No serializer found for class " + value.getClass().getName() + " and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS) )");
        }
    }

    static {
        DEFAULT_NULL_KEY_SERIALIZER = new FailingSerializer("Null key for a Map not allowed in Json (use a converting NullKeySerializer?)");
        DEFAULT_KEY_SERIALIZER = new StdKeySerializer();
        DEFAULT_UNKNOWN_SERIALIZER = new C12811(Object.class);
    }

    public StdSerializerProvider() {
        super(null);
        this._unknownTypeSerializer = DEFAULT_UNKNOWN_SERIALIZER;
        this._keySerializer = DEFAULT_KEY_SERIALIZER;
        this._nullValueSerializer = NullSerializer.instance;
        this._nullKeySerializer = DEFAULT_NULL_KEY_SERIALIZER;
        this._serializerFactory = null;
        this._serializerCache = new SerializerCache();
        this._knownSerializers = null;
        this._rootNames = new RootNameLookup();
    }

    protected StdSerializerProvider(SerializationConfig config, StdSerializerProvider src, SerializerFactory f) {
        super(config);
        this._unknownTypeSerializer = DEFAULT_UNKNOWN_SERIALIZER;
        this._keySerializer = DEFAULT_KEY_SERIALIZER;
        this._nullValueSerializer = NullSerializer.instance;
        this._nullKeySerializer = DEFAULT_NULL_KEY_SERIALIZER;
        if (config == null) {
            throw new NullPointerException();
        }
        this._serializerFactory = f;
        this._serializerCache = src._serializerCache;
        this._unknownTypeSerializer = src._unknownTypeSerializer;
        this._keySerializer = src._keySerializer;
        this._nullValueSerializer = src._nullValueSerializer;
        this._nullKeySerializer = src._nullKeySerializer;
        this._rootNames = src._rootNames;
        this._knownSerializers = this._serializerCache.getReadOnlyLookupMap();
    }

    protected StdSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new StdSerializerProvider(config, this, jsf);
    }

    public final void serializeValue(SerializationConfig config, JsonGenerator jgen, Object value, SerializerFactory jsf) throws IOException, JsonGenerationException {
        if (jsf == null) {
            throw new IllegalArgumentException("Can not pass null serializerFactory");
        }
        StdSerializerProvider inst = createInstance(config, jsf);
        if (inst.getClass() != getClass()) {
            throw new IllegalStateException("Broken serializer provider: createInstance returned instance of type " + inst.getClass() + "; blueprint of type " + getClass());
        }
        inst._serializeValue(jgen, value);
    }

    public final void serializeValue(SerializationConfig config, JsonGenerator jgen, Object value, JavaType rootType, SerializerFactory jsf) throws IOException, JsonGenerationException {
        if (jsf == null) {
            throw new IllegalArgumentException("Can not pass null serializerFactory");
        }
        StdSerializerProvider inst = createInstance(config, jsf);
        if (inst.getClass() != getClass()) {
            throw new IllegalStateException("Broken serializer provider: createInstance returned instance of type " + inst.getClass() + "; blueprint of type " + getClass());
        }
        inst._serializeValue(jgen, value, rootType);
    }

    public JsonSchema generateJsonSchema(Class<?> type, SerializationConfig config, SerializerFactory jsf) throws JsonMappingException {
        if (type == null) {
            throw new IllegalArgumentException("A class must be provided");
        }
        StdSerializerProvider inst = createInstance(config, jsf);
        if (inst.getClass() != getClass()) {
            throw new IllegalStateException("Broken serializer provider: createInstance returned instance of type " + inst.getClass() + "; blueprint of type " + getClass());
        }
        JsonSerializer<Object> ser = inst.findValueSerializer((Class) type, null);
        JsonNode schemaNode = ser instanceof SchemaAware ? ((SchemaAware) ser).getSchema(inst, null) : JsonSchema.getDefaultSchemaNode();
        if (schemaNode instanceof ObjectNode) {
            return new JsonSchema((ObjectNode) schemaNode);
        }
        throw new IllegalArgumentException("Class " + type.getName() + " would not be serialized as a JSON object and therefore has no schema");
    }

    public boolean hasSerializerFor(SerializationConfig config, Class<?> cls, SerializerFactory jsf) {
        return createInstance(config, jsf)._findExplicitUntypedSerializer(cls, null) != null;
    }

    public void setKeySerializer(JsonSerializer<Object> ks) {
        if (ks == null) {
            throw new IllegalArgumentException("Can not pass null JsonSerializer");
        }
        this._keySerializer = ks;
    }

    public void setNullValueSerializer(JsonSerializer<Object> nvs) {
        if (nvs == null) {
            throw new IllegalArgumentException("Can not pass null JsonSerializer");
        }
        this._nullValueSerializer = nvs;
    }

    public void setNullKeySerializer(JsonSerializer<Object> nks) {
        if (nks == null) {
            throw new IllegalArgumentException("Can not pass null JsonSerializer");
        }
        this._nullKeySerializer = nks;
    }

    public int cachedSerializersCount() {
        return this._serializerCache.size();
    }

    public void flushCachedSerializers() {
        this._serializerCache.flush();
    }

    public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property) throws JsonMappingException {
        JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer((Class) valueType);
        if (ser == null) {
            ser = this._serializerCache.untypedValueSerializer((Class) valueType);
            if (ser == null) {
                ser = this._serializerCache.untypedValueSerializer(TypeFactory.type((Type) valueType));
                if (ser == null) {
                    ser = _createAndCacheUntypedSerializer((Class) valueType, property);
                    if (ser == null) {
                        return getUnknownTypeSerializer(valueType);
                    }
                }
            }
        }
        return ser instanceof ContextualSerializer ? ((ContextualSerializer) ser).createContextual(this._config, property) : ser;
    }

    public JsonSerializer<Object> findValueSerializer(JavaType valueType, BeanProperty property) throws JsonMappingException {
        JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
        if (ser == null) {
            ser = this._serializerCache.untypedValueSerializer(valueType);
            if (ser == null) {
                ser = _createAndCacheUntypedSerializer(valueType, property);
                if (ser == null) {
                    return getUnknownTypeSerializer(valueType.getRawClass());
                }
            }
        }
        return ser instanceof ContextualSerializer ? ((ContextualSerializer) ser).createContextual(this._config, property) : ser;
    }

    public JsonSerializer<Object> findTypedValueSerializer(Class<?> valueType, boolean cache, BeanProperty property) throws JsonMappingException {
        JsonSerializer<Object> ser = this._knownSerializers.typedValueSerializer((Class) valueType);
        if (ser != null) {
            return ser;
        }
        ser = this._serializerCache.typedValueSerializer((Class) valueType);
        if (ser != null) {
            return ser;
        }
        ser = findValueSerializer((Class) valueType, property);
        TypeSerializer typeSer = this._serializerFactory.createTypeSerializer(this._config, TypeFactory.type((Type) valueType), property);
        if (typeSer != null) {
            ser = new WrappedSerializer(typeSer, ser);
        }
        if (cache) {
            this._serializerCache.addTypedSerializer((Class) valueType, (JsonSerializer) ser);
        }
        return ser;
    }

    public JsonSerializer<Object> findTypedValueSerializer(JavaType valueType, boolean cache, BeanProperty property) throws JsonMappingException {
        JsonSerializer<Object> ser = this._knownSerializers.typedValueSerializer(valueType);
        if (ser != null) {
            return ser;
        }
        ser = this._serializerCache.typedValueSerializer(valueType);
        if (ser != null) {
            return ser;
        }
        ser = findValueSerializer(valueType, property);
        TypeSerializer typeSer = this._serializerFactory.createTypeSerializer(this._config, valueType, property);
        if (typeSer != null) {
            ser = new WrappedSerializer(typeSer, ser);
        }
        if (cache) {
            this._serializerCache.addTypedSerializer(valueType, (JsonSerializer) ser);
        }
        return ser;
    }

    public JsonSerializer<Object> getKeySerializer(JavaType valueType, BeanProperty property) {
        return this._keySerializer;
    }

    public JsonSerializer<Object> getNullKeySerializer() {
        return this._nullKeySerializer;
    }

    public JsonSerializer<Object> getNullValueSerializer() {
        return this._nullValueSerializer;
    }

    public JsonSerializer<Object> getUnknownTypeSerializer(Class<?> cls) {
        return this._unknownTypeSerializer;
    }

    public final void defaultSerializeDateValue(long timestamp, JsonGenerator jgen) throws IOException, JsonProcessingException {
        if (isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS)) {
            jgen.writeNumber(timestamp);
            return;
        }
        if (this._dateFormat == null) {
            this._dateFormat = (DateFormat) this._config.getDateFormat().clone();
        }
        jgen.writeString(this._dateFormat.format(new Date(timestamp)));
    }

    public final void defaultSerializeDateValue(Date date, JsonGenerator jgen) throws IOException, JsonProcessingException {
        if (isEnabled(Feature.WRITE_DATES_AS_TIMESTAMPS)) {
            jgen.writeNumber(date.getTime());
            return;
        }
        if (this._dateFormat == null) {
            this._dateFormat = (DateFormat) this._config.getDateFormat().clone();
        }
        jgen.writeString(this._dateFormat.format(date));
    }

    protected void _serializeValue(JsonGenerator jgen, Object value) throws IOException, JsonProcessingException {
        JsonSerializer<Object> ser;
        boolean wrap;
        if (value == null) {
            ser = getNullValueSerializer();
            wrap = false;
        } else {
            ser = findTypedValueSerializer(value.getClass(), true, null);
            wrap = this._config.isEnabled(Feature.WRAP_ROOT_VALUE);
            if (wrap) {
                jgen.writeStartObject();
                jgen.writeFieldName(this._rootNames.findRootName(value.getClass(), this._config));
            }
        }
        try {
            ser.serialize(value, jgen, this);
            if (wrap) {
                jgen.writeEndObject();
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = "[no message for " + e.getClass().getName() + "]";
            }
            throw new JsonMappingException(msg, e);
        }
    }

    protected void _serializeValue(JsonGenerator jgen, Object value, JavaType rootType) throws IOException, JsonProcessingException {
        JsonSerializer<Object> ser;
        boolean wrap;
        if (value == null) {
            ser = getNullValueSerializer();
            wrap = false;
        } else {
            if (!rootType.getRawClass().isAssignableFrom(value.getClass())) {
                _reportIncompatibleRootType(value, rootType);
            }
            ser = findTypedValueSerializer(rootType, true, null);
            wrap = this._config.isEnabled(Feature.WRAP_ROOT_VALUE);
            if (wrap) {
                jgen.writeStartObject();
                jgen.writeFieldName(this._rootNames.findRootName(rootType, this._config));
            }
        }
        try {
            ser.serialize(value, jgen, this);
            if (wrap) {
                jgen.writeEndObject();
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Throwable e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = "[no message for " + e.getClass().getName() + "]";
            }
            throw new JsonMappingException(msg, e);
        }
    }

    protected void _reportIncompatibleRootType(Object value, JavaType rootType) throws IOException, JsonProcessingException {
        if (!rootType.isPrimitive() || !ClassUtil.wrapperType(rootType.getRawClass()).isAssignableFrom(value.getClass())) {
            throw new JsonMappingException("Incompatible types: declared root type (" + rootType + ") vs " + value.getClass().getName());
        }
    }

    protected JsonSerializer<Object> _findExplicitUntypedSerializer(Class<?> runtimeType, BeanProperty property) {
        JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer((Class) runtimeType);
        if (ser != null) {
            return ser;
        }
        ser = this._serializerCache.untypedValueSerializer((Class) runtimeType);
        if (ser != null) {
            return ser;
        }
        try {
            return _createAndCacheUntypedSerializer((Class) runtimeType, property);
        } catch (Exception e) {
            return null;
        }
    }

    protected JsonSerializer<Object> _createAndCacheUntypedSerializer(Class<?> type, BeanProperty property) throws JsonMappingException {
        try {
            JsonSerializer ser = _createUntypedSerializer(TypeFactory.type((Type) type), property);
            if (ser != null) {
                this._serializerCache.addNonTypedSerializer((Class) type, ser);
                if (ser instanceof ResolvableSerializer) {
                    _resolveSerializer((ResolvableSerializer) ser);
                }
            }
            return ser;
        } catch (IllegalArgumentException iae) {
            throw new JsonMappingException(iae.getMessage(), null, iae);
        }
    }

    protected JsonSerializer<Object> _createAndCacheUntypedSerializer(JavaType type, BeanProperty property) throws JsonMappingException {
        try {
            JsonSerializer ser = _createUntypedSerializer(type, property);
            if (ser != null) {
                this._serializerCache.addNonTypedSerializer(type, ser);
                if (ser instanceof ResolvableSerializer) {
                    _resolveSerializer((ResolvableSerializer) ser);
                }
            }
            return ser;
        } catch (IllegalArgumentException iae) {
            throw new JsonMappingException(iae.getMessage(), null, iae);
        }
    }

    protected JsonSerializer<Object> _createUntypedSerializer(JavaType type, BeanProperty property) throws JsonMappingException {
        return this._serializerFactory.createSerializer(this._config, type, property);
    }

    protected void _resolveSerializer(ResolvableSerializer ser) throws JsonMappingException {
        ser.resolve(this);
    }
}
