package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.xmlpull.v1.XmlPullParser;

@JacksonStdImpl
public class MapSerializer extends ContainerSerializerBase<Map<?, ?>> implements ResolvableSerializer {
    protected static final JavaType UNSPECIFIED_TYPE;
    protected final HashSet<String> _ignoredEntries;
    protected JsonSerializer<Object> _keySerializer;
    protected final JavaType _keyType;
    protected final BeanProperty _property;
    protected JsonSerializer<Object> _valueSerializer;
    protected final JavaType _valueType;
    protected final boolean _valueTypeIsStatic;
    protected final TypeSerializer _valueTypeSerializer;

    static {
        UNSPECIFIED_TYPE = TypeFactory.fastSimpleType(Object.class);
    }

    protected MapSerializer() {
        this((HashSet) null, null, null, false, null, null, null);
    }

    @Deprecated
    protected MapSerializer(HashSet<String> ignoredEntries, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts) {
        this(ignoredEntries, UNSPECIFIED_TYPE, valueType, valueTypeIsStatic, vts, null, null);
    }

    protected MapSerializer(HashSet<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<Object> keySerializer, BeanProperty property) {
        super(Map.class, false);
        this._property = property;
        this._ignoredEntries = ignoredEntries;
        this._keyType = keyType;
        this._valueType = valueType;
        this._valueTypeIsStatic = valueTypeIsStatic;
        this._valueTypeSerializer = vts;
        this._keySerializer = keySerializer;
    }

    public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
        MapSerializer ms = new MapSerializer(this._ignoredEntries, this._keyType, this._valueType, this._valueTypeIsStatic, vts, this._keySerializer, this._property);
        if (this._valueSerializer != null) {
            ms._valueSerializer = this._valueSerializer;
        }
        return ms;
    }

    public static MapSerializer construct(String[] ignoredList, JavaType mapType, boolean staticValueType, TypeSerializer vts, BeanProperty property) {
        JavaType valueType;
        JavaType keyType;
        HashSet<String> ignoredEntries = toSet(ignoredList);
        if (mapType == null) {
            valueType = UNSPECIFIED_TYPE;
            keyType = valueType;
        } else {
            keyType = mapType.getKeyType();
            valueType = mapType.getContentType();
        }
        if (!staticValueType) {
            staticValueType = valueType != null && valueType.isFinal();
        }
        return new MapSerializer(ignoredEntries, keyType, valueType, staticValueType, vts, null, property);
    }

    private static HashSet<String> toSet(String[] ignoredEntries) {
        if (ignoredEntries == null || ignoredEntries.length == 0) {
            return null;
        }
        HashSet<String> result = new HashSet(ignoredEntries.length);
        for (String prop : ignoredEntries) {
            result.add(prop);
        }
        return result;
    }

    public void serialize(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeStartObject();
        if (!value.isEmpty()) {
            if (this._valueSerializer != null) {
                serializeFieldsUsing(value, jgen, provider, this._valueSerializer);
            } else {
                serializeFields(value, jgen, provider);
            }
        }
        jgen.writeEndObject();
    }

    public void serializeWithType(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
        typeSer.writeTypePrefixForObject(value, jgen);
        if (!value.isEmpty()) {
            if (this._valueSerializer != null) {
                serializeFieldsUsing(value, jgen, provider, this._valueSerializer);
            } else {
                serializeFields(value, jgen, provider);
            }
        }
        typeSer.writeTypeSuffixForObject(value, jgen);
    }

    protected void serializeFields(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (this._valueTypeSerializer != null) {
            serializeTypedFields(value, jgen, provider);
            return;
        }
        JsonSerializer<Object> keySerializer = this._keySerializer;
        JsonSerializer<Object> prevValueSerializer = null;
        Class<?> prevValueClass = null;
        HashSet<String> ignored = this._ignoredEntries;
        boolean skipNulls = !provider.isEnabled(Feature.WRITE_NULL_MAP_VALUES);
        for (Entry<?, ?> entry : value.entrySet()) {
            Object valueElem = entry.getValue();
            Object keyElem = entry.getKey();
            if (keyElem == null) {
                provider.getNullKeySerializer().serialize(null, jgen, provider);
            } else if (!(skipNulls && valueElem == null) && (ignored == null || !ignored.contains(keyElem))) {
                keySerializer.serialize(keyElem, jgen, provider);
            }
            if (valueElem == null) {
                provider.defaultSerializeNull(jgen);
            } else {
                JsonSerializer<Object> currSerializer;
                Class<?> cc = valueElem.getClass();
                if (cc == prevValueClass) {
                    currSerializer = prevValueSerializer;
                } else {
                    currSerializer = provider.findValueSerializer((Class) cc, this._property);
                    prevValueSerializer = currSerializer;
                    prevValueClass = cc;
                }
                try {
                    currSerializer.serialize(valueElem, jgen, provider);
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, XmlPullParser.NO_NAMESPACE + keyElem);
                }
            }
        }
    }

    protected void serializeFieldsUsing(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
        JsonSerializer<Object> keySerializer = this._keySerializer;
        HashSet<String> ignored = this._ignoredEntries;
        TypeSerializer typeSer = this._valueTypeSerializer;
        boolean skipNulls = !provider.isEnabled(Feature.WRITE_NULL_MAP_VALUES);
        for (Entry<?, ?> entry : value.entrySet()) {
            Object valueElem = entry.getValue();
            Object keyElem = entry.getKey();
            if (keyElem == null) {
                provider.getNullKeySerializer().serialize(null, jgen, provider);
            } else if (!(skipNulls && valueElem == null) && (ignored == null || !ignored.contains(keyElem))) {
                keySerializer.serialize(keyElem, jgen, provider);
            }
            if (valueElem == null) {
                provider.defaultSerializeNull(jgen);
            } else if (typeSer == null) {
                try {
                    ser.serialize(valueElem, jgen, provider);
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, XmlPullParser.NO_NAMESPACE + keyElem);
                }
            } else {
                ser.serializeWithType(valueElem, jgen, provider, typeSer);
            }
        }
    }

    protected void serializeTypedFields(Map<?, ?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        JsonSerializer<Object> keySerializer = this._keySerializer;
        JsonSerializer<Object> prevValueSerializer = null;
        Class<?> prevValueClass = null;
        HashSet<String> ignored = this._ignoredEntries;
        boolean skipNulls = !provider.isEnabled(Feature.WRITE_NULL_MAP_VALUES);
        for (Entry<?, ?> entry : value.entrySet()) {
            Object valueElem = entry.getValue();
            Object keyElem = entry.getKey();
            if (keyElem == null) {
                provider.getNullKeySerializer().serialize(null, jgen, provider);
            } else if (!(skipNulls && valueElem == null) && (ignored == null || !ignored.contains(keyElem))) {
                keySerializer.serialize(keyElem, jgen, provider);
            }
            if (valueElem == null) {
                provider.defaultSerializeNull(jgen);
            } else {
                JsonSerializer<Object> currSerializer;
                Class<?> cc = valueElem.getClass();
                if (cc == prevValueClass) {
                    currSerializer = prevValueSerializer;
                } else {
                    currSerializer = provider.findValueSerializer((Class) cc, this._property);
                    prevValueSerializer = currSerializer;
                    prevValueClass = cc;
                }
                try {
                    currSerializer.serializeWithType(valueElem, jgen, provider, this._valueTypeSerializer);
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, XmlPullParser.NO_NAMESPACE + keyElem);
                }
            }
        }
    }

    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("object", true);
    }

    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if (this._valueTypeIsStatic) {
            this._valueSerializer = provider.findValueSerializer(this._valueType, this._property);
        }
        this._keySerializer = provider.getKeySerializer(this._keyType, this._property);
    }
}
