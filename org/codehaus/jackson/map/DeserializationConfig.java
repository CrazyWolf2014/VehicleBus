package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.Base64Variants;
import org.codehaus.jackson.map.AnnotationIntrospector.Pair;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.util.LinkedNode;
import org.codehaus.jackson.map.util.StdDateFormat;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.type.JavaType;

public class DeserializationConfig implements MapperConfig<DeserializationConfig> {
    protected static final DateFormat DEFAULT_DATE_FORMAT;
    protected static final int DEFAULT_FEATURE_FLAGS;
    protected AbstractTypeResolver _abstractTypeResolver;
    protected AnnotationIntrospector _annotationIntrospector;
    protected ClassIntrospector<? extends BeanDescription> _classIntrospector;
    protected DateFormat _dateFormat;
    protected int _featureFlags;
    protected HashMap<ClassKey, Class<?>> _mixInAnnotations;
    protected boolean _mixInAnnotationsShared;
    protected JsonNodeFactory _nodeFactory;
    protected LinkedNode<DeserializationProblemHandler> _problemHandlers;
    protected SubtypeResolver _subtypeResolver;
    protected final TypeResolverBuilder<?> _typer;
    protected VisibilityChecker<?> _visibilityChecker;

    public enum Feature {
        USE_ANNOTATIONS(true),
        AUTO_DETECT_SETTERS(true),
        AUTO_DETECT_CREATORS(true),
        AUTO_DETECT_FIELDS(true),
        USE_GETTERS_AS_SETTERS(true),
        CAN_OVERRIDE_ACCESS_MODIFIERS(true),
        USE_BIG_DECIMAL_FOR_FLOATS(false),
        USE_BIG_INTEGER_FOR_INTS(false),
        READ_ENUMS_USING_TO_STRING(false),
        FAIL_ON_UNKNOWN_PROPERTIES(true),
        FAIL_ON_NULL_FOR_PRIMITIVES(false),
        FAIL_ON_NUMBERS_FOR_ENUMS(false),
        WRAP_EXCEPTIONS(true),
        WRAP_ROOT_VALUE(false);
        
        final boolean _defaultState;

        public static int collectDefaults() {
            int flags = 0;
            for (Feature f : values()) {
                if (f.enabledByDefault()) {
                    flags |= f.getMask();
                }
            }
            return flags;
        }

        private Feature(boolean defaultState) {
            this._defaultState = defaultState;
        }

        public boolean enabledByDefault() {
            return this._defaultState;
        }

        public int getMask() {
            return 1 << ordinal();
        }
    }

    static {
        DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
        DEFAULT_DATE_FORMAT = StdDateFormat.instance;
    }

    public DeserializationConfig(ClassIntrospector<? extends BeanDescription> intr, AnnotationIntrospector annIntr, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver) {
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._dateFormat = DEFAULT_DATE_FORMAT;
        this._classIntrospector = intr;
        this._annotationIntrospector = annIntr;
        this._typer = null;
        this._visibilityChecker = vc;
        this._subtypeResolver = subtypeResolver;
        this._nodeFactory = JsonNodeFactory.instance;
    }

    protected DeserializationConfig(DeserializationConfig src, HashMap<ClassKey, Class<?>> mixins, TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver) {
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._dateFormat = DEFAULT_DATE_FORMAT;
        this._classIntrospector = src._classIntrospector;
        this._annotationIntrospector = src._annotationIntrospector;
        this._abstractTypeResolver = src._abstractTypeResolver;
        this._featureFlags = src._featureFlags;
        this._problemHandlers = src._problemHandlers;
        this._dateFormat = src._dateFormat;
        this._nodeFactory = src._nodeFactory;
        this._mixInAnnotations = mixins;
        this._typer = typer;
        this._visibilityChecker = vc;
        this._subtypeResolver = subtypeResolver;
    }

    public void enable(Feature f) {
        this._featureFlags |= f.getMask();
    }

    public void disable(Feature f) {
        this._featureFlags &= f.getMask() ^ -1;
    }

    public void set(Feature f, boolean state) {
        if (state) {
            enable(f);
        } else {
            disable(f);
        }
    }

    public final boolean isEnabled(Feature f) {
        return (this._featureFlags & f.getMask()) != 0;
    }

    public void fromAnnotations(Class<?> cls) {
        this._visibilityChecker = this._annotationIntrospector.findAutoDetectVisibility(AnnotatedClass.construct(cls, this._annotationIntrospector, null), this._visibilityChecker);
    }

    public DeserializationConfig createUnshared(TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver) {
        HashMap<ClassKey, Class<?>> mixins = this._mixInAnnotations;
        this._mixInAnnotationsShared = true;
        return new DeserializationConfig(this, mixins, typer, vc, subtypeResolver);
    }

    public DeserializationConfig createUnshared(JsonNodeFactory nf) {
        DeserializationConfig config = createUnshared(this._typer, this._visibilityChecker, this._subtypeResolver);
        config.setNodeFactory(nf);
        return config;
    }

    public void setIntrospector(ClassIntrospector<? extends BeanDescription> i) {
        this._classIntrospector = i;
    }

    public AnnotationIntrospector getAnnotationIntrospector() {
        if (isEnabled(Feature.USE_ANNOTATIONS)) {
            return this._annotationIntrospector;
        }
        return NopAnnotationIntrospector.instance;
    }

    public void setAnnotationIntrospector(AnnotationIntrospector introspector) {
        this._annotationIntrospector = introspector;
    }

    public void insertAnnotationIntrospector(AnnotationIntrospector introspector) {
        this._annotationIntrospector = Pair.create(introspector, this._annotationIntrospector);
    }

    public void appendAnnotationIntrospector(AnnotationIntrospector introspector) {
        this._annotationIntrospector = Pair.create(this._annotationIntrospector, introspector);
    }

    public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins) {
        HashMap<ClassKey, Class<?>> mixins = null;
        if (sourceMixins != null && sourceMixins.size() > 0) {
            mixins = new HashMap(sourceMixins.size());
            for (Entry<Class<?>, Class<?>> en : sourceMixins.entrySet()) {
                mixins.put(new ClassKey((Class) en.getKey()), en.getValue());
            }
        }
        this._mixInAnnotationsShared = false;
        this._mixInAnnotations = mixins;
    }

    public void addMixInAnnotations(Class<?> target, Class<?> mixinSource) {
        if (this._mixInAnnotations == null || this._mixInAnnotationsShared) {
            this._mixInAnnotationsShared = false;
            this._mixInAnnotations = new HashMap();
        }
        this._mixInAnnotations.put(new ClassKey(target), mixinSource);
    }

    public Class<?> findMixInClassFor(Class<?> cls) {
        return this._mixInAnnotations == null ? null : (Class) this._mixInAnnotations.get(new ClassKey(cls));
    }

    public DateFormat getDateFormat() {
        return this._dateFormat;
    }

    public void setDateFormat(DateFormat df) {
        if (df == null) {
            df = StdDateFormat.instance;
        }
        this._dateFormat = df;
    }

    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        return this._visibilityChecker;
    }

    public TypeResolverBuilder<?> getDefaultTyper(JavaType baseType) {
        return this._typer;
    }

    public SubtypeResolver getSubtypeResolver() {
        if (this._subtypeResolver == null) {
            this._subtypeResolver = new StdSubtypeResolver();
        }
        return this._subtypeResolver;
    }

    public void setSubtypeResolver(SubtypeResolver r) {
        this._subtypeResolver = r;
    }

    public <T extends BeanDescription> T introspectClassAnnotations(Class<?> cls) {
        return this._classIntrospector.forClassAnnotations(this, cls, this);
    }

    public <T extends BeanDescription> T introspectDirectClassAnnotations(Class<?> cls) {
        return this._classIntrospector.forDirectClassAnnotations(this, cls, this);
    }

    public LinkedNode<DeserializationProblemHandler> getProblemHandlers() {
        return this._problemHandlers;
    }

    public void addHandler(DeserializationProblemHandler h) {
        if (!LinkedNode.contains(this._problemHandlers, h)) {
            this._problemHandlers = new LinkedNode(h, this._problemHandlers);
        }
    }

    public void clearHandlers() {
        this._problemHandlers = null;
    }

    public <T extends BeanDescription> T introspect(JavaType type) {
        return this._classIntrospector.forDeserialization(this, type, this);
    }

    public <T extends BeanDescription> T introspectForCreation(JavaType type) {
        return this._classIntrospector.forCreation(this, type, this);
    }

    public AbstractTypeResolver getAbstractTypeResolver() {
        return this._abstractTypeResolver;
    }

    public void setAbstractTypeResolver(AbstractTypeResolver atr) {
        this._abstractTypeResolver = atr;
    }

    public Base64Variant getBase64Variant() {
        return Base64Variants.getDefaultVariant();
    }

    public void setNodeFactory(JsonNodeFactory nf) {
        this._nodeFactory = nf;
    }

    public final JsonNodeFactory getNodeFactory() {
        return this._nodeFactory;
    }
}
