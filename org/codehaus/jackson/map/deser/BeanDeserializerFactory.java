package org.codehaus.jackson.map.deser;

import com.launch.service.BundleBuilder;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.AbstractTypeResolver;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializerFactory;
import org.codehaus.jackson.map.DeserializerFactory.Config;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.Deserializers;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.deser.SettableBeanProperty.FieldProperty;
import org.codehaus.jackson.map.deser.SettableBeanProperty.MethodProperty;
import org.codehaus.jackson.map.deser.SettableBeanProperty.SetterlessProperty;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ArrayBuilders;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BeanDeserializerFactory extends BasicDeserializerFactory {
    private static final Class<?>[] INIT_CAUSE_PARAMS;
    public static final BeanDeserializerFactory instance;
    protected final Config _factoryConfig;

    public static class ConfigImpl extends Config {
        protected static final BeanDeserializerModifier[] NO_MODIFIERS;
        protected final Deserializers[] _additionalDeserializers;
        protected final BeanDeserializerModifier[] _modifiers;

        static {
            NO_MODIFIERS = new BeanDeserializerModifier[0];
        }

        public ConfigImpl() {
            this(null, null);
        }

        protected ConfigImpl(Deserializers[] allAdditionalDeserializers, BeanDeserializerModifier[] modifiers) {
            if (allAdditionalDeserializers == null) {
                allAdditionalDeserializers = BeanDeserializerFactory.NO_DESERIALIZERS;
            }
            this._additionalDeserializers = allAdditionalDeserializers;
            if (modifiers == null) {
                modifiers = NO_MODIFIERS;
            }
            this._modifiers = modifiers;
        }

        public Config withAdditionalDeserializers(Deserializers additional) {
            if (additional != null) {
                return new ConfigImpl((Deserializers[]) ArrayBuilders.insertInList(this._additionalDeserializers, additional), this._modifiers);
            }
            throw new IllegalArgumentException("Can not pass null Deserializers");
        }

        public Config withDeserializerModifier(BeanDeserializerModifier modifier) {
            if (modifier == null) {
                throw new IllegalArgumentException("Can not pass null modifier");
            }
            return new ConfigImpl(this._additionalDeserializers, (BeanDeserializerModifier[]) ArrayBuilders.insertInList(this._modifiers, modifier));
        }

        public boolean hasDeserializers() {
            return this._additionalDeserializers.length > 0;
        }

        public boolean hasDeserializerModifiers() {
            return this._modifiers.length > 0;
        }

        public Iterable<Deserializers> deserializers() {
            return ArrayBuilders.arrayAsIterable(this._additionalDeserializers);
        }

        public Iterable<BeanDeserializerModifier> deserializerModifiers() {
            return ArrayBuilders.arrayAsIterable(this._modifiers);
        }
    }

    static {
        INIT_CAUSE_PARAMS = new Class[]{Throwable.class};
        instance = new BeanDeserializerFactory(null);
    }

    @Deprecated
    public BeanDeserializerFactory() {
        this(null);
    }

    public BeanDeserializerFactory(Config config) {
        if (config == null) {
            config = new ConfigImpl();
        }
        this._factoryConfig = config;
    }

    public final Config getConfig() {
        return this._factoryConfig;
    }

    public DeserializerFactory withConfig(Config config) {
        if (this._factoryConfig == config) {
            return this;
        }
        if (getClass() != BeanDeserializerFactory.class) {
            throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
        }
        this(config);
        return this;
    }

    protected JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findArrayDeserializer(type, config, provider, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findCollectionDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> type, DeserializationConfig config, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findEnumDeserializer(type, config, beanDesc, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findMapDeserializer(type, config, provider, beanDesc, property, keyDeserializer, elementTypeDeserializer, elementDeserializer);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> type, DeserializationConfig config, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<?> deser = d.findTreeNodeDeserializer(type, config, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    protected JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        for (Deserializers d : this._factoryConfig.deserializers()) {
            JsonDeserializer<Object> deser = d.findBeanDeserializer(type, config, provider, beanDesc, property);
            if (deser != null) {
                return deser;
            }
        }
        return null;
    }

    public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property) throws JsonMappingException {
        BasicBeanDescription beanDesc = (BasicBeanDescription) config.introspect(type);
        JsonDeserializer<Object> ad = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
        if (ad != null) {
            return ad;
        }
        JavaType newType = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
        if (newType.getRawClass() != type.getRawClass()) {
            type = newType;
            beanDesc = (BasicBeanDescription) config.introspect(type);
        }
        JsonDeserializer<Object> custom = _findCustomBeanDeserializer(type, config, p, beanDesc, property);
        if (custom != null) {
            return custom;
        }
        JsonDeserializer<Object> deser = super.createBeanDeserializer(config, p, type, property);
        if (deser != null) {
            return deser;
        }
        if (!isPotentialBeanType(type.getRawClass())) {
            return null;
        }
        if (type.isThrowable()) {
            return buildThrowableDeserializer(config, type, beanDesc, property);
        }
        if (!type.isAbstract()) {
            return buildBeanDeserializer(config, type, beanDesc, property);
        }
        AbstractTypeResolver res = config.getAbstractTypeResolver();
        if (res != null && config.getAnnotationIntrospector().findTypeResolver(beanDesc.getClassInfo(), type) == null) {
            JavaType concrete = res.resolveAbstractType(config, type);
            if (concrete != null) {
                return buildBeanDeserializer(config, concrete, (BasicBeanDescription) config.introspect(concrete), property);
            }
        }
        return new AbstractDeserializer(type);
    }

    public JsonDeserializer<Object> buildBeanDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
        builder.setCreators(findDeserializerCreators(config, beanDesc));
        addBeanProps(config, beanDesc, builder);
        addReferenceProperties(config, beanDesc, builder);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                builder = mod.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<?> deserializer = builder.build(property);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
                deserializer = mod2.modifyDeserializer(config, beanDesc, deserializer);
            }
        }
        return deserializer;
    }

    public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property) throws JsonMappingException {
        BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
        builder.setCreators(findDeserializerCreators(config, beanDesc));
        addBeanProps(config, beanDesc, builder);
        AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
        if (am != null) {
            SettableBeanProperty prop = constructSettableProperty(config, beanDesc, "cause", am);
            if (prop != null) {
                builder.addProperty(prop);
            }
        }
        builder.addIgnorable("localizedMessage");
        builder.addIgnorable(BundleBuilder.AskFromMessage);
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
                builder = mod.updateBuilder(config, beanDesc, builder);
            }
        }
        JsonDeserializer<?> build = builder.build(property);
        if (build instanceof BeanDeserializer) {
            build = new ThrowableDeserializer((BeanDeserializer) build);
        }
        if (this._factoryConfig.hasDeserializerModifiers()) {
            for (BeanDeserializerModifier mod2 : this._factoryConfig.deserializerModifiers()) {
                build = mod2.modifyDeserializer(config, beanDesc, build);
            }
        }
        return build;
    }

    protected BeanDeserializerBuilder constructBeanDeserializerBuilder(BasicBeanDescription beanDesc) {
        return new BeanDeserializerBuilder(beanDesc);
    }

    protected CreatorContainer findDeserializerCreators(DeserializationConfig config, BasicBeanDescription beanDesc) throws JsonMappingException {
        boolean fixAccess = config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
        CreatorContainer creators = new CreatorContainer(beanDesc.getBeanClass(), fixAccess);
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        if (beanDesc.getType().isConcrete()) {
            Constructor<?> defaultCtor = beanDesc.findDefaultConstructor();
            if (defaultCtor != null) {
                if (fixAccess) {
                    ClassUtil.checkAndFixAccess(defaultCtor);
                }
                creators.setDefaultConstructor(defaultCtor);
            }
        }
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(Feature.AUTO_DETECT_CREATORS)) {
            vchecker = vchecker.withCreatorVisibility(Visibility.NONE);
        }
        vchecker = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        _addDeserializerConstructors(config, beanDesc, vchecker, intr, creators);
        _addDeserializerFactoryMethods(config, beanDesc, vchecker, intr, creators);
        return creators;
    }

    protected void _addDeserializerConstructors(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorContainer creators) throws JsonMappingException {
        for (AnnotatedMember ctor : beanDesc.getConstructors()) {
            int argCount = ctor.getParameterCount();
            if (argCount >= 1) {
                boolean isCreator = intr.hasCreatorAnnotation(ctor);
                boolean isVisible = vchecker.isCreatorVisible(ctor);
                String name;
                AnnotatedParameter param;
                if (argCount == 1) {
                    name = intr.findPropertyNameForParam(ctor.getParameter(0));
                    if (name == null || name.length() == 0) {
                        Class<?> type = ctor.getParameterClass(0);
                        if (type == String.class) {
                            if (isCreator || isVisible) {
                                creators.addStringConstructor(ctor);
                            }
                        } else if (type == Integer.TYPE || type == Integer.class) {
                            if (isCreator || isVisible) {
                                creators.addIntConstructor(ctor);
                            }
                        } else if (type == Long.TYPE || type == Long.class) {
                            if (isCreator || isVisible) {
                                creators.addLongConstructor(ctor);
                            }
                        } else if (isCreator) {
                            creators.addDelegatingConstructor(ctor);
                        }
                    } else {
                        creators.addPropertyConstructor(ctor, new SettableBeanProperty[]{constructCreatorProperty(config, beanDesc, name, 0, param)});
                    }
                } else if (isCreator || isVisible) {
                    boolean annotationFound = false;
                    boolean notAnnotatedParamFound = false;
                    SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
                    for (int i = 0; i < argCount; i++) {
                        param = ctor.getParameter(i);
                        name = param == null ? null : intr.findPropertyNameForParam(param);
                        int i2 = (name == null || name.length() == 0) ? 1 : 0;
                        notAnnotatedParamFound |= i2;
                        annotationFound |= !notAnnotatedParamFound ? 1 : 0;
                        if (notAnnotatedParamFound && (annotationFound || isCreator)) {
                            throw new IllegalArgumentException("Argument #" + i + " of constructor " + ctor + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
                        }
                        properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
                    }
                    if (annotationFound) {
                        creators.addPropertyConstructor(ctor, properties);
                    }
                }
            }
        }
    }

    protected void _addDeserializerFactoryMethods(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorContainer creators) throws JsonMappingException {
        for (AnnotatedMember factory : beanDesc.getFactoryMethods()) {
            int argCount = factory.getParameterCount();
            if (argCount >= 1) {
                String name;
                boolean isCreator = intr.hasCreatorAnnotation(factory);
                if (argCount == 1) {
                    name = intr.findPropertyNameForParam(factory.getParameter(0));
                    if (name == null || name.length() == 0) {
                        Class<?> type = factory.getParameterClass(0);
                        if (type == String.class) {
                            if (isCreator || vchecker.isCreatorVisible(factory)) {
                                creators.addStringFactory(factory);
                            }
                        } else if (type == Integer.TYPE || type == Integer.class) {
                            if (isCreator || vchecker.isCreatorVisible(factory)) {
                                creators.addIntFactory(factory);
                            }
                        } else if (type == Long.TYPE || type == Long.class) {
                            if (isCreator || vchecker.isCreatorVisible(factory)) {
                                creators.addLongFactory(factory);
                            }
                        } else if (intr.hasCreatorAnnotation(factory)) {
                            creators.addDelegatingFactory(factory);
                        }
                    }
                } else if (!intr.hasCreatorAnnotation(factory)) {
                    continue;
                }
                SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
                for (int i = 0; i < argCount; i++) {
                    AnnotatedParameter param = factory.getParameter(i);
                    name = intr.findPropertyNameForParam(param);
                    if (name == null || name.length() == 0) {
                        throw new IllegalArgumentException("Argument #" + i + " of factory method " + factory + " has no property name annotation; must have when multiple-paramater static method annotated as Creator");
                    }
                    properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
                }
                creators.addPropertyFactory(factory, properties);
            }
        }
    }

    protected void addBeanProps(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        VisibilityChecker<?> vchecker = config.getDefaultVisibilityChecker();
        if (!config.isEnabled(Feature.AUTO_DETECT_SETTERS)) {
            vchecker = vchecker.withSetterVisibility(Visibility.NONE);
        }
        if (!config.isEnabled(Feature.AUTO_DETECT_FIELDS)) {
            vchecker = vchecker.withFieldVisibility(Visibility.NONE);
        }
        vchecker = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
        Map<String, AnnotatedMethod> setters = beanDesc.findSetters(vchecker);
        AnnotatedMethod anySetter = beanDesc.findAnySetter();
        AnnotationIntrospector intr = config.getAnnotationIntrospector();
        Boolean B = intr.findIgnoreUnknownProperties(beanDesc.getClassInfo());
        if (B != null) {
            builder.setIgnoreUnknownProperties(B.booleanValue());
        }
        HashSet<String> ignored = ArrayBuilders.arrayToSet(intr.findPropertiesToIgnore(beanDesc.getClassInfo()));
        Iterator i$ = ignored.iterator();
        while (i$.hasNext()) {
            builder.addIgnorable((String) i$.next());
        }
        AnnotatedClass ac = beanDesc.getClassInfo();
        for (AnnotatedMethod am : ac.ignoredMemberMethods()) {
            String name = beanDesc.okNameForSetter(am);
            if (name != null) {
                builder.addIgnorable(name);
            }
        }
        for (AnnotatedField af : ac.ignoredFields()) {
            builder.addIgnorable(af.getName());
        }
        HashMap<Class<?>, Boolean> ignoredTypes = new HashMap();
        for (Entry<String, AnnotatedMethod> en : setters.entrySet()) {
            SettableBeanProperty prop;
            name = (String) en.getKey();
            if (!ignored.contains(name)) {
                AnnotatedMethod setter = (AnnotatedMethod) en.getValue();
                if (isIgnorableType(config, beanDesc, setter.getParameterClass(0), ignoredTypes)) {
                    builder.addIgnorable(name);
                } else {
                    prop = constructSettableProperty(config, beanDesc, name, setter);
                    if (prop != null) {
                        builder.addProperty(prop);
                    }
                }
            }
        }
        if (anySetter != null) {
            builder.setAnySetter(constructAnySetter(config, beanDesc, anySetter));
        }
        HashSet<String> addedProps = new HashSet(setters.keySet());
        for (Entry<String, AnnotatedField> en2 : beanDesc.findDeserializableFields(vchecker, addedProps).entrySet()) {
            name = (String) en2.getKey();
            if (!(ignored.contains(name) || builder.hasProperty(name))) {
                AnnotatedField field = (AnnotatedField) en2.getValue();
                if (isIgnorableType(config, beanDesc, field.getRawType(), ignoredTypes)) {
                    builder.addIgnorable(name);
                } else {
                    prop = constructSettableProperty(config, beanDesc, name, field);
                    if (prop != null) {
                        builder.addProperty(prop);
                        addedProps.add(name);
                    }
                }
            }
        }
        if (config.isEnabled(Feature.USE_GETTERS_AS_SETTERS)) {
            for (Entry<String, AnnotatedMethod> en3 : beanDesc.findGetters(vchecker, addedProps).entrySet()) {
                AnnotatedMethod getter = (AnnotatedMethod) en3.getValue();
                Class<?> rt = getter.getRawType();
                if (!Collection.class.isAssignableFrom(rt)) {
                    if (!Map.class.isAssignableFrom(rt)) {
                    }
                }
                name = (String) en3.getKey();
                if (!(ignored.contains(name) || builder.hasProperty(name))) {
                    builder.addProperty(constructSetterlessProperty(config, beanDesc, name, getter));
                    addedProps.add(name);
                }
            }
        }
    }

    protected void addReferenceProperties(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder) throws JsonMappingException {
        Map<String, AnnotatedMember> refs = beanDesc.findBackReferenceProperties();
        if (refs != null) {
            for (Entry<String, AnnotatedMember> en : refs.entrySet()) {
                String name = (String) en.getKey();
                AnnotatedMember m = (AnnotatedMember) en.getValue();
                if (m instanceof AnnotatedMethod) {
                    builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedMethod) m));
                } else {
                    builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedField) m));
                }
            }
        }
    }

    protected SettableAnyProperty constructAnySetter(DeserializationConfig config, BasicBeanDescription beanDesc, AnnotatedMethod setter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            setter.fixAccess();
        }
        JavaType type = TypeFactory.type(setter.getParameterType(1), beanDesc.bindingsForBeanType());
        Std property = new Std(setter.getName(), type, beanDesc.getClassAnnotations(), setter);
        type = resolveType(config, beanDesc, type, setter, property);
        JsonDeserializer<Object> deser = findDeserializerFromAnnotation(config, setter, property);
        if (deser == null) {
            return new SettableAnyProperty(property, setter, modifyTypeByAnnotation(config, setter, type, property.getName()));
        }
        SettableAnyProperty prop = new SettableAnyProperty(property, setter, type);
        prop.setValueDeserializer(deser);
        return prop;
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod setter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            setter.fixAccess();
        }
        JavaType t0 = TypeFactory.type(setter.getParameterType(0), beanDesc.bindingsForBeanType());
        Std property = new Std(name, t0, beanDesc.getClassAnnotations(), setter);
        JavaType type = resolveType(config, beanDesc, t0, setter, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, setter, property);
        type = modifyTypeByAnnotation(config, setter, type, name);
        SettableBeanProperty prop = new MethodProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), setter);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(setter);
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        return prop;
    }

    protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedField field) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            field.fixAccess();
        }
        JavaType t0 = TypeFactory.type(field.getGenericType(), beanDesc.bindingsForBeanType());
        Std property = new Std(name, t0, beanDesc.getClassAnnotations(), field);
        JavaType type = resolveType(config, beanDesc, t0, field, property);
        if (type != t0) {
            property = property.withType(type);
        }
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, field, property);
        type = modifyTypeByAnnotation(config, field, type, name);
        SettableBeanProperty prop = new FieldProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), field);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(field);
        if (ref != null && ref.isManagedReference()) {
            prop.setManagedReferenceName(ref.getName());
        }
        return prop;
    }

    protected SettableBeanProperty constructSetterlessProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod getter) throws JsonMappingException {
        if (config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
            getter.fixAccess();
        }
        JavaType type = getter.getType(beanDesc.bindingsForBeanType());
        JsonDeserializer<Object> propDeser = findDeserializerFromAnnotation(config, getter, new Std(name, type, beanDesc.getClassAnnotations(), getter));
        type = modifyTypeByAnnotation(config, getter, type, name);
        SettableBeanProperty prop = new SetterlessProperty(name, type, (TypeDeserializer) type.getTypeHandler(), beanDesc.getClassAnnotations(), getter);
        if (propDeser != null) {
            prop.setValueDeserializer(propDeser);
        }
        return prop;
    }

    protected boolean isPotentialBeanType(Class<?> type) {
        String typeStr = ClassUtil.canBeABeanType(type);
        if (typeStr != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        } else if (ClassUtil.isProxyType(type)) {
            throw new IllegalArgumentException("Can not deserialize Proxy class " + type.getName() + " as a Bean");
        } else {
            typeStr = ClassUtil.isLocalType(type);
            if (typeStr == null) {
                return true;
            }
            throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
        }
    }

    protected boolean isIgnorableType(DeserializationConfig config, BasicBeanDescription beanDesc, Class<?> type, Map<Class<?>, Boolean> ignoredTypes) {
        Boolean status = (Boolean) ignoredTypes.get(type);
        if (status == null) {
            status = config.getAnnotationIntrospector().isIgnorableType(((BasicBeanDescription) config.introspectClassAnnotations(type)).getClassInfo());
            if (status == null) {
                status = Boolean.FALSE;
            }
        }
        return status.booleanValue();
    }
}
