package org.codehaus.jackson.map.ser;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
import org.codehaus.jackson.map.introspect.Annotated;
import org.codehaus.jackson.map.introspect.AnnotatedField;
import org.codehaus.jackson.map.introspect.AnnotatedMember;
import org.codehaus.jackson.map.introspect.AnnotatedMethod;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.Annotations;
import org.codehaus.jackson.type.JavaType;

public class PropertyBuilder {
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final BasicBeanDescription _beanDesc;
    protected final SerializationConfig _config;
    protected Object _defaultBean;
    protected final Inclusion _outputProps;

    /* renamed from: org.codehaus.jackson.map.ser.PropertyBuilder.1 */
    static /* synthetic */ class C09471 {
        static final /* synthetic */ int[] f1672x1c046cf1;

        static {
            f1672x1c046cf1 = new int[Inclusion.values().length];
            try {
                f1672x1c046cf1[Inclusion.NON_DEFAULT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1672x1c046cf1[Inclusion.NON_NULL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public PropertyBuilder(SerializationConfig config, BasicBeanDescription beanDesc) {
        this._config = config;
        this._beanDesc = beanDesc;
        this._outputProps = beanDesc.findSerializationInclusion(config.getSerializationInclusion());
        this._annotationIntrospector = this._config.getAnnotationIntrospector();
    }

    public Annotations getClassAnnotations() {
        return this._beanDesc.getClassAnnotations();
    }

    protected BeanPropertyWriter buildWriter(String name, JavaType declaredType, JsonSerializer<Object> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping) {
        Method m;
        Field f;
        if (am instanceof AnnotatedField) {
            m = null;
            f = ((AnnotatedField) am).getAnnotated();
        } else {
            m = ((AnnotatedMethod) am).getAnnotated();
            f = null;
        }
        JavaType serializationType = findSerializationType(am, defaultUseStaticTyping);
        if (contentTypeSer != null) {
            if (serializationType == null) {
                serializationType = declaredType;
            }
            if (serializationType.getContentType() == null) {
                throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '" + name + "' (of type " + this._beanDesc.getType() + "); serialization type " + serializationType + " has no content");
            }
            serializationType = serializationType.withContentTypeHandler(contentTypeSer);
            serializationType.getContentType();
        }
        Object suppValue = null;
        boolean suppressNulls = false;
        Inclusion methodProps = this._annotationIntrospector.findSerializationInclusion(am, this._outputProps);
        if (methodProps != null) {
            switch (C09471.f1672x1c046cf1[methodProps.ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    suppValue = getDefaultValue(name, m, f);
                    if (suppValue == null) {
                        suppressNulls = true;
                        break;
                    }
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    suppressNulls = true;
                    break;
            }
        }
        return new BeanPropertyWriter(am, this._beanDesc.getClassAnnotations(), name, declaredType, (JsonSerializer) ser, typeSer, serializationType, m, f, suppressNulls, suppValue);
    }

    protected JavaType findSerializationType(Annotated a, boolean useStaticTyping) {
        Type serializationType = this._annotationIntrospector.findSerializationType(a);
        if (serializationType != null) {
            Class<?> raw = a.getRawType();
            if (serializationType.isAssignableFrom(raw)) {
                return TypeFactory.type(serializationType);
            }
            if (raw.isAssignableFrom(serializationType)) {
                return TypeFactory.type(serializationType);
            }
            throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + a.getName() + "': class " + serializationType.getName() + " not a super-type of (declared) class " + raw.getName());
        }
        Typing typing = this._annotationIntrospector.findSerializationTyping(a);
        if (typing != null) {
            useStaticTyping = typing == Typing.STATIC;
        }
        if (useStaticTyping) {
            return TypeFactory.type(a.getGenericType(), this._beanDesc.getType());
        }
        return null;
    }

    protected Object getDefaultBean() {
        if (this._defaultBean == null) {
            this._defaultBean = this._beanDesc.instantiateBean(this._config.isEnabled(Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
            if (this._defaultBean == null) {
                throw new IllegalArgumentException("Class " + this._beanDesc.getClassInfo().getAnnotated().getName() + " has no default constructor; can not instantiate default bean value to support 'properties=JsonSerialize.Inclusion.NON_DEFAULT' annotation");
            }
        }
        return this._defaultBean;
    }

    protected Object getDefaultValue(String name, Method m, Field f) {
        Object defaultBean = getDefaultBean();
        if (m == null) {
            return f.get(defaultBean);
        }
        try {
            return m.invoke(defaultBean, new Object[0]);
        } catch (Exception e) {
            return _throwWrapped(e, name, defaultBean);
        }
    }

    protected Object _throwWrapped(Exception e, String propName, Object defaultBean) {
        Throwable t = e;
        while (t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        } else if (t instanceof RuntimeException) {
            throw ((RuntimeException) t);
        } else {
            throw new IllegalArgumentException("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance");
        }
    }
}
