package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.map.AnnotationIntrospector.Pair;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.util.StdDateFormat;
import org.codehaus.jackson.type.JavaType;

public class SerializationConfig implements MapperConfig<SerializationConfig> {
    protected static final int DEFAULT_FEATURE_FLAGS;
    protected AnnotationIntrospector _annotationIntrospector;
    protected ClassIntrospector<? extends BeanDescription> _classIntrospector;
    protected DateFormat _dateFormat;
    protected int _featureFlags;
    protected FilterProvider _filterProvider;
    protected HashMap<ClassKey, Class<?>> _mixInAnnotations;
    protected boolean _mixInAnnotationsShared;
    protected Inclusion _serializationInclusion;
    protected Class<?> _serializationView;
    protected SubtypeResolver _subtypeResolver;
    protected final TypeResolverBuilder<?> _typer;
    protected VisibilityChecker<?> _visibilityChecker;

    public enum Feature {
        USE_ANNOTATIONS(true),
        AUTO_DETECT_GETTERS(true),
        AUTO_DETECT_IS_GETTERS(true),
        AUTO_DETECT_FIELDS(true),
        CAN_OVERRIDE_ACCESS_MODIFIERS(true),
        WRITE_NULL_PROPERTIES(true),
        USE_STATIC_TYPING(false),
        DEFAULT_VIEW_INCLUSION(true),
        WRAP_ROOT_VALUE(false),
        INDENT_OUTPUT(false),
        FAIL_ON_EMPTY_BEANS(true),
        WRAP_EXCEPTIONS(true),
        CLOSE_CLOSEABLE(false),
        FLUSH_AFTER_WRITE_VALUE(true),
        WRITE_DATES_AS_TIMESTAMPS(true),
        WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS(false),
        WRITE_ENUMS_USING_TO_STRING(false),
        WRITE_NULL_MAP_VALUES(true);
        
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
    }

    public SerializationConfig(ClassIntrospector<? extends BeanDescription> intr, AnnotationIntrospector annIntr, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver) {
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._dateFormat = StdDateFormat.instance;
        this._serializationInclusion = null;
        this._classIntrospector = intr;
        this._annotationIntrospector = annIntr;
        this._typer = null;
        this._visibilityChecker = vc;
        this._subtypeResolver = subtypeResolver;
        this._filterProvider = null;
    }

    protected SerializationConfig(SerializationConfig src, HashMap<ClassKey, Class<?>> mixins, TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver, FilterProvider filterProvider) {
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._dateFormat = StdDateFormat.instance;
        this._serializationInclusion = null;
        this._classIntrospector = src._classIntrospector;
        this._annotationIntrospector = src._annotationIntrospector;
        this._featureFlags = src._featureFlags;
        this._dateFormat = src._dateFormat;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = src._serializationView;
        this._mixInAnnotations = mixins;
        this._typer = typer;
        this._visibilityChecker = vc;
        this._subtypeResolver = subtypeResolver;
        this._filterProvider = filterProvider;
    }

    protected SerializationConfig(SerializationConfig src, FilterProvider filterProvider) {
        this._featureFlags = DEFAULT_FEATURE_FLAGS;
        this._dateFormat = StdDateFormat.instance;
        this._serializationInclusion = null;
        this._classIntrospector = src._classIntrospector;
        this._annotationIntrospector = src._annotationIntrospector;
        this._featureFlags = src._featureFlags;
        this._dateFormat = src._dateFormat;
        this._serializationInclusion = src._serializationInclusion;
        this._serializationView = src._serializationView;
        this._mixInAnnotations = src._mixInAnnotations;
        this._typer = src._typer;
        this._visibilityChecker = src._visibilityChecker;
        this._subtypeResolver = src._subtypeResolver;
        this._filterProvider = filterProvider;
    }

    public SerializationConfig withFilters(FilterProvider filterProvider) {
        return new SerializationConfig(this, filterProvider);
    }

    public SerializationConfig createUnshared(TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver, FilterProvider filterProvider) {
        HashMap<ClassKey, Class<?>> mixins = this._mixInAnnotations;
        this._mixInAnnotationsShared = true;
        return new SerializationConfig(this, mixins, typer, vc, subtypeResolver, filterProvider);
    }

    public void fromAnnotations(Class<?> cls) {
        AnnotatedClass ac = AnnotatedClass.construct(cls, this._annotationIntrospector, null);
        this._visibilityChecker = this._annotationIntrospector.findAutoDetectVisibility(ac, this._visibilityChecker);
        Inclusion incl = this._annotationIntrospector.findSerializationInclusion(ac, null);
        if (incl != this._serializationInclusion) {
            setSerializationInclusion(incl);
        }
        Typing typing = this._annotationIntrospector.findSerializationTyping(ac);
        if (typing != null) {
            set(Feature.USE_STATIC_TYPING, typing == Typing.STATIC);
        }
    }

    public SerializationConfig createUnshared(TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver) {
        HashMap<ClassKey, Class<?>> mixins = this._mixInAnnotations;
        this._mixInAnnotationsShared = true;
        return new SerializationConfig(this, mixins, typer, vc, subtypeResolver, this._filterProvider);
    }

    public void setIntrospector(ClassIntrospector<? extends BeanDescription> i) {
        this._classIntrospector = i;
    }

    public AnnotationIntrospector getAnnotationIntrospector() {
        if (isEnabled(Feature.USE_ANNOTATIONS)) {
            return this._annotationIntrospector;
        }
        return AnnotationIntrospector.nopInstance();
    }

    public void setAnnotationIntrospector(AnnotationIntrospector ai) {
        this._annotationIntrospector = ai;
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
        this._dateFormat = df;
        set(Feature.WRITE_DATES_AS_TIMESTAMPS, df == null);
    }

    public TypeResolverBuilder<?> getDefaultTyper(JavaType baseType) {
        return this._typer;
    }

    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        return this._visibilityChecker;
    }

    public <T extends BeanDescription> T introspectClassAnnotations(Class<?> cls) {
        return this._classIntrospector.forClassAnnotations(this, cls, this);
    }

    public <T extends BeanDescription> T introspectDirectClassAnnotations(Class<?> cls) {
        return this._classIntrospector.forDirectClassAnnotations(this, cls, this);
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

    public <T extends BeanDescription> T introspect(JavaType type) {
        return this._classIntrospector.forSerialization(this, type, this);
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

    public Class<?> getSerializationView() {
        return this._serializationView;
    }

    public Inclusion getSerializationInclusion() {
        if (this._serializationInclusion != null) {
            return this._serializationInclusion;
        }
        return isEnabled(Feature.WRITE_NULL_PROPERTIES) ? Inclusion.ALWAYS : Inclusion.NON_NULL;
    }

    public void setSerializationInclusion(Inclusion props) {
        this._serializationInclusion = props;
        if (props == Inclusion.NON_NULL) {
            disable(Feature.WRITE_NULL_PROPERTIES);
        } else {
            enable(Feature.WRITE_NULL_PROPERTIES);
        }
    }

    public void setSerializationView(Class<?> view) {
        this._serializationView = view;
    }

    public FilterProvider getFilterProvider() {
        return this._filterProvider;
    }

    public String toString() {
        return "[SerializationConfig: flags=0x" + Integer.toHexString(this._featureFlags) + "]";
    }
}
