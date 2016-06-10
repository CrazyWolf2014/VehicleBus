package org.codehaus.jackson.map.deser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerFactory.Config;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.KeyDeserializer.None;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.SettableBeanProperty.CreatorProperty;
import org.codehaus.jackson.map.deser.StdDeserializer.AtomicReferenceDeserializer;
import org.codehaus.jackson.map.deser.impl.StringCollectionDeserializer;
import org.codehaus.jackson.map.ext.OptionalHandlerFactory;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public abstract class BasicDeserializerFactory extends DeserializerFactory {
    static final JavaType TYPE_STRING;
    protected static final HashMap<JavaType, JsonDeserializer<Object>> _arrayDeserializers;
    static final HashMap<String, Class<? extends Collection>> _collectionFallbacks;
    static final HashMap<String, Class<? extends Map>> _mapFallbacks;
    static final HashMap<JavaType, JsonDeserializer<Object>> _simpleDeserializers;
    protected OptionalHandlerFactory optionalHandlers;

    protected abstract JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType arrayType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType collectionType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> cls, DeserializationConfig deserializationConfig, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomMapDeserializer(MapType mapType, DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, BasicBeanDescription basicBeanDescription, BeanProperty beanProperty, KeyDeserializer keyDeserializer, TypeDeserializer typeDeserializer, JsonDeserializer<?> jsonDeserializer) throws JsonMappingException;

    protected abstract JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> cls, DeserializationConfig deserializationConfig, BeanProperty beanProperty) throws JsonMappingException;

    public abstract DeserializerFactory withConfig(Config config);

    static {
        TYPE_STRING = TypeFactory.type((Type) String.class);
        _simpleDeserializers = StdDeserializers.constructAll();
        _mapFallbacks = new HashMap();
        _mapFallbacks.put(Map.class.getName(), LinkedHashMap.class);
        _mapFallbacks.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class);
        _mapFallbacks.put(SortedMap.class.getName(), TreeMap.class);
        _mapFallbacks.put("java.util.NavigableMap", TreeMap.class);
        try {
            Class<?> key = Class.forName("java.util.ConcurrentNavigableMap");
            _mapFallbacks.put(key.getName(), Class.forName("java.util.ConcurrentSkipListMap"));
        } catch (ClassNotFoundException e) {
        }
        _collectionFallbacks = new HashMap();
        _collectionFallbacks.put(Collection.class.getName(), ArrayList.class);
        _collectionFallbacks.put(List.class.getName(), ArrayList.class);
        _collectionFallbacks.put(Set.class.getName(), HashSet.class);
        _collectionFallbacks.put(SortedSet.class.getName(), TreeSet.class);
        _collectionFallbacks.put(Queue.class.getName(), LinkedList.class);
        _collectionFallbacks.put("java.util.Deque", LinkedList.class);
        _collectionFallbacks.put("java.util.NavigableSet", TreeSet.class);
        _arrayDeserializers = ArrayDeserializers.getAll();
    }

    protected BasicDeserializerFactory() {
        this.optionalHandlers = OptionalHandlerFactory.instance;
    }

    public JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, DeserializerProvider p, ArrayType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<?> custom;
        JavaType elemType = type.getContentType();
        JsonDeserializer<Object> contentDeser = (JsonDeserializer) elemType.getValueHandler();
        if (contentDeser == null) {
            JsonDeserializer<?> deser = (JsonDeserializer) _arrayDeserializers.get(elemType);
            if (deser != null) {
                custom = _findCustomArrayDeserializer(type, config, p, property, null, null);
                return custom != null ? custom : deser;
            } else if (elemType.isPrimitive()) {
                throw new IllegalArgumentException("Internal error: primitive type (" + type + ") passed, no array deserializer found");
            }
        }
        TypeDeserializer elemTypeDeser = (TypeDeserializer) elemType.getTypeHandler();
        if (elemTypeDeser == null) {
            elemTypeDeser = findTypeDeserializer(config, elemType, property);
        }
        custom = _findCustomArrayDeserializer(type, config, p, property, elemTypeDeser, contentDeser);
        if (custom != null) {
            return custom;
        }
        if (contentDeser == null) {
            contentDeser = p.findValueDeserializer(config, elemType, property);
        }
        return new ArrayDeserializer(type, contentDeser, elemTypeDeser);
    }

    public JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig config, DeserializerProvider p, CollectionType type, BeanProperty property) throws JsonMappingException {
        Class<?> collectionClass = type.getRawClass();
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectClassAnnotations(collectionClass);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (deser != null) {
            return deser;
        }
        JavaType type2 = (CollectionType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
        JavaType contentType = type2.getContentType();
        JsonDeserializer<Object> contentDeser = (JsonDeserializer) contentType.getValueHandler();
        TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
        if (contentTypeDeser == null) {
            contentTypeDeser = findTypeDeserializer(config, contentType, property);
        }
        JsonDeserializer<?> custom = _findCustomCollectionDeserializer(type2, config, p, beanDesc, property, contentTypeDeser, contentDeser);
        if (custom != null) {
            return custom;
        }
        if (contentDeser == null) {
            if (EnumSet.class.isAssignableFrom(collectionClass)) {
                return new EnumSetDeserializer(constructEnumResolver(contentType.getRawClass(), config));
            }
            contentDeser = p.findValueDeserializer(config, contentType, property);
        }
        if (type2.isInterface() || type2.isAbstract()) {
            Class<? extends Collection> fallback = (Class) _collectionFallbacks.get(collectionClass.getName());
            if (fallback == null) {
                throw new IllegalArgumentException("Can not find a deserializer for non-concrete Collection type " + type2);
            }
            collectionClass = fallback;
        }
        Constructor<Collection<Object>> ctor = ClassUtil.findConstructor(collectionClass, config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        if (contentType.getRawClass() == String.class) {
            return new StringCollectionDeserializer(type2, contentDeser, ctor);
        }
        return new CollectionDeserializer(type2, contentDeser, contentTypeDeser, ctor);
    }

    public JsonDeserializer<?> createMapDeserializer(DeserializationConfig config, DeserializerProvider p, MapType type, BeanProperty property) throws JsonMappingException {
        Class<?> mapClass = type.getRawClass();
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (deser != null) {
            return deser;
        }
        JavaType type2 = (MapType) modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
        JavaType keyType = type2.getKeyType();
        JavaType contentType = type2.getContentType();
        JsonDeserializer<Object> contentDeser = (JsonDeserializer) contentType.getValueHandler();
        KeyDeserializer keyDes = (KeyDeserializer) keyType.getValueHandler();
        if (keyDes == null) {
            if (TYPE_STRING.equals(keyType)) {
                keyDes = null;
            } else {
                keyDes = p.findKeyDeserializer(config, keyType, property);
            }
        }
        TypeDeserializer contentTypeDeser = (TypeDeserializer) contentType.getTypeHandler();
        if (contentTypeDeser == null) {
            contentTypeDeser = findTypeDeserializer(config, contentType, property);
        }
        JsonDeserializer<?> custom = _findCustomMapDeserializer(type2, config, p, beanDesc, property, keyDes, contentTypeDeser, contentDeser);
        if (custom != null) {
            return custom;
        }
        if (contentDeser == null) {
            contentDeser = p.findValueDeserializer(config, contentType, property);
        }
        if (EnumMap.class.isAssignableFrom(mapClass)) {
            Class<?> kt = keyType.getRawClass();
            if (kt != null && kt.isEnum()) {
                return new EnumMapDeserializer(constructEnumResolver(kt, config), contentDeser);
            }
            throw new IllegalArgumentException("Can not construct EnumMap; generic (key) type not available");
        }
        if (type2.isInterface() || type2.isAbstract()) {
            Class<? extends Map> fallback = (Class) _mapFallbacks.get(mapClass.getName());
            if (fallback == null) {
                throw new IllegalArgumentException("Can not find a deserializer for non-concrete Map type " + type2);
            }
            type2 = (MapType) type2.forcedNarrowBy(fallback);
            beanDesc = (BasicBeanDescription) config.introspectForCreation(type2);
        }
        boolean fixAccess = config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        Constructor<Map<Object, Object>> defaultCtor = beanDesc.findDefaultConstructor();
        if (defaultCtor != null && fixAccess) {
            ClassUtil.checkAndFixAccess(defaultCtor);
        }
        JsonDeserializer md = new MapDeserializer(type2, defaultCtor, keyDes, contentDeser, contentTypeDeser);
        md.setIgnorableProperties(config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo()));
        md.setCreators(findMapCreators(config, beanDesc));
        return md;
    }

    public JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspectForCreation(type);
        JsonDeserializer<?> des = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (des != null) {
            return des;
        }
        Class<?> enumClass = type.getRawClass();
        JsonDeserializer<?> custom = _findCustomEnumDeserializer(enumClass, config, beanDesc, property);
        if (custom != null) {
            return custom;
        }
        for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
            if (config.getAnnotationIntrospector().hasCreatorAnnotation(factory)) {
                if (factory.getParameterCount() == 1 && factory.getRawType().isAssignableFrom(enumClass)) {
                    return EnumDeserializer.deserializerForCreator(config, enumClass, factory);
                }
                throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass.getName() + ")");
            }
        }
        return new EnumDeserializer(constructEnumResolver(enumClass, config));
    }

    public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType nodeType, BeanProperty property) throws JsonMappingException {
        Class<? extends JsonNode> nodeClass = nodeType.getRawClass();
        JsonDeserializer<?> custom = _findCustomTreeNodeDeserializer(nodeClass, config, property);
        return custom != null ? custom : JsonNodeDeserializer.getDeserializer(nodeClass);
    }

    public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        JsonDeserializer<Object> deser = (JsonDeserializer) _simpleDeserializers.get(type);
        if (deser != null) {
            return deser;
        }
        if (AtomicReference.class.isAssignableFrom(type.getRawClass())) {
            return new AtomicReferenceDeserializer(type, property);
        }
        JsonDeserializer<?> d = this.optionalHandlers.findDeserializer(type, config, p);
        if (d != null) {
            return d;
        }
        return null;
    }

    public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType, BeanProperty property) {
        AnnotatedClass ac = ((BasicBeanDescription) config.introspectClassAnnotations(baseType.getRawClass())).getClassInfo();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findTypeResolver(ac, baseType);
        Collection<NamedType> subtypes = null;
        if (b == null) {
            b = config.getDefaultTyper(baseType);
            if (b == null) {
                return null;
            }
        }
        subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, (MapperConfig) config, ai);
        return b.buildTypeDeserializer(baseType, subtypes, property);
    }

    public TypeDeserializer findPropertyTypeDeserializer(DeserializationConfig config, JavaType baseType, AnnotatedMember annotated, BeanProperty property) {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(annotated, baseType);
        if (b == null) {
            return findTypeDeserializer(config, baseType, property);
        }
        return b.buildTypeDeserializer(baseType, config.getSubtypeResolver().collectAndResolveSubtypes(annotated, (MapperConfig) config, ai), property);
    }

    public TypeDeserializer findPropertyContentTypeDeserializer(DeserializationConfig config, JavaType containerType, AnnotatedMember propertyEntity, BeanProperty property) {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(propertyEntity, containerType);
        JavaType contentType = containerType.getContentType();
        if (b == null) {
            return findTypeDeserializer(config, contentType, property);
        }
        return b.buildTypeDeserializer(contentType, config.getSubtypeResolver().collectAndResolveSubtypes(propertyEntity, (MapperConfig) config, ai), property);
    }

    protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationConfig config, Annotated a, BeanProperty property) {
        Object deserDef = config.getAnnotationIntrospector().findDeserializer(a, property);
        if (deserDef != null) {
            return _constructDeserializer(config, deserDef);
        }
        return null;
    }

    JsonDeserializer<Object> _constructDeserializer(DeserializationConfig config, Object deserDef) {
        if (deserDef instanceof JsonDeserializer) {
            return (JsonDeserializer) deserDef;
        }
        if (deserDef instanceof Class) {
            Class<?> cls = (Class) deserDef;
            if (JsonDeserializer.class.isAssignableFrom(cls)) {
                return (JsonDeserializer) ClassUtil.createInstance(cls, config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
            }
            throw new IllegalStateException("AnnotationIntrospector returned Class " + cls.getName() + "; expected Class<JsonDeserializer>");
        }
        throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type " + deserDef.getClass().getName() + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
    }

    protected <T extends JavaType> T modifyTypeByAnnotation(DeserializationConfig config, Annotated a, T type, String propName) throws JsonMappingException {
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Class<?> subclass = intr.findDeserializationType(a, type, propName);
        if (subclass != null) {
            try {
                type = type.narrowBy(subclass);
            } catch (IllegalArgumentException iae) {
                throw new JsonMappingException("Failed to narrow type " + type + " with concrete-type annotation (value " + subclass.getName() + "), method '" + a.getName() + "': " + iae.getMessage(), null, iae);
            }
        }
        if (type.isContainerType()) {
            Class<?> keyClass = intr.findDeserializationKeyType(a, type.getKeyType(), propName);
            if (keyClass != null) {
                if (type instanceof MapType) {
                    try {
                        type = ((MapType) type).narrowKey(keyClass);
                    } catch (IllegalArgumentException iae2) {
                        throw new JsonMappingException("Failed to narrow key type " + type + " with key-type annotation (" + keyClass.getName() + "): " + iae2.getMessage(), null, iae2);
                    }
                }
                throw new JsonMappingException("Illegal key-type annotation: type " + type + " is not a Map type");
            }
            Class<?> cc = intr.findDeserializationContentType(a, type.getContentType(), propName);
            if (cc != null) {
                try {
                    type = type.narrowContentsBy(cc);
                } catch (IllegalArgumentException iae22) {
                    throw new JsonMappingException("Failed to narrow content type " + type + " with content-type annotation (" + cc.getName() + "): " + iae22.getMessage(), null, iae22);
                }
            }
        }
        return type;
    }

    protected JavaType resolveType(DeserializationConfig config, BasicBeanDescription beanDesc, JavaType type, AnnotatedMember member, BeanProperty property) {
        TypeDeserializer valueTypeDeser;
        if (type.isContainerType()) {
            AnnotationIntrospector intr = config.getAnnotationIntrospector();
            boolean canForceAccess = config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
            JavaType keyType = type.getKeyType();
            if (keyType != null) {
                Class<? extends KeyDeserializer> kdClass = intr.findKeyDeserializer(member);
                if (!(kdClass == null || kdClass == None.class)) {
                    keyType.setValueHandler((KeyDeserializer) ClassUtil.createInstance(kdClass, canForceAccess));
                }
            }
            Class<? extends JsonDeserializer<?>> cdClass = intr.findContentDeserializer(member);
            if (!(cdClass == null || cdClass == JsonDeserializer.None.class)) {
                type.getContentType().setValueHandler((JsonDeserializer) ClassUtil.createInstance(cdClass, canForceAccess));
            }
            if (member instanceof AnnotatedMember) {
                TypeDeserializer contentTypeDeser = findPropertyContentTypeDeserializer(config, type, member, property);
                if (contentTypeDeser != null) {
                    type = type.withContentTypeHandler(contentTypeDeser);
                }
            }
        }
        if (member instanceof AnnotatedMember) {
            valueTypeDeser = findPropertyTypeDeserializer(config, type, member, property);
        } else {
            valueTypeDeser = findTypeDeserializer(config, type, null);
        }
        if (valueTypeDeser != null) {
            return type.withTypeHandler(valueTypeDeser);
        }
        return type;
    }

    protected EnumResolver<?> constructEnumResolver(Class<?> enumClass, DeserializationConfig config) {
        if (config.isEnabled(Feature.READ_ENUMS_USING_TO_STRING)) {
            return EnumResolver.constructUnsafeUsingToString(enumClass);
        }
        return EnumResolver.constructUnsafe(enumClass, config.getAnnotationIntrospector());
    }

    protected CreatorContainer findMapCreators(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        int i;
        AnnotatedParameter param;
        Class<?> mapClass = beanDesc.getBeanClass();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        CreatorContainer creators = new CreatorContainer(mapClass, config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
        for (AnnotatedConstructor ctor : beanDesc.getConstructors()) {
            SettableBeanProperty[] properties;
            int nameCount;
            String name;
            int argCount = ctor.getParameterCount();
            if (argCount >= 1 && intr.hasCreatorAnnotation(ctor)) {
                properties = new SettableBeanProperty[argCount];
                nameCount = 0;
                for (i = 0; i < argCount; i++) {
                    param = ctor.getParameter(i);
                    name = param == null ? null : intr.findPropertyNameForParam(param);
                    if (name == null || name.length() == 0) {
                        throw new IllegalArgumentException("Parameter #" + i + " of constructor " + ctor + " has no property name annotation: must have for @JsonCreator for a Map type");
                    }
                    nameCount++;
                    properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
                }
                creators.addPropertyConstructor(ctor, properties);
            }
        }
        for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
            argCount = factory.getParameterCount();
            if (argCount >= 1 && intr.hasCreatorAnnotation(factory)) {
                properties = new SettableBeanProperty[argCount];
                nameCount = 0;
                for (i = 0; i < argCount; i++) {
                    param = factory.getParameter(i);
                    name = param == null ? null : intr.findPropertyNameForParam(param);
                    if (name == null || name.length() == 0) {
                        throw new IllegalArgumentException("Parameter #" + i + " of factory method " + factory + " has no property name annotation: must have for @JsonCreator for a Map type");
                    }
                    nameCount++;
                    properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
                }
                creators.addPropertyFactory(factory, properties);
            }
        }
        return creators;
    }

    protected SettableBeanProperty constructCreatorProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, int index, AnnotatedParameter param) throws JsonMappingException {
        JavaType t0 = TypeFactory.type(param.getParameterType(), beanDesc.bindingsForBeanType());
        Std property = new Std(name, t0, beanDesc.getClassAnnotations(), param);
        JavaType type = resolveType(config, beanDesc, t0, param, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, param, property);
        type = modifyTypeByAnnotation(config, param, type, name);
        SettableBeanProperty prop = new CreatorProperty(name, type, findTypeDeserializer(config, type, property), beanDesc.getClassAnnotations(), param, index);
        if (deser != null) {
            prop.setValueDeserializer(deser);
        }
        return prop;
    }
}
