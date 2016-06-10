package org.codehaus.jackson.map.ser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.SerializerFactory.Config;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BeanSerializerFactory extends BasicSerializerFactory {
    public static final BeanSerializerFactory instance;
    protected final Config _factoryConfig;

    public static class ConfigImpl extends Config {
        protected static final BeanSerializerModifier[] NO_MODIFIERS;
        protected static final Serializers[] NO_SERIALIZERS;
        protected final Serializers[] _additionalSerializers;
        protected final BeanSerializerModifier[] _modifiers;

        static {
            NO_SERIALIZERS = new Serializers[0];
            NO_MODIFIERS = new BeanSerializerModifier[0];
        }

        public ConfigImpl() {
            this(null, null);
        }

        protected ConfigImpl(Serializers[] allAdditionalSerializers, BeanSerializerModifier[] modifiers) {
            if (allAdditionalSerializers == null) {
                allAdditionalSerializers = NO_SERIALIZERS;
            }
            this._additionalSerializers = allAdditionalSerializers;
            if (modifiers == null) {
                modifiers = NO_MODIFIERS;
            }
            this._modifiers = modifiers;
        }

        public Config withAdditionalSerializers(Serializers additional) {
            if (additional != null) {
                return new ConfigImpl((Serializers[]) ArrayBuilders.insertInList(this._additionalSerializers, additional), this._modifiers);
            }
            throw new IllegalArgumentException("Can not pass null Serializers");
        }

        public Config withSerializerModifier(BeanSerializerModifier modifier) {
            if (modifier == null) {
                throw new IllegalArgumentException("Can not pass null modifier");
            }
            return new ConfigImpl(this._additionalSerializers, (BeanSerializerModifier[]) ArrayBuilders.insertInList(this._modifiers, modifier));
        }

        public boolean hasSerializers() {
            return this._additionalSerializers.length > 0;
        }

        public boolean hasSerializerModifiers() {
            return this._modifiers.length > 0;
        }

        public Iterable<Serializers> serializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalSerializers);
        }

        public Iterable<BeanSerializerModifier> serializerModifiers() {
            return ArrayBuilders.arrayAsIterable(this._modifiers);
        }
    }

    static {
        instance = new BeanSerializerFactory(null);
    }

    @Deprecated
    protected BeanSerializerFactory() {
        this(null);
    }

    protected BeanSerializerFactory(Config config) {
        if (config == null) {
            config = new ConfigImpl();
        }
        this._factoryConfig = config;
    }

    public Config getConfig() {
        return this._factoryConfig;
    }

    public SerializerFactory withConfig(Config config) {
        if (this._factoryConfig == config) {
            return this;
        }
        if (getClass() != BeanSerializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
        }
        this(config);
        return this;
    }

    public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property) {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        JsonSerializer<?> ser = findSerializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ser != null) {
            return ser;
        }
        ser = _findFirstSerializer(this._factoryConfig.serializers(), config, type, beanDesc, property);
        if (ser != null) {
            return ser;
        }
        ser = super.findSerializerByLookup(type, config, beanDesc, property);
        if (ser != null) {
            return ser;
        }
        ser = super.findSerializerByPrimaryType(type, config, beanDesc, property);
        if (ser != null) {
            return ser;
        }
        ser = findBeanSerializer(config, type, beanDesc, property);
        if (ser == null) {
            return super.findSerializerByAddonType(config, type, beanDesc, property);
        }
        return ser;
    }

    private static JsonSerializer<?> _findFirstSerializer(Iterable<Serializers> sers, SerializationConfig config, JavaType type, BeanDescription beanDesc, BeanProperty property) {
        for (Serializers ser : sers) {
            JsonSerializer<?> js = ser.findSerializer(config, type, beanDesc, property);
            if (js != null) {
                return js;
            }
        }
        return null;
    }

    public JsonSerializer<Object> findBeanSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) {
        if (!isPotentialBeanType(type.getRawClass())) {
            return null;
        }
        JsonSerializer<Object> serializer = constructBeanSerializer(config, beanDesc, property);
        if (!this._factoryConfig.hasSerializerModifiers()) {
            return serializer;
        }
        for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
            serializer = mod.modifySerializer(config, beanDesc, serializer);
        }
        return serializer;
    }

    public TypeSerializer findPropertyTypeSerializer(JavaType baseType, SerializationConfig config, AnnotatedMember accessor, BeanProperty property) {
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(accessor, baseType);
        if (b == null) {
            return createTypeSerializer(config, baseType, property);
        }
        return b.buildTypeSerializer(baseType, config.getSubtypeResolver().collectAndResolveSubtypes(accessor, (MapperConfig) config, ai), property);
    }

    public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, SerializationConfig config, AnnotatedMember accessor, BeanProperty property) {
        JavaType contentType = containerType.getContentType();
        AnnotationIntrospector ai = config.getAnnotationIntrospector();
        TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(accessor, containerType);
        if (b == null) {
            return createTypeSerializer(config, contentType, property);
        }
        return b.buildTypeSerializer(contentType, config.getSubtypeResolver().collectAndResolveSubtypes(accessor, (MapperConfig) config, ai), property);
    }

    protected JsonSerializer<Object> constructBeanSerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) {
        if (beanDesc.getBeanClass() == Object.class) {
            throw new IllegalArgumentException("Can not create bean serializer for Object.class");
        }
        BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
        List<BeanPropertyWriter> props = findBeanProperties(config, beanDesc);
        AnnotatedMethod anyGetter = beanDesc.findAnyGetter();
        if (this._factoryConfig.hasSerializerModifiers()) {
            if (props == null) {
                props = new ArrayList();
            }
            for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
                props = mod.changeProperties(config, beanDesc, props);
            }
        }
        if (props != null && props.size() != 0) {
            props = sortBeanProperties(config, beanDesc, filterBeanProperties(config, beanDesc, props));
        } else if (anyGetter != null) {
            props = Collections.emptyList();
        } else if (beanDesc.hasKnownClassAnnotations()) {
            return builder.createDummy();
        } else {
            return null;
        }
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod2 : this._factoryConfig.serializerModifiers()) {
                props = mod2.orderProperties(config, beanDesc, props);
            }
        }
        builder.setProperties(props);
        builder.setFilterId(findFilterId(config, beanDesc));
        if (anyGetter != null) {
            JavaType type = anyGetter.getType(beanDesc.bindingsForBeanType());
            builder.setAnyGetter(new AnyGetterWriter(anyGetter, MapSerializer.construct(null, type, config.isEnabled(Feature.USE_STATIC_TYPING), createTypeSerializer(config, type.getContentType(), property), property)));
        }
        processViews(config, builder);
        if (this._factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod22 : this._factoryConfig.serializerModifiers()) {
                builder = mod22.updateBuilder(config, beanDesc, builder);
            }
        }
        return builder.build();
    }

    protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews) {
        return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
    }

    protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BasicBeanDescription beanDesc) {
        return new PropertyBuilder(config, beanDesc);
    }

    protected BeanSerializerBuilder constructBeanSerializerBuilder(BasicBeanDescription beanDesc) {
        return new BeanSerializerBuilder(beanDesc);
    }

    protected Object findFilterId(SerializationConfig config, BasicBeanDescription beanDesc) {
        return config.getAnnotationIntrospector().findFilterId(beanDesc.getClassInfo());
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        return ClassUtil.canBeABeanType(type) == null && !ClassUtil.isProxyType(type);
    }

    protected List<BeanPropertyWriter> findBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc) {
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(Feature.AUTO_DETECT_GETTERS)) {
            vchecker = vchecker.withGetterVisibility(Visibility.NONE);
        }
        if (!config.isEnabled(Feature.AUTO_DETECT_IS_GETTERS)) {
            vchecker = vchecker.withIsGetterVisibility(Visibility.NONE);
        }
        if (!config.isEnabled(Feature.AUTO_DETECT_FIELDS)) {
            vchecker = vchecker.withFieldVisibility(Visibility.NONE);
        }
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        vchecker = intr.findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        LinkedHashMap<String, AnnotatedMethod> methodsByProp = beanDesc.findGetters(vchecker, null);
        LinkedHashMap<String, AnnotatedField> fieldsByProp = beanDesc.findSerializableFields(vchecker, methodsByProp.keySet());
        removeIgnorableTypes(config, beanDesc, methodsByProp);
        removeIgnorableTypes(config, beanDesc, fieldsByProp);
        if (methodsByProp.isEmpty() && fieldsByProp.isEmpty()) {
            return null;
        }
        boolean staticTyping = usesStaticTyping(config, beanDesc, null);
        PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
        List<BeanPropertyWriter> arrayList = new ArrayList(methodsByProp.size());
        TypeBindings typeBind = beanDesc.bindingsForBeanType();
        for (Entry<String, AnnotatedField> en : fieldsByProp.entrySet()) {
            ReferenceProperty prop = intr.findReferenceType((AnnotatedMember) en.getValue());
            if (prop == null || !prop.isBackReference()) {
                arrayList = arrayList;
                arrayList.add(_constructWriter(config, typeBind, pb, staticTyping, (String) en.getKey(), (AnnotatedMember) en.getValue()));
            }
        }
        for (Entry<String, AnnotatedMethod> en2 : methodsByProp.entrySet()) {
            prop = intr.findReferenceType((AnnotatedMember) en2.getValue());
            if (prop == null || !prop.isBackReference()) {
                arrayList = arrayList;
                arrayList.add(_constructWriter(config, typeBind, pb, staticTyping, (String) en2.getKey(), (AnnotatedMember) en2.getValue()));
            }
        }
        return arrayList;
    }

    protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
        String[] ignored = config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo());
        if (ignored != null && ignored.length > 0) {
            HashSet<String> ignoredSet = ArrayBuilders.arrayToSet(ignored);
            Iterator<BeanPropertyWriter> it = props.iterator();
            while (it.hasNext()) {
                if (ignoredSet.contains(((BeanPropertyWriter) it.next()).getName())) {
                    it.remove();
                }
            }
        }
        return props;
    }

    protected List<BeanPropertyWriter> sortBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
        List<String> creatorProps = beanDesc.findCreatorPropertyNames();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        AnnotatedClass ac = beanDesc.getClassInfo();
        String[] propOrder = intr.findSerializationPropertyOrder(ac);
        Boolean alpha = intr.findSerializationSortAlphabetically(ac);
        boolean sort = alpha != null && alpha.booleanValue();
        if (!sort && creatorProps.isEmpty() && propOrder == null) {
            return props;
        }
        return _sortBeanProperties(props, creatorProps, propOrder, sort);
    }

    protected void processViews(SerializationConfig config, BeanSerializerBuilder builder) {
        List<BeanPropertyWriter> props = builder.getProperties();
        BeanPropertyWriter bpw;
        Class<?>[] views;
        if (config.isEnabled(Feature.DEFAULT_VIEW_INCLUSION)) {
            int i;
            int propCount = props.size();
            BeanPropertyWriter[] filtered = null;
            for (i = 0; i < propCount; i++) {
                bpw = (BeanPropertyWriter) props.get(i);
                views = bpw.getViews();
                if (views != null) {
                    if (filtered == null) {
                        filtered = new BeanPropertyWriter[props.size()];
                    }
                    filtered[i] = constructFilteredBeanWriter(bpw, views);
                }
            }
            if (filtered != null) {
                for (i = 0; i < propCount; i++) {
                    if (filtered[i] == null) {
                        filtered[i] = (BeanPropertyWriter) props.get(i);
                    }
                }
                builder.setFilteredProperties(filtered);
                return;
            }
            return;
        }
        ArrayList<BeanPropertyWriter> explicit = new ArrayList(props.size());
        for (BeanPropertyWriter bpw2 : props) {
            views = bpw2.getViews();
            if (views != null) {
                explicit.add(constructFilteredBeanWriter(bpw2, views));
            }
        }
        builder.setFilteredProperties((BeanPropertyWriter[]) explicit.toArray(new BeanPropertyWriter[explicit.size()]));
    }

    protected <T extends AnnotatedMember> void removeIgnorableTypes(SerializationConfig config, BasicBeanDescription beanDesc, Map<String, T> props) {
        if (!props.isEmpty()) {
            AnnotationIntrospector intr = config.getAnnotationIntrospector();
            Iterator<Entry<String, T>> it = props.entrySet().iterator();
            HashMap<Class<?>, Boolean> ignores = new HashMap();
            while (it.hasNext()) {
                Class<?> type = ((AnnotatedMember) ((Entry) it.next()).getValue()).getRawType();
                Boolean result = (Boolean) ignores.get(type);
                if (result == null) {
                    result = intr.isIgnorableType(((BasicBeanDescription) config.introspectClassAnnotations(type)).getClassInfo());
                    if (result == null) {
                        result = Boolean.FALSE;
                    }
                    ignores.put(type, result);
                }
                if (result.booleanValue()) {
                    it.remove();
                }
            }
        }
    }

    protected BeanPropertyWriter _constructWriter(SerializationConfig config, TypeBindings typeContext, PropertyBuilder pb, boolean staticTyping, String name, AnnotatedMember accessor) {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            accessor.fixAccess();
        }
        JavaType type = accessor.getType(typeContext);
        Std property = new Std(name, type, pb.getClassAnnotations(), accessor);
        JsonSerializer<Object> annotatedSerializer = findSerializerFromAnnotation(config, accessor, property);
        TypeSerializer contentTypeSer = null;
        if (ClassUtil.isCollectionMapOrArray(type.getRawClass())) {
            contentTypeSer = findPropertyContentTypeSerializer(type, config, accessor, property);
        }
        BeanPropertyWriter pbw = pb.buildWriter(name, type, annotatedSerializer, findPropertyTypeSerializer(type, config, accessor, property), contentTypeSer, accessor, staticTyping);
        pbw.setViews(config.getAnnotationIntrospector().findSerializationViews(accessor));
        return pbw;
    }

    protected List<BeanPropertyWriter> _sortBeanProperties(List<BeanPropertyWriter> props, List<String> creatorProps, String[] propertyOrder, boolean sort) {
        Map<String, BeanPropertyWriter> all;
        BeanPropertyWriter w;
        int size = props.size();
        if (sort) {
            all = new TreeMap();
        } else {
            all = new LinkedHashMap(size * 2);
        }
        for (BeanPropertyWriter w2 : props) {
            all.put(w2.getName(), w2);
        }
        Map<String, BeanPropertyWriter> ordered = new LinkedHashMap(size * 2);
        if (propertyOrder != null) {
            for (String name : propertyOrder) {
                w2 = (BeanPropertyWriter) all.get(name);
                if (w2 != null) {
                    ordered.put(name, w2);
                }
            }
        }
        for (String name2 : creatorProps) {
            w2 = (BeanPropertyWriter) all.get(name2);
            if (w2 != null) {
                ordered.put(name2, w2);
            }
        }
        ordered.putAll(all);
        return new ArrayList(ordered.values());
    }
}
