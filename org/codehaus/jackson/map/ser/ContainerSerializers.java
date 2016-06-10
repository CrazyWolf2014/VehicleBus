package org.codehaus.jackson.map.ser;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ResolvableSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JacksonStdImpl;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.schema.JsonSchema;
import org.codehaus.jackson.schema.SchemaAware;
import org.codehaus.jackson.type.JavaType;

public final class ContainerSerializers {

    public static abstract class AsArraySerializer<T> extends ContainerSerializerBase<T> implements ResolvableSerializer {
        protected PropertySerializerMap _dynamicSerializers;
        protected JsonSerializer<Object> _elementSerializer;
        protected final JavaType _elementType;
        protected final BeanProperty _property;
        protected final boolean _staticTyping;
        protected final TypeSerializer _valueTypeSerializer;

        protected abstract void serializeContents(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException;

        protected AsArraySerializer(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            boolean z = false;
            super(cls, false);
            this._elementType = et;
            if (staticTyping || (et != null && et.isFinal())) {
                z = true;
            }
            this._staticTyping = z;
            this._valueTypeSerializer = vts;
            this._property = property;
            this._dynamicSerializers = PropertySerializerMap.emptyMap();
        }

        public final void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            jgen.writeStartArray();
            serializeContents(value, jgen, provider);
            jgen.writeEndArray();
        }

        public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer) throws IOException, JsonGenerationException {
            typeSer.writeTypePrefixForArray(value, jgen);
            serializeContents(value, jgen, provider);
            typeSer.writeTypeSuffixForArray(value, jgen);
        }

        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            ObjectNode o = createSchemaNode("array", true);
            JavaType contentType = null;
            if (typeHint != null) {
                contentType = TypeFactory.type(typeHint).getContentType();
                if (contentType == null && (typeHint instanceof ParameterizedType)) {
                    Type[] typeArgs = ((ParameterizedType) typeHint).getActualTypeArguments();
                    if (typeArgs.length == 1) {
                        contentType = TypeFactory.type(typeArgs[0]);
                    }
                }
            }
            if (contentType == null && this._elementType != null) {
                contentType = this._elementType;
            }
            if (contentType != null) {
                JsonNode schemaNode = null;
                if (contentType.getRawClass() != Object.class) {
                    JsonSerializer<Object> ser = provider.findValueSerializer(contentType, this._property);
                    if (ser instanceof SchemaAware) {
                        schemaNode = ((SchemaAware) ser).getSchema(provider, null);
                    }
                }
                if (schemaNode == null) {
                    schemaNode = JsonSchema.getDefaultSchemaNode();
                }
                o.put("items", schemaNode);
            }
            return o;
        }

        public void resolve(SerializerProvider provider) throws JsonMappingException {
            if (this._staticTyping && this._elementType != null) {
                this._elementSerializer = provider.findValueSerializer(this._elementType, this._property);
            }
        }

        protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider) throws JsonMappingException {
            SerializerAndMapResult result = map.findAndAddSerializer((Class) type, provider, this._property);
            if (map != result.map) {
                this._dynamicSerializers = result.map;
            }
            return result.serializer;
        }

        protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider) throws JsonMappingException {
            SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
            if (map != result.map) {
                this._dynamicSerializers = result.map;
            }
            return result.serializer;
        }
    }

    @JacksonStdImpl
    public static class CollectionSerializer extends AsArraySerializer<Collection<?>> {
        public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            super(Collection.class, elemType, staticTyping, vts, property);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new CollectionSerializer(this._elementType, this._staticTyping, vts, this._property);
        }

        public void serializeContents(Collection<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (this._elementSerializer != null) {
                serializeContentsUsing(value, jgen, provider, this._elementSerializer);
                return;
            }
            Iterator<?> it = value.iterator();
            if (it.hasNext()) {
                PropertySerializerMap serializers = this._dynamicSerializers;
                TypeSerializer typeSer = this._valueTypeSerializer;
                int i = 0;
                do {
                    Object elem = it.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    } else {
                        Class<?> cc = elem.getClass();
                        JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                        if (serializer == null) {
                            if (this._elementType.hasGenericTypes()) {
                                serializer = _findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                            } else {
                                try {
                                    serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                                } catch (Exception e) {
                                    wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                                    return;
                                }
                            }
                        }
                        if (typeSer == null) {
                            serializer.serialize(elem, jgen, provider);
                        } else {
                            serializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                    i++;
                } while (it.hasNext());
            }
        }

        public void serializeContentsUsing(Collection<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
            Iterator<?> it = value.iterator();
            if (it.hasNext()) {
                TypeSerializer typeSer = this._valueTypeSerializer;
                int i = 0;
                do {
                    Object elem = it.next();
                    if (elem == null) {
                        try {
                            provider.defaultSerializeNull(jgen);
                        } catch (Exception e) {
                            wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                        }
                    } else if (typeSer == null) {
                        ser.serialize(elem, jgen, provider);
                    } else {
                        ser.serializeWithType(elem, jgen, provider, typeSer);
                    }
                    i++;
                } while (it.hasNext());
            }
        }
    }

    public static class EnumSetSerializer extends AsArraySerializer<EnumSet<? extends Enum<?>>> {
        public EnumSetSerializer(JavaType elemType, BeanProperty property) {
            super(EnumSet.class, elemType, true, null, property);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return this;
        }

        public void serializeContents(EnumSet<? extends Enum<?>> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            JsonSerializer<Object> enumSer = this._elementSerializer;
            Iterator i$ = value.iterator();
            while (i$.hasNext()) {
                Enum<?> en = (Enum) i$.next();
                if (enumSer == null) {
                    enumSer = provider.findValueSerializer(en.getDeclaringClass(), this._property);
                }
                enumSer.serialize(en, jgen, provider);
            }
        }
    }

    @JacksonStdImpl
    public static class IndexedListSerializer extends AsArraySerializer<List<?>> {
        public IndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            super(List.class, elemType, staticTyping, vts, property);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new IndexedListSerializer(this._elementType, this._staticTyping, vts, this._property);
        }

        public void serializeContents(List<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (this._elementSerializer != null) {
                serializeContentsUsing(value, jgen, provider, this._elementSerializer);
            } else if (this._valueTypeSerializer != null) {
                serializeTypedContents(value, jgen, provider);
            } else {
                int len = value.size();
                if (len != 0) {
                    int i = 0;
                    try {
                        PropertySerializerMap serializers = this._dynamicSerializers;
                        while (i < len) {
                            Object elem = value.get(i);
                            if (elem == null) {
                                provider.defaultSerializeNull(jgen);
                            } else {
                                Class<?> cc = elem.getClass();
                                JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                                if (serializer == null) {
                                    if (this._elementType.hasGenericTypes()) {
                                        serializer = _findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                                    } else {
                                        serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                                    }
                                }
                                serializer.serialize(elem, jgen, provider);
                            }
                            i++;
                        }
                    } catch (Exception e) {
                        wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                    }
                }
            }
        }

        public void serializeContentsUsing(List<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser) throws IOException, JsonGenerationException {
            int len = value.size();
            if (len != 0) {
                TypeSerializer typeSer = this._valueTypeSerializer;
                for (int i = 0; i < len; i++) {
                    Object elem = value.get(i);
                    if (elem == null) {
                        try {
                            provider.defaultSerializeNull(jgen);
                        } catch (Exception e) {
                            wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                        }
                    } else if (typeSer == null) {
                        ser.serialize(elem, jgen, provider);
                    } else {
                        ser.serializeWithType(elem, jgen, provider, typeSer);
                    }
                }
            }
        }

        public void serializeTypedContents(List<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            int len = value.size();
            if (len != 0) {
                int i = 0;
                try {
                    TypeSerializer typeSer = this._valueTypeSerializer;
                    PropertySerializerMap serializers = this._dynamicSerializers;
                    while (i < len) {
                        Object elem = value.get(i);
                        if (elem == null) {
                            provider.defaultSerializeNull(jgen);
                        } else {
                            Class<?> cc = elem.getClass();
                            JsonSerializer<Object> serializer = serializers.serializerFor(cc);
                            if (serializer == null) {
                                if (this._elementType.hasGenericTypes()) {
                                    serializer = _findAndAddDynamic(serializers, this._elementType.forcedNarrowBy(cc), provider);
                                } else {
                                    serializer = _findAndAddDynamic(serializers, (Class) cc, provider);
                                }
                            }
                            serializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                        i++;
                    }
                } catch (Exception e) {
                    wrapAndThrow(provider, (Throwable) e, (Object) value, i);
                }
            }
        }
    }

    @JacksonStdImpl
    public static class IterableSerializer extends AsArraySerializer<Iterable<?>> {
        public IterableSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            super(Iterable.class, elemType, staticTyping, vts, property);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new IterableSerializer(this._elementType, this._staticTyping, vts, this._property);
        }

        public void serializeContents(Iterable<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            Iterator<?> it = value.iterator();
            if (it.hasNext()) {
                TypeSerializer typeSer = this._valueTypeSerializer;
                JsonSerializer<Object> prevSerializer = null;
                Class<?> prevClass = null;
                do {
                    Object elem = it.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    } else {
                        JsonSerializer<Object> currSerializer;
                        Class<?> cc = elem.getClass();
                        if (cc == prevClass) {
                            currSerializer = prevSerializer;
                        } else {
                            currSerializer = provider.findValueSerializer((Class) cc, this._property);
                            prevSerializer = currSerializer;
                            prevClass = cc;
                        }
                        if (typeSer == null) {
                            currSerializer.serialize(elem, jgen, provider);
                        } else {
                            currSerializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                } while (it.hasNext());
            }
        }
    }

    @JacksonStdImpl
    public static class IteratorSerializer extends AsArraySerializer<Iterator<?>> {
        public IteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
            super(Iterator.class, elemType, staticTyping, vts, property);
        }

        public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts) {
            return new IteratorSerializer(this._elementType, this._staticTyping, vts, this._property);
        }

        public void serializeContents(Iterator<?> value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
            if (value.hasNext()) {
                TypeSerializer typeSer = this._valueTypeSerializer;
                JsonSerializer<Object> prevSerializer = null;
                Class<?> prevClass = null;
                do {
                    Object elem = value.next();
                    if (elem == null) {
                        provider.defaultSerializeNull(jgen);
                    } else {
                        JsonSerializer<Object> currSerializer;
                        Class<?> cc = elem.getClass();
                        if (cc == prevClass) {
                            currSerializer = prevSerializer;
                        } else {
                            currSerializer = provider.findValueSerializer((Class) cc, this._property);
                            prevSerializer = currSerializer;
                            prevClass = cc;
                        }
                        if (typeSer == null) {
                            currSerializer.serialize(elem, jgen, provider);
                        } else {
                            currSerializer.serializeWithType(elem, jgen, provider, typeSer);
                        }
                    }
                } while (value.hasNext());
            }
        }
    }

    private ContainerSerializers() {
    }

    public static ContainerSerializerBase<?> indexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        return new IndexedListSerializer(elemType, staticTyping, vts, property);
    }

    public static ContainerSerializerBase<?> collectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        return new CollectionSerializer(elemType, staticTyping, vts, property);
    }

    public static ContainerSerializerBase<?> iteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        return new IteratorSerializer(elemType, staticTyping, vts, property);
    }

    public static ContainerSerializerBase<?> iterableSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property) {
        return new IterableSerializer(elemType, staticTyping, vts, property);
    }

    public static JsonSerializer<?> enumSetSerializer(JavaType enumType, BeanProperty property) {
        return new EnumSetSerializer(enumType, property);
    }
}
