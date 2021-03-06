package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonClass;
import org.codehaus.jackson.annotate.JsonContentClass;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonGetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnoreType;
import org.codehaus.jackson.annotate.JsonKeyClass;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.codehaus.jackson.annotate.JsonSetter;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.JsonSerializer.None;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.annotate.JsonTypeIdResolver;
import org.codehaus.jackson.map.annotate.JsonTypeResolver;
import org.codehaus.jackson.map.annotate.JsonView;
import org.codehaus.jackson.map.annotate.NoClass;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.ser.impl.RawSerializer;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;
import org.xmlpull.v1.XmlPullParser;

public class JacksonAnnotationIntrospector extends AnnotationIntrospector {
    public boolean isHandled(Annotation ann) {
        return ann.annotationType().getAnnotation(JacksonAnnotation.class) != null;
    }

    public String findEnumValue(Enum<?> value) {
        return value.name();
    }

    public Boolean findCachability(AnnotatedClass ac) {
        JsonCachable ann = (JsonCachable) ac.getAnnotation(JsonCachable.class);
        if (ann == null) {
            return null;
        }
        return ann.value() ? Boolean.TRUE : Boolean.FALSE;
    }

    public String findRootName(AnnotatedClass ac) {
        return null;
    }

    public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        JsonIgnoreProperties ignore = (JsonIgnoreProperties) ac.getAnnotation(JsonIgnoreProperties.class);
        return ignore == null ? null : ignore.value();
    }

    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        JsonIgnoreProperties ignore = (JsonIgnoreProperties) ac.getAnnotation(JsonIgnoreProperties.class);
        return ignore == null ? null : Boolean.valueOf(ignore.ignoreUnknown());
    }

    public Boolean isIgnorableType(AnnotatedClass ac) {
        JsonIgnoreType ignore = (JsonIgnoreType) ac.getAnnotation(JsonIgnoreType.class);
        return ignore == null ? null : Boolean.valueOf(ignore.value());
    }

    public Object findFilterId(AnnotatedClass ac) {
        JsonFilter ann = (JsonFilter) ac.getAnnotation(JsonFilter.class);
        if (ann != null) {
            String id = ann.value();
            if (id.length() > 0) {
                return id;
            }
        }
        return null;
    }

    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
        JsonAutoDetect ann = (JsonAutoDetect) ac.getAnnotation(JsonAutoDetect.class);
        return ann == null ? checker : checker.with(ann);
    }

    public ReferenceProperty findReferenceType(AnnotatedMember member) {
        JsonManagedReference ref1 = (JsonManagedReference) member.getAnnotation(JsonManagedReference.class);
        if (ref1 != null) {
            return ReferenceProperty.managed(ref1.value());
        }
        JsonBackReference ref2 = (JsonBackReference) member.getAnnotation(JsonBackReference.class);
        if (ref2 != null) {
            return ReferenceProperty.back(ref2.value());
        }
        return null;
    }

    public TypeResolverBuilder<?> findTypeResolver(AnnotatedClass ac, JavaType baseType) {
        return _findTypeResolver(ac, baseType);
    }

    public TypeResolverBuilder<?> findPropertyTypeResolver(AnnotatedMember am, JavaType baseType) {
        if (baseType.isContainerType()) {
            return null;
        }
        return _findTypeResolver(am, baseType);
    }

    public TypeResolverBuilder<?> findPropertyContentTypeResolver(AnnotatedMember am, JavaType containerType) {
        if (containerType.isContainerType()) {
            return _findTypeResolver(am, containerType);
        }
        throw new IllegalArgumentException("Must call method with a container type (got " + containerType + ")");
    }

    public List<NamedType> findSubtypes(Annotated a) {
        JsonSubTypes t = (JsonSubTypes) a.getAnnotation(JsonSubTypes.class);
        if (t == null) {
            return null;
        }
        Type[] types = t.value();
        List<NamedType> result = new ArrayList(types.length);
        for (Type type : types) {
            result.add(new NamedType(type.value(), type.name()));
        }
        return result;
    }

    public String findTypeName(AnnotatedClass ac) {
        JsonTypeName tn = (JsonTypeName) ac.getAnnotation(JsonTypeName.class);
        return tn == null ? null : tn.value();
    }

    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return _isIgnorable(m);
    }

    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return _isIgnorable(c);
    }

    public boolean isIgnorableField(AnnotatedField f) {
        return _isIgnorable(f);
    }

    public Object findSerializer(Annotated a, BeanProperty property) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            Class<? extends JsonSerializer<?>> serClass = ann.using();
            if (serClass != None.class) {
                return serClass;
            }
        }
        JsonRawValue annRaw = (JsonRawValue) a.getAnnotation(JsonRawValue.class);
        if (annRaw == null || !annRaw.value()) {
            return null;
        }
        return new RawSerializer(a.getRawType());
    }

    public Inclusion findSerializationInclusion(Annotated a, Inclusion defValue) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            return ann.include();
        }
        JsonWriteNullProperties oldAnn = (JsonWriteNullProperties) a.getAnnotation(JsonWriteNullProperties.class);
        if (oldAnn != null) {
            return oldAnn.value() ? Inclusion.ALWAYS : Inclusion.NON_NULL;
        } else {
            return defValue;
        }
    }

    public Class<?> findSerializationType(Annotated am) {
        JsonSerialize ann = (JsonSerialize) am.getAnnotation(JsonSerialize.class);
        if (ann != null) {
            Class<?> cls = ann.as();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public Typing findSerializationTyping(Annotated a) {
        JsonSerialize ann = (JsonSerialize) a.getAnnotation(JsonSerialize.class);
        return ann == null ? null : ann.typing();
    }

    public Class<?>[] findSerializationViews(Annotated a) {
        JsonView ann = (JsonView) a.getAnnotation(JsonView.class);
        return ann == null ? null : ann.value();
    }

    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        JsonPropertyOrder order = (JsonPropertyOrder) ac.getAnnotation(JsonPropertyOrder.class);
        return order == null ? null : order.value();
    }

    public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
        JsonPropertyOrder order = (JsonPropertyOrder) ac.getAnnotation(JsonPropertyOrder.class);
        return order == null ? null : Boolean.valueOf(order.alphabetic());
    }

    public String findGettablePropertyName(AnnotatedMethod am) {
        JsonProperty pann = (JsonProperty) am.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        JsonGetter ann = (JsonGetter) am.getAnnotation(JsonGetter.class);
        if (ann != null) {
            return ann.value();
        }
        if (am.hasAnnotation(JsonSerialize.class) || am.hasAnnotation(JsonView.class)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return null;
    }

    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        JsonValue ann = (JsonValue) am.getAnnotation(JsonValue.class);
        return ann != null && ann.value();
    }

    public String findSerializablePropertyName(AnnotatedField af) {
        JsonProperty pann = (JsonProperty) af.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        if (af.hasAnnotation(JsonSerialize.class) || af.hasAnnotation(JsonView.class)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return null;
    }

    public Class<? extends JsonDeserializer<?>> findDeserializer(Annotated a, BeanProperty property) {
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            Class<? extends JsonDeserializer<?>> deserClass = ann.using();
            if (deserClass != JsonDeserializer.None.class) {
                return deserClass;
            }
        }
        return null;
    }

    public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated a) {
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            Class<? extends KeyDeserializer> deserClass = ann.keyUsing();
            if (deserClass != KeyDeserializer.None.class) {
                return deserClass;
            }
        }
        return null;
    }

    public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated a) {
        JsonDeserialize ann = (JsonDeserialize) a.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            Class<? extends JsonDeserializer<?>> deserClass = ann.contentUsing();
            if (deserClass != JsonDeserializer.None.class) {
                return deserClass;
            }
        }
        return null;
    }

    public Class<?> findDeserializationType(Annotated am, JavaType baseType, String propName) {
        Class<?> cls;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            cls = ann.as();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        JsonClass oldAnn = (JsonClass) am.getAnnotation(JsonClass.class);
        if (oldAnn != null) {
            cls = oldAnn.value();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName) {
        Class<?> cls;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            cls = ann.keyAs();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        JsonKeyClass oldAnn = (JsonKeyClass) am.getAnnotation(JsonKeyClass.class);
        if (oldAnn != null) {
            cls = oldAnn.value();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType, String propName) {
        Class<?> cls;
        JsonDeserialize ann = (JsonDeserialize) am.getAnnotation(JsonDeserialize.class);
        if (ann != null) {
            cls = ann.contentAs();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        JsonContentClass oldAnn = (JsonContentClass) am.getAnnotation(JsonContentClass.class);
        if (oldAnn != null) {
            cls = oldAnn.value();
            if (cls != NoClass.class) {
                return cls;
            }
        }
        return null;
    }

    public String findSettablePropertyName(AnnotatedMethod am) {
        JsonProperty pann = (JsonProperty) am.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        JsonSetter ann = (JsonSetter) am.getAnnotation(JsonSetter.class);
        if (ann != null) {
            return ann.value();
        }
        if (am.hasAnnotation(JsonDeserialize.class) || am.hasAnnotation(JsonView.class)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return null;
    }

    public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
        return am.hasAnnotation(JsonAnySetter.class);
    }

    public boolean hasAnyGetterAnnotation(AnnotatedMethod am) {
        return am.hasAnnotation(JsonAnyGetter.class);
    }

    public boolean hasCreatorAnnotation(Annotated a) {
        return a.hasAnnotation(JsonCreator.class);
    }

    public String findDeserializablePropertyName(AnnotatedField af) {
        JsonProperty pann = (JsonProperty) af.getAnnotation(JsonProperty.class);
        if (pann != null) {
            return pann.value();
        }
        if (af.hasAnnotation(JsonDeserialize.class) || af.hasAnnotation(JsonView.class)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return null;
    }

    public String findPropertyNameForParam(AnnotatedParameter param) {
        if (param != null) {
            JsonProperty pann = (JsonProperty) param.getAnnotation(JsonProperty.class);
            if (pann != null) {
                return pann.value();
            }
        }
        return null;
    }

    protected boolean _isIgnorable(Annotated a) {
        JsonIgnore ann = (JsonIgnore) a.getAnnotation(JsonIgnore.class);
        return ann != null && ann.value();
    }

    protected TypeResolverBuilder<?> _findTypeResolver(Annotated ann, JavaType baseType) {
        TypeResolverBuilder<?> b;
        JsonTypeInfo info = (JsonTypeInfo) ann.getAnnotation(JsonTypeInfo.class);
        JsonTypeResolver resAnn = (JsonTypeResolver) ann.getAnnotation(JsonTypeResolver.class);
        if (resAnn != null) {
            if (info == null) {
                return null;
            }
            b = (TypeResolverBuilder) ClassUtil.createInstance(resAnn.value(), false);
        } else if (info == null || info.use() == Id.NONE) {
            return null;
        } else {
            b = _constructStdTypeResolverBuilder();
        }
        JsonTypeIdResolver idResInfo = (JsonTypeIdResolver) ann.getAnnotation(JsonTypeIdResolver.class);
        TypeIdResolver idRes = idResInfo == null ? null : (TypeIdResolver) ClassUtil.createInstance(idResInfo.value(), true);
        if (idRes != null) {
            idRes.init(baseType);
        }
        return b.init(info.use(), idRes).inclusion(info.include()).typeProperty(info.property());
    }

    protected StdTypeResolverBuilder _constructStdTypeResolverBuilder() {
        return new StdTypeResolverBuilder();
    }
}
