package org.codehaus.jackson.xc;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElement.DEFAULT;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.Versioned;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.KeyDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.AnnotatedParameter;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.VersionUtil;
import org.xmlpull.v1.XmlPullParser;

public class JaxbAnnotationIntrospector extends AnnotationIntrospector implements Versioned {
    protected static final String MARKER_FOR_DEFAULT = "##default";
    private static final ThreadLocal<SoftReference<PropertyDescriptors>> _propertyDescriptors;
    protected final JsonDeserializer<?> _dataHandlerDeserializer;
    protected final JsonSerializer<?> _dataHandlerSerializer;
    protected final String _jaxbPackageName;

    /* renamed from: org.codehaus.jackson.xc.JaxbAnnotationIntrospector.1 */
    static /* synthetic */ class C09521 {
        static final /* synthetic */ int[] $SwitchMap$javax$xml$bind$annotation$XmlAccessType;

        static {
            $SwitchMap$javax$xml$bind$annotation$XmlAccessType = new int[XmlAccessType.values().length];
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.FIELD.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.NONE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.PROPERTY.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$javax$xml$bind$annotation$XmlAccessType[XmlAccessType.PUBLIC_MEMBER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private static class AnnotatedProperty implements AnnotatedElement {
        private final PropertyDescriptor pd;

        private AnnotatedProperty(PropertyDescriptor pd) {
            this.pd = pd;
        }

        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            Method m = this.pd.getReadMethod();
            if (m != null && m.isAnnotationPresent(annotationClass)) {
                return true;
            }
            m = this.pd.getWriteMethod();
            if (m == null || !m.isAnnotationPresent(annotationClass)) {
                return false;
            }
            return true;
        }

        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            Method m = this.pd.getReadMethod();
            if (m != null) {
                T ann = m.getAnnotation(annotationClass);
                if (ann != null) {
                    return ann;
                }
            }
            m = this.pd.getWriteMethod();
            if (m != null) {
                return m.getAnnotation(annotationClass);
            }
            return null;
        }

        public Annotation[] getAnnotations() {
            throw new UnsupportedOperationException();
        }

        public Annotation[] getDeclaredAnnotations() {
            throw new UnsupportedOperationException();
        }
    }

    protected static final class PropertyDescriptors {
        private Map<String, PropertyDescriptor> _byMethodName;
        private Map<String, PropertyDescriptor> _byPropertyName;
        private final Class<?> _forClass;
        private final List<PropertyDescriptor> _properties;

        public PropertyDescriptors(Class<?> forClass, List<PropertyDescriptor> properties) {
            this._forClass = forClass;
            this._properties = properties;
        }

        public Class<?> getBeanClass() {
            return this._forClass;
        }

        public PropertyDescriptor findByPropertyName(String name) {
            if (this._byPropertyName == null) {
                this._byPropertyName = new HashMap(this._properties.size());
                for (PropertyDescriptor desc : this._properties) {
                    this._byPropertyName.put(desc.getName(), desc);
                }
            }
            return (PropertyDescriptor) this._byPropertyName.get(name);
        }

        public PropertyDescriptor findByMethodName(String name) {
            if (this._byMethodName == null) {
                this._byMethodName = new HashMap(this._properties.size());
                for (PropertyDescriptor desc : this._properties) {
                    Method getter = desc.getReadMethod();
                    if (getter != null) {
                        this._byMethodName.put(getter.getName(), desc);
                    }
                    Method setter = desc.getWriteMethod();
                    if (setter != null) {
                        this._byMethodName.put(setter.getName(), desc);
                    }
                }
            }
            return (PropertyDescriptor) this._byMethodName.get(name);
        }

        public static PropertyDescriptors find(Class<?> forClass) throws IntrospectionException {
            List<PropertyDescriptor> descriptors;
            BeanInfo beanInfo = Introspector.getBeanInfo(forClass);
            if (beanInfo.getPropertyDescriptors().length == 0) {
                descriptors = Collections.emptyList();
            } else {
                descriptors = new ArrayList();
                Map<String, PropertyDescriptor> partials = null;
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                    Method read = pd.getReadMethod();
                    String readName = read == null ? null : JaxbAnnotationIntrospector.findJaxbPropertyName(read, pd.getPropertyType(), null);
                    Method write = pd.getWriteMethod();
                    String writeName = write == null ? null : JaxbAnnotationIntrospector.findJaxbPropertyName(write, pd.getPropertyType(), null);
                    if (write == null) {
                        if (readName == null) {
                            readName = pd.getName();
                        }
                        partials = _processReadMethod(partials, read, readName, descriptors);
                    } else if (read == null) {
                        if (writeName == null) {
                            writeName = pd.getName();
                        }
                        partials = _processWriteMethod(partials, write, writeName, descriptors);
                    } else if (readName == null || writeName == null || readName.equals(writeName)) {
                        String name;
                        if (readName != null) {
                            name = readName;
                        } else if (writeName != null) {
                            name = writeName;
                        } else {
                            name = pd.getName();
                        }
                        descriptors.add(new PropertyDescriptor(name, read, write));
                    } else {
                        partials = _processWriteMethod(_processReadMethod(partials, read, readName, descriptors), write, writeName, descriptors);
                    }
                }
            }
            return new PropertyDescriptors(forClass, descriptors);
        }

        private static Map<String, PropertyDescriptor> _processReadMethod(Map<String, PropertyDescriptor> partials, Method method, String propertyName, List<PropertyDescriptor> pds) throws IntrospectionException {
            if (partials == null) {
                partials = new HashMap();
            } else {
                PropertyDescriptor pd = (PropertyDescriptor) partials.get(propertyName);
                if (pd != null) {
                    pd.setReadMethod(method);
                    if (pd.getWriteMethod() != null) {
                        pds.add(pd);
                        partials.remove(propertyName);
                        return partials;
                    }
                }
            }
            partials.put(propertyName, new PropertyDescriptor(propertyName, method, null));
            return partials;
        }

        private static Map<String, PropertyDescriptor> _processWriteMethod(Map<String, PropertyDescriptor> partials, Method method, String propertyName, List<PropertyDescriptor> pds) throws IntrospectionException {
            if (partials == null) {
                partials = new HashMap();
            } else {
                PropertyDescriptor pd = (PropertyDescriptor) partials.get(propertyName);
                if (pd != null) {
                    pd.setWriteMethod(method);
                    if (pd.getReadMethod() != null) {
                        pds.add(pd);
                        partials.remove(propertyName);
                        return partials;
                    }
                }
            }
            partials.put(propertyName, new PropertyDescriptor(propertyName, null, method));
            return partials;
        }
    }

    public JaxbAnnotationIntrospector() {
        this._jaxbPackageName = XmlElement.class.getPackage().getName();
        JsonSerializer<?> dataHandlerSerializer = null;
        JsonDeserializer<?> dataHandlerDeserializer = null;
        try {
            dataHandlerSerializer = (JsonSerializer) Class.forName("org.codehaus.jackson.xc.DataHandlerJsonSerializer").newInstance();
            dataHandlerDeserializer = (JsonDeserializer) Class.forName("org.codehaus.jackson.xc.DataHandlerJsonDeserializer").newInstance();
        } catch (Throwable th) {
        }
        this._dataHandlerSerializer = dataHandlerSerializer;
        this._dataHandlerDeserializer = dataHandlerDeserializer;
    }

    public Version version() {
        return VersionUtil.versionFor(getClass());
    }

    public boolean isHandled(Annotation ann) {
        Class<?> cls = ann.annotationType();
        Package pkg = cls.getPackage();
        if ((pkg != null ? pkg.getName() : cls.getName()).startsWith(this._jaxbPackageName) || cls == JsonCachable.class) {
            return true;
        }
        return false;
    }

    public Boolean findCachability(AnnotatedClass ac) {
        JsonCachable ann = (JsonCachable) ac.getAnnotation(JsonCachable.class);
        if (ann != null) {
            return ann.value() ? Boolean.TRUE : Boolean.FALSE;
        } else {
            return null;
        }
    }

    public String findRootName(AnnotatedClass ac) {
        XmlRootElement elem = findRootElementAnnotation(ac);
        if (elem == null) {
            return null;
        }
        String name = elem.name();
        if (MARKER_FOR_DEFAULT.equals(name)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return name;
    }

    public String[] findPropertiesToIgnore(AnnotatedClass ac) {
        return null;
    }

    public Boolean findIgnoreUnknownProperties(AnnotatedClass ac) {
        return null;
    }

    public Boolean isIgnorableType(AnnotatedClass ac) {
        return null;
    }

    public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker) {
        XmlAccessType at = findAccessType(ac);
        if (at == null) {
            return checker;
        }
        switch (C09521.$SwitchMap$javax$xml$bind$annotation$XmlAccessType[at.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return checker.withFieldVisibility(Visibility.ANY).withSetterVisibility(Visibility.NONE).withGetterVisibility(Visibility.NONE).withIsGetterVisibility(Visibility.NONE);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return checker.withFieldVisibility(Visibility.NONE).withSetterVisibility(Visibility.NONE).withGetterVisibility(Visibility.NONE).withIsGetterVisibility(Visibility.NONE);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return checker.withFieldVisibility(Visibility.NONE).withSetterVisibility(Visibility.PUBLIC_ONLY).withGetterVisibility(Visibility.PUBLIC_ONLY).withIsGetterVisibility(Visibility.PUBLIC_ONLY);
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return checker.withFieldVisibility(Visibility.PUBLIC_ONLY).withSetterVisibility(Visibility.PUBLIC_ONLY).withGetterVisibility(Visibility.PUBLIC_ONLY).withIsGetterVisibility(Visibility.PUBLIC_ONLY);
            default:
                return checker;
        }
    }

    protected boolean isPropertiesAccessible(Annotated ac) {
        XmlAccessType accessType = findAccessType(ac);
        if (accessType == null) {
            accessType = XmlAccessType.PUBLIC_MEMBER;
        }
        return accessType == XmlAccessType.PUBLIC_MEMBER || accessType == XmlAccessType.PROPERTY;
    }

    protected XmlAccessType findAccessType(Annotated ac) {
        XmlAccessorType at = (XmlAccessorType) findAnnotation(XmlAccessorType.class, ac, true, true, true);
        return at == null ? null : at.value();
    }

    public TypeResolverBuilder<?> findTypeResolver(AnnotatedClass ac, JavaType baseType) {
        return null;
    }

    public TypeResolverBuilder<?> findPropertyTypeResolver(AnnotatedMember am, JavaType baseType) {
        if (baseType.isContainerType()) {
            return null;
        }
        return _typeResolverFromXmlElements(am);
    }

    public TypeResolverBuilder<?> findPropertyContentTypeResolver(AnnotatedMember am, JavaType containerType) {
        if (containerType.isContainerType()) {
            return _typeResolverFromXmlElements(am);
        }
        throw new IllegalArgumentException("Must call method with a container type (got " + containerType + ")");
    }

    protected TypeResolverBuilder<?> _typeResolverFromXmlElements(AnnotatedMember am) {
        XmlElementRefs elemRefs = (XmlElementRefs) findAnnotation(XmlElementRefs.class, am, false, false, false);
        if (((XmlElements) findAnnotation(XmlElements.class, am, false, false, false)) == null && elemRefs == null) {
            return null;
        }
        return new StdTypeResolverBuilder().init(Id.NAME, null).inclusion(As.WRAPPER_OBJECT);
    }

    public List<NamedType> findSubtypes(Annotated a) {
        XmlElements elems = (XmlElements) findAnnotation(XmlElements.class, a, false, false, false);
        List<NamedType> arrayList;
        String name;
        if (elems != null) {
            arrayList = new ArrayList();
            for (XmlElement elem : elems.value()) {
                name = elem.name();
                if (MARKER_FOR_DEFAULT.equals(name)) {
                    name = null;
                }
                arrayList.add(new NamedType(elem.type(), name));
            }
            return arrayList;
        }
        XmlElementRefs elemRefs = (XmlElementRefs) findAnnotation(XmlElementRefs.class, a, false, false, false);
        if (elemRefs == null) {
            return null;
        }
        arrayList = new ArrayList();
        for (XmlElementRef elemRef : elemRefs.value()) {
            Class<?> refType = elemRef.type();
            if (!JAXBElement.class.isAssignableFrom(refType)) {
                name = elemRef.name();
                if (name == null || MARKER_FOR_DEFAULT.equals(name)) {
                    XmlRootElement rootElement = (XmlRootElement) refType.getAnnotation(XmlRootElement.class);
                    if (rootElement != null) {
                        name = rootElement.name();
                    }
                }
                if (name == null || MARKER_FOR_DEFAULT.equals(name)) {
                    name = Introspector.decapitalize(refType.getSimpleName());
                }
                arrayList.add(new NamedType(refType, name));
            }
        }
        return arrayList;
    }

    public String findTypeName(AnnotatedClass ac) {
        XmlType type = (XmlType) findAnnotation(XmlType.class, ac, false, false, false);
        if (type != null) {
            String name = type.name();
            if (!MARKER_FOR_DEFAULT.equals(name)) {
                return name;
            }
        }
        return null;
    }

    public boolean isIgnorableMethod(AnnotatedMethod m) {
        return m.getAnnotation(XmlTransient.class) != null;
    }

    public boolean isIgnorableConstructor(AnnotatedConstructor c) {
        return false;
    }

    public boolean isIgnorableField(AnnotatedField f) {
        return f.getAnnotation(XmlTransient.class) != null;
    }

    public JsonSerializer<?> findSerializer(Annotated am, BeanProperty property) {
        XmlAdapter<Object, Object> adapter = findAdapter(am, true);
        if (adapter != null) {
            return new XmlAdapterJsonSerializer(adapter, property);
        }
        Class<?> type = am.getRawType();
        if (type == null || this._dataHandlerSerializer == null || !isDataHandler(type)) {
            return null;
        }
        return this._dataHandlerSerializer;
    }

    private boolean isDataHandler(Class<?> type) {
        return (type == null || Object.class.equals(type) || (!"javax.activation.DataHandler".equals(type.getName()) && !isDataHandler(type.getSuperclass()))) ? false : true;
    }

    public Class<?> findSerializationType(Annotated a) {
        XmlElement annotation = (XmlElement) findAnnotation(XmlElement.class, a, false, false, false);
        if (annotation == null || annotation.type() == DEFAULT.class) {
            return null;
        }
        if (isIndexedType(a.getRawType())) {
            return null;
        }
        return annotation.type();
    }

    public Inclusion findSerializationInclusion(Annotated a, Inclusion defValue) {
        XmlElementWrapper w = (XmlElementWrapper) a.getAnnotation(XmlElementWrapper.class);
        if (w != null) {
            return w.nillable() ? Inclusion.ALWAYS : Inclusion.NON_NULL;
        } else {
            XmlElement e = (XmlElement) a.getAnnotation(XmlElement.class);
            if (e != null) {
                return e.nillable() ? Inclusion.ALWAYS : Inclusion.NON_NULL;
            } else {
                return defValue;
            }
        }
    }

    public Typing findSerializationTyping(Annotated a) {
        return null;
    }

    public Class<?>[] findSerializationViews(Annotated a) {
        return null;
    }

    public String[] findSerializationPropertyOrder(AnnotatedClass ac) {
        XmlType type = (XmlType) findAnnotation(XmlType.class, ac, true, true, true);
        if (type == null) {
            return null;
        }
        String[] order = type.propOrder();
        if (order == null || order.length == 0) {
            return null;
        }
        PropertyDescriptors props = getDescriptors(ac.getRawType());
        int len = order.length;
        for (int i = 0; i < len; i++) {
            String propName = order[i];
            if (props.findByPropertyName(propName) == null && propName.length() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("get");
                sb.append(Character.toUpperCase(propName.charAt(0)));
                if (propName.length() > 1) {
                    sb.append(propName.substring(1));
                }
                PropertyDescriptor desc = props.findByMethodName(sb.toString());
                if (desc != null) {
                    order[i] = desc.getName();
                }
            }
        }
        return order;
    }

    public Boolean findSerializationSortAlphabetically(AnnotatedClass ac) {
        boolean z = true;
        XmlAccessorOrder order = (XmlAccessorOrder) findAnnotation(XmlAccessorOrder.class, ac, true, true, true);
        if (order == null) {
            return null;
        }
        if (order.value() != XmlAccessOrder.ALPHABETICAL) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public String findGettablePropertyName(AnnotatedMethod am) {
        PropertyDescriptor desc = findPropertyDescriptor(am);
        if (desc != null) {
            return findJaxbSpecifiedPropertyName(desc);
        }
        return null;
    }

    public boolean hasAsValueAnnotation(AnnotatedMethod am) {
        return false;
    }

    public String findEnumValue(Enum<?> e) {
        Class<?> enumClass = e.getDeclaringClass();
        String enumValue = e.name();
        try {
            XmlEnumValue xmlEnumValue = (XmlEnumValue) enumClass.getDeclaredField(enumValue).getAnnotation(XmlEnumValue.class);
            if (xmlEnumValue != null) {
                enumValue = xmlEnumValue.value();
            }
            return enumValue;
        } catch (NoSuchFieldException e1) {
            throw new IllegalStateException("Could not locate Enum entry '" + enumValue + "' (Enum class " + enumClass.getName() + ")", e1);
        }
    }

    public String findSerializablePropertyName(AnnotatedField af) {
        if (isInvisible(af)) {
            return null;
        }
        Field field = af.getAnnotated();
        String name = findJaxbPropertyName(field, field.getType(), XmlPullParser.NO_NAMESPACE);
        return name == null ? field.getName() : name;
    }

    public JsonDeserializer<?> findDeserializer(Annotated am, BeanProperty property) {
        XmlAdapter<Object, Object> adapter = findAdapter(am, false);
        if (adapter != null) {
            return new XmlAdapterJsonDeserializer(adapter, property);
        }
        Class<?> type = am.getRawType();
        if (type == null || this._dataHandlerDeserializer == null || !isDataHandler(type)) {
            return null;
        }
        return this._dataHandlerDeserializer;
    }

    public Class<KeyDeserializer> findKeyDeserializer(Annotated am) {
        return null;
    }

    public Class<JsonDeserializer<?>> findContentDeserializer(Annotated am) {
        return null;
    }

    public Class<?> findDeserializationType(Annotated a, JavaType baseType, String propName) {
        if (baseType.isContainerType()) {
            return null;
        }
        return _doFindDeserializationType(a, baseType, propName);
    }

    public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName) {
        return null;
    }

    public Class<?> findDeserializationContentType(Annotated a, JavaType baseContentType, String propName) {
        return _doFindDeserializationType(a, baseContentType, propName);
    }

    protected Class<?> _doFindDeserializationType(Annotated a, JavaType baseType, String propName) {
        if (a.hasAnnotation(XmlJavaTypeAdapter.class)) {
            return null;
        }
        XmlElement annotation = (XmlElement) findAnnotation(XmlElement.class, a, false, false, false);
        if (annotation != null) {
            Class<?> type = annotation.type();
            if (type != DEFAULT.class) {
                return type;
            }
        }
        if ((a instanceof AnnotatedMethod) && propName != null) {
            annotation = (XmlElement) findFieldAnnotation(XmlElement.class, ((AnnotatedMethod) a).getDeclaringClass(), propName);
            if (!(annotation == null || annotation.type() == DEFAULT.class)) {
                return annotation.type();
            }
        }
        return null;
    }

    public String findSettablePropertyName(AnnotatedMethod am) {
        PropertyDescriptor desc = findPropertyDescriptor(am);
        if (desc != null) {
            return findJaxbSpecifiedPropertyName(desc);
        }
        return null;
    }

    public boolean hasAnySetterAnnotation(AnnotatedMethod am) {
        return false;
    }

    public boolean hasCreatorAnnotation(Annotated am) {
        return false;
    }

    public String findDeserializablePropertyName(AnnotatedField af) {
        if (isInvisible(af)) {
            return null;
        }
        Field field = af.getAnnotated();
        String name = findJaxbPropertyName(field, field.getType(), XmlPullParser.NO_NAMESPACE);
        return name == null ? field.getName() : name;
    }

    public String findPropertyNameForParam(AnnotatedParameter param) {
        return null;
    }

    protected boolean isInvisible(AnnotatedField f) {
        boolean invisible = true;
        for (Annotation annotation : f.getAnnotated().getDeclaredAnnotations()) {
            if (isHandled(annotation)) {
                invisible = false;
            }
        }
        if (!invisible) {
            return invisible;
        }
        XmlAccessType accessType = XmlAccessType.PUBLIC_MEMBER;
        XmlAccessorType at = (XmlAccessorType) findAnnotation(XmlAccessorType.class, f, true, true, true);
        if (at != null) {
            accessType = at.value();
        }
        return (accessType == XmlAccessType.FIELD || (accessType == XmlAccessType.PUBLIC_MEMBER && Modifier.isPublic(f.getAnnotated().getModifiers()))) ? false : true;
    }

    protected <A extends Annotation> A findAnnotation(Class<A> annotationClass, Annotated annotated, boolean includePackage, boolean includeClass, boolean includeSuperclasses) {
        A annotation;
        Class memberClass;
        if (annotated instanceof AnnotatedMethod) {
            PropertyDescriptor pd = findPropertyDescriptor((AnnotatedMethod) annotated);
            if (pd != null) {
                annotation = new AnnotatedProperty(null).getAnnotation(annotationClass);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        AnnotatedElement annType = annotated.getAnnotated();
        if (annotated instanceof AnnotatedParameter) {
            AnnotatedParameter param = (AnnotatedParameter) annotated;
            annotation = param.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
            memberClass = param.getMember().getDeclaringClass();
        } else {
            annotation = annType.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
            if (annType instanceof Member) {
                memberClass = ((Member) annType).getDeclaringClass();
                if (includeClass) {
                    annotation = memberClass.getAnnotation(annotationClass);
                    if (annotation != null) {
                        return annotation;
                    }
                }
            } else if (annType instanceof Class) {
                memberClass = (Class) annType;
            } else {
                throw new IllegalStateException("Unsupported annotated member: " + annotated.getClass().getName());
            }
        }
        if (memberClass != null) {
            if (includeSuperclasses) {
                Class superclass = memberClass.getSuperclass();
                while (superclass != null && superclass != Object.class) {
                    annotation = superclass.getAnnotation(annotationClass);
                    if (annotation != null) {
                        return annotation;
                    }
                    superclass = superclass.getSuperclass();
                }
            }
            if (includePackage) {
                return memberClass.getPackage().getAnnotation(annotationClass);
            }
        }
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected <A extends java.lang.annotation.Annotation> A findFieldAnnotation(java.lang.Class<A> r6, java.lang.Class<?> r7, java.lang.String r8) {
        /*
        r5 = this;
    L_0x0000:
        r0 = r7.getDeclaredFields();
        r3 = r0.length;
        r2 = 0;
    L_0x0006:
        if (r2 >= r3) goto L_0x001c;
    L_0x0008:
        r1 = r0[r2];
        r4 = r1.getName();
        r4 = r8.equals(r4);
        if (r4 == 0) goto L_0x0019;
    L_0x0014:
        r4 = r1.getAnnotation(r6);
    L_0x0018:
        return r4;
    L_0x0019:
        r2 = r2 + 1;
        goto L_0x0006;
    L_0x001c:
        r4 = r7.isInterface();
        if (r4 != 0) goto L_0x0026;
    L_0x0022:
        r4 = java.lang.Object.class;
        if (r7 != r4) goto L_0x0028;
    L_0x0026:
        r4 = 0;
        goto L_0x0018;
    L_0x0028:
        r7 = r7.getSuperclass();
        if (r7 != 0) goto L_0x0000;
    L_0x002e:
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.codehaus.jackson.xc.JaxbAnnotationIntrospector.findFieldAnnotation(java.lang.Class, java.lang.Class, java.lang.String):A");
    }

    static {
        _propertyDescriptors = new ThreadLocal();
    }

    protected PropertyDescriptors getDescriptors(Class<?> forClass) {
        SoftReference<PropertyDescriptors> ref = (SoftReference) _propertyDescriptors.get();
        PropertyDescriptors descriptors = ref == null ? null : (PropertyDescriptors) ref.get();
        if (descriptors == null || descriptors.getBeanClass() != forClass) {
            try {
                descriptors = PropertyDescriptors.find(forClass);
                _propertyDescriptors.set(new SoftReference(descriptors));
            } catch (IntrospectionException e) {
                throw new IllegalArgumentException("Problem introspecting bean properties: " + e.getMessage(), e);
            }
        }
        return descriptors;
    }

    protected PropertyDescriptor findPropertyDescriptor(AnnotatedMethod m) {
        return getDescriptors(m.getDeclaringClass()).findByMethodName(m.getName());
    }

    protected String findJaxbSpecifiedPropertyName(PropertyDescriptor prop) {
        return findJaxbPropertyName(new AnnotatedProperty(null), prop.getPropertyType(), prop.getName());
    }

    protected static String findJaxbPropertyName(AnnotatedElement ae, Class<?> aeType, String defaultName) {
        XmlElementWrapper elementWrapper = (XmlElementWrapper) ae.getAnnotation(XmlElementWrapper.class);
        String name;
        if (elementWrapper != null) {
            name = elementWrapper.name();
            return !MARKER_FOR_DEFAULT.equals(name) ? name : defaultName;
        } else {
            XmlAttribute attribute = (XmlAttribute) ae.getAnnotation(XmlAttribute.class);
            if (attribute != null) {
                name = attribute.name();
                if (MARKER_FOR_DEFAULT.equals(name)) {
                    return defaultName;
                }
                return name;
            }
            XmlElement element = (XmlElement) ae.getAnnotation(XmlElement.class);
            if (element != null) {
                name = element.name();
                if (MARKER_FOR_DEFAULT.equals(name)) {
                    return defaultName;
                }
                return name;
            }
            XmlElementRef elementRef = (XmlElementRef) ae.getAnnotation(XmlElementRef.class);
            if (elementRef != null) {
                name = elementRef.name();
                if (!MARKER_FOR_DEFAULT.equals(name)) {
                    return name;
                }
                if (aeType != null) {
                    XmlRootElement rootElement = (XmlRootElement) aeType.getAnnotation(XmlRootElement.class);
                    if (rootElement != null) {
                        name = rootElement.name();
                        if (MARKER_FOR_DEFAULT.equals(name)) {
                            return Introspector.decapitalize(aeType.getSimpleName());
                        }
                        return name;
                    }
                }
            }
            if (((XmlValue) ae.getAnnotation(XmlValue.class)) != null) {
                return SharedPref.VALUE;
            }
            return null;
        }
    }

    private XmlRootElement findRootElementAnnotation(AnnotatedClass ac) {
        return (XmlRootElement) findAnnotation(XmlRootElement.class, ac, true, false, true);
    }

    protected XmlAdapter<Object, Object> findAdapter(Annotated am, boolean forSerialization) {
        if (am instanceof AnnotatedClass) {
            return findAdapterForClass((AnnotatedClass) am, forSerialization);
        }
        XmlJavaTypeAdapter adapterInfo;
        XmlAdapter<Object, Object> adapter;
        Class<?> memberType = am.getRawType();
        if (memberType == Void.TYPE && (am instanceof AnnotatedMethod)) {
            memberType = ((AnnotatedMethod) am).getParameterClass(0);
        }
        Member member = (Member) am.getAnnotated();
        if (member != null) {
            Class<?> potentialAdaptee = member.getDeclaringClass();
            if (potentialAdaptee != null) {
                adapterInfo = (XmlJavaTypeAdapter) potentialAdaptee.getAnnotation(XmlJavaTypeAdapter.class);
                if (adapterInfo != null) {
                    adapter = checkAdapter(adapterInfo, memberType);
                    if (adapter != null) {
                        return adapter;
                    }
                }
            }
        }
        adapterInfo = (XmlJavaTypeAdapter) findAnnotation(XmlJavaTypeAdapter.class, am, true, false, false);
        if (adapterInfo != null) {
            adapter = checkAdapter(adapterInfo, memberType);
            if (adapter != null) {
                return adapter;
            }
        }
        XmlJavaTypeAdapters adapters = (XmlJavaTypeAdapters) findAnnotation(XmlJavaTypeAdapters.class, am, true, false, false);
        if (adapters != null) {
            for (XmlJavaTypeAdapter info : adapters.value()) {
                adapter = checkAdapter(info, memberType);
                if (adapter != null) {
                    return adapter;
                }
            }
        }
        return null;
    }

    private final XmlAdapter<Object, Object> checkAdapter(XmlJavaTypeAdapter adapterInfo, Class<?> typeNeeded) {
        Class<?> adaptedType = adapterInfo.type();
        if (adaptedType == XmlJavaTypeAdapter.DEFAULT.class || adaptedType.isAssignableFrom(typeNeeded)) {
            return (XmlAdapter) ClassUtil.createInstance(adapterInfo.value(), false);
        }
        return null;
    }

    protected XmlAdapter<Object, Object> findAdapterForClass(AnnotatedClass ac, boolean forSerialization) {
        XmlJavaTypeAdapter adapterInfo = (XmlJavaTypeAdapter) ac.getAnnotated().getAnnotation(XmlJavaTypeAdapter.class);
        if (adapterInfo != null) {
            return (XmlAdapter) ClassUtil.createInstance(adapterInfo.value(), false);
        }
        return null;
    }

    protected boolean isIndexedType(Class<?> raw) {
        return raw.isArray() || Collection.class.isAssignableFrom(raw) || Map.class.isAssignableFrom(raw);
    }
}
