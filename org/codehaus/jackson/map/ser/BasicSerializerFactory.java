package org.codehaus.jackson.map.ser;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.RandomAccess;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializable;
import org.codehaus.jackson.map.JsonSerializableWithType;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.ext.OptionalHandlerFactory;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.ser.ArraySerializers.BooleanArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.ByteArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.CharArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.DoubleArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.FloatArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.IntArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.LongArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.ShortArraySerializer;
import org.codehaus.jackson.map.ser.ArraySerializers.StringArraySerializer;
import org.codehaus.jackson.map.ser.StdSerializers.BooleanSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.CalendarSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.DoubleSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.FloatSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.IntLikeSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.IntegerSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.LongSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.NumberSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.SerializableSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.SerializableWithTypeSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.SqlDateSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.SqlTimeSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.StringSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.TokenBufferSerializer;
import org.codehaus.jackson.map.ser.StdSerializers.UtilDateSerializer;
import org.codehaus.jackson.map.ser.impl.IndexedStringListSerializer;
import org.codehaus.jackson.map.ser.impl.InetAddressSerializer;
import org.codehaus.jackson.map.ser.impl.ObjectArraySerializer;
import org.codehaus.jackson.map.ser.impl.StringCollectionSerializer;
import org.codehaus.jackson.map.ser.impl.TimeZoneSerializer;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.map.util.EnumValues;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

public abstract class BasicSerializerFactory extends SerializerFactory {
    static final JsonSerializer<?> MARKER_COLLECTION;
    static final JsonSerializer<?> MARKER_INDEXED_LIST;
    static final JsonSerializer<?> MARKER_OBJECT_ARRAY;
    static final JsonSerializer<?> MARKER_OBJECT_MAP;
    static final JsonSerializer<?> MARKER_STRING_ARRAY;
    protected static final HashMap<String, JsonSerializer<?>> _concrete;
    protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy;
    protected OptionalHandlerFactory optionalHandlers;

    private static final class SerializerMarker extends JsonSerializer<Object> {
        private SerializerMarker() {
        }

        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) {
        }
    }

    static {
        _concrete = new HashMap();
        _concreteLazy = new HashMap();
        MARKER_INDEXED_LIST = new SerializerMarker();
        MARKER_COLLECTION = new SerializerMarker();
        MARKER_OBJECT_ARRAY = new SerializerMarker();
        MARKER_STRING_ARRAY = new SerializerMarker();
        MARKER_OBJECT_MAP = new SerializerMarker();
        _concrete.put(String.class.getName(), new StringSerializer());
        ToStringSerializer sls = ToStringSerializer.instance;
        _concrete.put(StringBuffer.class.getName(), sls);
        _concrete.put(StringBuilder.class.getName(), sls);
        _concrete.put(Character.class.getName(), sls);
        _concrete.put(Character.TYPE.getName(), sls);
        _concrete.put(Boolean.TYPE.getName(), new BooleanSerializer(true));
        _concrete.put(Boolean.class.getName(), new BooleanSerializer(false));
        JsonSerializer<?> intS = new IntegerSerializer();
        _concrete.put(Integer.class.getName(), intS);
        _concrete.put(Integer.TYPE.getName(), intS);
        _concrete.put(Long.class.getName(), LongSerializer.instance);
        _concrete.put(Long.TYPE.getName(), LongSerializer.instance);
        _concrete.put(Byte.class.getName(), IntLikeSerializer.instance);
        _concrete.put(Byte.TYPE.getName(), IntLikeSerializer.instance);
        _concrete.put(Short.class.getName(), IntLikeSerializer.instance);
        _concrete.put(Short.TYPE.getName(), IntLikeSerializer.instance);
        _concrete.put(Float.class.getName(), FloatSerializer.instance);
        _concrete.put(Float.TYPE.getName(), FloatSerializer.instance);
        _concrete.put(Double.class.getName(), DoubleSerializer.instance);
        _concrete.put(Double.TYPE.getName(), DoubleSerializer.instance);
        JsonSerializer<?> ns = new NumberSerializer();
        _concrete.put(BigInteger.class.getName(), ns);
        _concrete.put(BigDecimal.class.getName(), ns);
        _concrete.put(Calendar.class.getName(), CalendarSerializer.instance);
        _concrete.put(Date.class.getName(), UtilDateSerializer.instance);
        _concrete.put(java.sql.Date.class.getName(), new SqlDateSerializer());
        _concrete.put(Time.class.getName(), new SqlTimeSerializer());
        _concrete.put(Timestamp.class.getName(), UtilDateSerializer.instance);
        _concrete.put(boolean[].class.getName(), new BooleanArraySerializer());
        _concrete.put(byte[].class.getName(), new ByteArraySerializer());
        _concrete.put(char[].class.getName(), new CharArraySerializer());
        _concrete.put(short[].class.getName(), new ShortArraySerializer());
        _concrete.put(int[].class.getName(), new IntArraySerializer());
        _concrete.put(long[].class.getName(), new LongArraySerializer());
        _concrete.put(float[].class.getName(), new FloatArraySerializer());
        _concrete.put(double[].class.getName(), new DoubleArraySerializer());
        _concrete.put(Object[].class.getName(), MARKER_OBJECT_ARRAY);
        _concrete.put(String[].class.getName(), MARKER_STRING_ARRAY);
        _concrete.put(ArrayList.class.getName(), MARKER_INDEXED_LIST);
        _concrete.put(Vector.class.getName(), MARKER_INDEXED_LIST);
        _concrete.put(LinkedList.class.getName(), MARKER_COLLECTION);
        _concrete.put(HashMap.class.getName(), MARKER_OBJECT_MAP);
        _concrete.put(Hashtable.class.getName(), MARKER_OBJECT_MAP);
        _concrete.put(LinkedHashMap.class.getName(), MARKER_OBJECT_MAP);
        _concrete.put(TreeMap.class.getName(), MARKER_OBJECT_MAP);
        _concrete.put(Properties.class.getName(), MARKER_OBJECT_MAP);
        _concrete.put(HashSet.class.getName(), MARKER_COLLECTION);
        _concrete.put(LinkedHashSet.class.getName(), MARKER_COLLECTION);
        _concrete.put(TreeSet.class.getName(), MARKER_COLLECTION);
        for (Entry<Class<?>, Object> en : new JdkSerializers().provide()) {
            Class<? extends JsonSerializer<?>> value = en.getValue();
            if (value instanceof JsonSerializer) {
                _concrete.put(((Class) en.getKey()).getName(), (JsonSerializer) value);
            } else if (value instanceof Class) {
                _concreteLazy.put(((Class) en.getKey()).getName(), value);
            } else {
                throw new IllegalStateException("Internal error: unrecognized value of type " + en.getClass().getName());
            }
        }
        _concreteLazy.put(TokenBuffer.class.getName(), TokenBufferSerializer.class);
    }

    protected BasicSerializerFactory() {
        this.optionalHandlers = OptionalHandlerFactory.instance;
    }

    public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property) {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        JsonSerializer<?> ser = findSerializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ser == null) {
            ser = findSerializerByLookup(type, config, beanDesc, property);
            if (ser == null) {
                ser = findSerializerByPrimaryType(type, config, beanDesc, property);
                if (ser == null) {
                    ser = findSerializerByAddonType(config, type, beanDesc, property);
                }
            }
        }
        return ser;
    }

    public TypeSerializer createTypeSerializer(SerializationConfig config, JavaType baseType, BeanProperty property) {
        AnnotatedClass ac = ((BasicBeanDescription) config.introspectClassAnnotations(baseType.getRawClass())).getClassInfo();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findTypeResolver(ac, baseType);
        Collection<NamedType> subtypes = null;
        if (b == null) {
            b = config.getDefaultTyper(baseType);
        } else {
            subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, (MapperConfig) config, ai);
        }
        return b == null ? null : b.buildTypeSerializer(baseType, subtypes, property);
    }

    public final JsonSerializer<?> getNullSerializer() {
        return NullSerializer.instance;
    }

    public final JsonSerializer<?> findSerializerByLookup(JavaType type, SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) {
        String clsName = type.getRawClass().getName();
        JsonSerializer<?> ser = (JsonSerializer) _concrete.get(clsName);
        if (ser == null) {
            Class<? extends JsonSerializer<?>> serClass = (Class) _concreteLazy.get(clsName);
            if (serClass != null) {
                try {
                    ser = (JsonSerializer) serClass.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to instantiate standard serializer (of type " + serClass.getName() + "): " + e.getMessage(), e);
                }
            }
        }
        if (ser == null) {
            Class<?> raw = type.getRawClass();
            if (InetAddress.class.isAssignableFrom(raw)) {
                return InetAddressSerializer.instance;
            }
            if (TimeZone.class.isAssignableFrom(raw)) {
                return TimeZoneSerializer.instance;
            }
            ser = this.optionalHandlers.findSerializer(config, type, beanDesc, property);
        } else if (ser == MARKER_OBJECT_MAP) {
            return buildMapSerializer(config, type, beanDesc, property);
        } else {
            if (ser == MARKER_OBJECT_ARRAY) {
                return buildObjectArraySerializer(config, type, beanDesc, property);
            }
            if (ser == MARKER_STRING_ARRAY) {
                return new StringArraySerializer(property);
            }
            if (ser == MARKER_INDEXED_LIST) {
                if (type.getContentType().getRawClass() == String.class) {
                    return new IndexedStringListSerializer(property);
                }
                return buildIndexedListSerializer(config, type, beanDesc, property);
            } else if (ser == MARKER_COLLECTION) {
                if (type.getContentType().getRawClass() == String.class) {
                    return new StringCollectionSerializer(property);
                }
                return buildCollectionSerializer(config, type, beanDesc, property);
            }
        }
        return ser;
    }

    public final JsonSerializer<?> findSerializerByPrimaryType(JavaType type, SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) {
        Class<?> cls = type.getRawClass();
        if (JsonSerializable.class.isAssignableFrom(cls)) {
            if (JsonSerializableWithType.class.isAssignableFrom(cls)) {
                return SerializableWithTypeSerializer.instance;
            }
            return SerializableSerializer.instance;
        } else if (Map.class.isAssignableFrom(cls)) {
            if (EnumMap.class.isAssignableFrom(cls)) {
                return buildEnumMapSerializer(config, type, beanDesc, property);
            }
            return buildMapSerializer(config, type, beanDesc, property);
        } else if (Object[].class.isAssignableFrom(cls)) {
            return buildObjectArraySerializer(config, type, beanDesc, property);
        } else {
            if (!List.class.isAssignableFrom(cls)) {
                AnnotatedMethod valueMethod = beanDesc.findJsonValueMethod();
                if (valueMethod != null) {
                    return new JsonValueSerializer(valueMethod.getAnnotated(), findSerializerFromAnnotation(config, valueMethod, property), property);
                } else if (Number.class.isAssignableFrom(cls)) {
                    return NumberSerializer.instance;
                } else {
                    if (Enum.class.isAssignableFrom(cls)) {
                        return EnumSerializer.construct(cls, config, beanDesc);
                    }
                    if (Calendar.class.isAssignableFrom(cls)) {
                        return CalendarSerializer.instance;
                    }
                    if (Date.class.isAssignableFrom(cls)) {
                        return UtilDateSerializer.instance;
                    }
                    if (!Collection.class.isAssignableFrom(cls)) {
                        return null;
                    }
                    if (EnumSet.class.isAssignableFrom(cls)) {
                        return buildEnumSetSerializer(config, type, beanDesc, property);
                    }
                    return buildCollectionSerializer(config, type, beanDesc, property);
                }
            } else if (cls == List.class || cls == AbstractList.class || RandomAccess.class.isAssignableFrom(cls)) {
                return buildIndexedListSerializer(config, type, beanDesc, property);
            } else {
                return buildCollectionSerializer(config, type, beanDesc, property);
            }
        }
    }

    public final JsonSerializer<?> findSerializerByAddonType(SerializationConfig config, JavaType javaType, BasicBeanDescription beanDesc, BeanProperty property) {
        Class<?> type = javaType.getRawClass();
        if (Iterator.class.isAssignableFrom(type)) {
            return buildIteratorSerializer(config, javaType, beanDesc, property);
        }
        if (Iterable.class.isAssignableFrom(type)) {
            return buildIterableSerializer(config, javaType, beanDesc, property);
        }
        if (CharSequence.class.isAssignableFrom(type)) {
            return ToStringSerializer.instance;
        }
        return null;
    }

    protected JsonSerializer<Object> findSerializerFromAnnotation(SerializationConfig config, Annotated a, BeanProperty property) {
        Class<?> serDef = config.getAnnotationIntrospector().findSerializer(a, property);
        if (serDef == null) {
            return null;
        }
        if (serDef instanceof JsonSerializer) {
            return (JsonSerializer) serDef;
        }
        if (serDef instanceof Class) {
            Class<?> cls = serDef;
            if (JsonSerializer.class.isAssignableFrom(cls)) {
                return (JsonSerializer) ClassUtil.createInstance(cls, config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
            }
            throw new IllegalStateException("AnnotationIntrospector returned Class " + cls.getName() + "; expected Class<JsonSerializer>");
        }
        throw new IllegalStateException("AnnotationIntrospector returned value of type " + serDef.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
    }

    protected JsonSerializer<?> buildMapSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        TypeSerializer vts = createTypeSerializer(config, type.getContentType(), property);
        return MapSerializer.construct(intr.findPropertiesToIgnore(beanDesc.getClassInfo()), type, usesStaticTyping(config, beanDesc, vts), vts, property);
    }

    protected JsonSerializer<?> buildEnumMapSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        JavaType keyType = type.getKeyType();
        JavaType valueType = type.getContentType();
        EnumValues enums = null;
        if (keyType.isEnumType()) {
            enums = EnumValues.construct(keyType.getRawClass(), config.getAnnotationIntrospector());
        }
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return new EnumMapSerializer(valueType, usesStaticTyping(config, beanDesc, vts), enums, vts, property);
    }

    protected JsonSerializer<?> buildObjectArraySerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        JavaType valueType = type.getContentType();
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return new ObjectArraySerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
    }

    protected JsonSerializer<?> buildIndexedListSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        JavaType valueType = type.getContentType();
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return ContainerSerializers.indexedListSerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
    }

    protected JsonSerializer<?> buildCollectionSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        JavaType valueType = type.getContentType();
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return ContainerSerializers.collectionSerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
    }

    protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        JavaType valueType = type.containedType(0);
        if (valueType == null) {
            valueType = TypeFactory.type((Type) Object.class);
        }
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return ContainerSerializers.iteratorSerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
    }

    protected JsonSerializer<?> buildIterableSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        JavaType valueType = type.containedType(0);
        if (valueType == null) {
            valueType = TypeFactory.type((Type) Object.class);
        }
        TypeSerializer vts = createTypeSerializer(config, valueType, property);
        return ContainerSerializers.iterableSerializer(valueType, usesStaticTyping(config, beanDesc, vts), vts, property);
    }

    protected JsonSerializer<?> buildEnumSetSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        JavaType enumType = type.getContentType();
        if (!enumType.isEnumType()) {
            enumType = null;
        }
        return ContainerSerializers.enumSetSerializer(enumType, property);
    }

    protected boolean usesStaticTyping(SerializationConfig config, BasicBeanDescription beanDesc, TypeSerializer typeSer) {
        if (typeSer != null) {
            return false;
        }
        Typing t = config.getAnnotationIntrospector().findSerializationTyping(beanDesc.getClassInfo());
        if (t == null) {
            return config.isEnabled(Feature.USE_STATIC_TYPING);
        }
        if (t == Typing.STATIC) {
            return true;
        }
        return false;
    }
}
