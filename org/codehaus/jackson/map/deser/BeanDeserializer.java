package org.codehaus.jackson.map.deser;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.BeanProperty.Std;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.DeserializerProvider;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ResolvableDeserializer;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.deser.SettableBeanProperty.ManagedReferenceProperty;
import org.codehaus.jackson.map.deser.impl.BeanPropertyMap;
import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.util.TokenBuffer;

@JsonCachable
public class BeanDeserializer extends StdDeserializer<Object> implements ResolvableDeserializer {
    protected final SettableAnyProperty _anySetter;
    protected final Map<String, SettableBeanProperty> _backRefs;
    protected final BeanPropertyMap _beanProperties;
    protected final JavaType _beanType;
    protected final Constructor<?> _defaultConstructor;
    protected final Delegating _delegatingCreator;
    protected final AnnotatedClass _forClass;
    protected final HashSet<String> _ignorableProps;
    protected final boolean _ignoreAllUnknown;
    protected final NumberBased _numberCreator;
    protected final BeanProperty _property;
    protected final PropertyBased _propertyBasedCreator;
    protected final StringBased _stringCreator;
    protected HashMap<ClassKey, JsonDeserializer<Object>> _subDeserializers;

    /* renamed from: org.codehaus.jackson.map.deser.BeanDeserializer.1 */
    static /* synthetic */ class C09411 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonParser$NumberType;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$JsonToken;

        static {
            $SwitchMap$org$codehaus$jackson$JsonParser$NumberType = new int[NumberType.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonParser$NumberType[NumberType.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$org$codehaus$jackson$JsonToken = new int[JsonToken.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_STRING.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_INT.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_NUMBER_FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_EMBEDDED_OBJECT.ordinal()] = 4;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_TRUE.ordinal()] = 5;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.VALUE_FALSE.ordinal()] = 6;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.START_ARRAY.ordinal()] = 7;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.FIELD_NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$JsonToken[JsonToken.END_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    public BeanDeserializer(AnnotatedClass forClass, JavaType type, BeanProperty property, CreatorContainer creators, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, HashSet<String> ignorableProps, boolean ignoreAllUnknown, SettableAnyProperty anySetter) {
        super(type);
        this._forClass = forClass;
        this._beanType = type;
        this._property = property;
        this._beanProperties = properties;
        this._backRefs = backRefs;
        this._ignorableProps = ignorableProps;
        this._ignoreAllUnknown = ignoreAllUnknown;
        this._anySetter = anySetter;
        this._stringCreator = creators.stringCreator();
        this._numberCreator = creators.numberCreator();
        this._delegatingCreator = creators.delegatingCreator();
        this._propertyBasedCreator = creators.propertyBasedCreator();
        if (this._delegatingCreator == null && this._propertyBasedCreator == null) {
            this._defaultConstructor = creators.getDefaultConstructor();
        } else {
            this._defaultConstructor = null;
        }
    }

    protected BeanDeserializer(BeanDeserializer src) {
        super(src._beanType);
        this._forClass = src._forClass;
        this._beanType = src._beanType;
        this._property = src._property;
        this._beanProperties = src._beanProperties;
        this._backRefs = src._backRefs;
        this._ignorableProps = src._ignorableProps;
        this._ignoreAllUnknown = src._ignoreAllUnknown;
        this._anySetter = src._anySetter;
        this._defaultConstructor = src._defaultConstructor;
        this._stringCreator = src._stringCreator;
        this._numberCreator = src._numberCreator;
        this._delegatingCreator = src._delegatingCreator;
        this._propertyBasedCreator = src._propertyBasedCreator;
    }

    public boolean hasProperty(String propertyName) {
        return this._beanProperties.find(propertyName) != null;
    }

    public int getPropertyCount() {
        return this._beanProperties.size();
    }

    public void resolve(DeserializationConfig config, DeserializerProvider provider) throws JsonMappingException {
        Iterator<SettableBeanProperty> it = this._beanProperties.allProperties();
        while (it.hasNext()) {
            SettableBeanProperty prop = (SettableBeanProperty) it.next();
            if (!prop.hasValueDeserializer()) {
                prop.setValueDeserializer(findDeserializer(config, provider, prop.getType(), prop));
            }
            String refName = prop.getManagedReferenceName();
            if (refName != null) {
                SettableBeanProperty backProp;
                String str;
                JsonDeserializer<?> valueDeser = prop._valueDeserializer;
                boolean isContainer = false;
                if (valueDeser instanceof BeanDeserializer) {
                    backProp = ((BeanDeserializer) valueDeser).findBackReference(refName);
                } else if (valueDeser instanceof ContainerDeserializer) {
                    JsonDeserializer<?> contentDeser = ((ContainerDeserializer) valueDeser).getContentDeserializer();
                    if (contentDeser instanceof BeanDeserializer) {
                        backProp = ((BeanDeserializer) contentDeser).findBackReference(refName);
                        isContainer = true;
                    } else {
                        str = "Can not handle managed/back reference '";
                        str = "': value deserializer is of type ContainerDeserializer, but content type is not handled by a BeanDeserializer ";
                        str = " (instead it's of type ";
                        throw new IllegalArgumentException(r18 + refName + r18 + r18 + contentDeser.getClass().getName() + ")");
                    }
                } else if (valueDeser instanceof AbstractDeserializer) {
                    str = "Can not handle managed/back reference for abstract types (property ";
                    str = this._beanType.getRawClass().getName();
                    str = ".";
                    throw new IllegalArgumentException(r18 + r18 + r18 + prop.getName() + ")");
                } else {
                    str = "Can not handle managed/back reference '";
                    str = "': type for value deserializer is not BeanDeserializer or ContainerDeserializer, but ";
                    throw new IllegalArgumentException(r18 + refName + r18 + valueDeser.getClass().getName());
                }
                if (backProp == null) {
                    str = "Can not handle managed/back reference '";
                    str = "': no back reference property found from type ";
                    throw new IllegalArgumentException(r18 + refName + r18 + prop.getType());
                }
                JavaType referredType = this._beanType;
                JavaType backRefType = backProp.getType();
                if (backRefType.getRawClass().isAssignableFrom(referredType.getRawClass())) {
                    BeanPropertyMap beanPropertyMap = this._beanProperties;
                    r18.replace(new ManagedReferenceProperty(refName, prop, backProp, this._forClass.getAnnotations(), isContainer));
                } else {
                    str = "Can not handle managed/back reference '";
                    str = "': back reference type (";
                    str = backRefType.getRawClass().getName();
                    str = ") not compatible with managed type (";
                    throw new IllegalArgumentException(r18 + refName + r18 + r18 + r18 + referredType.getRawClass().getName() + ")");
                }
            }
        }
        if (!(this._anySetter == null || this._anySetter.hasValueDeserializer())) {
            this._anySetter.setValueDeserializer(findDeserializer(config, provider, this._anySetter.getType(), this._anySetter.getProperty()));
        }
        if (this._delegatingCreator != null) {
            DeserializationConfig deserializationConfig = config;
            DeserializerProvider deserializerProvider = provider;
            this._delegatingCreator.setDeserializer(findDeserializer(deserializationConfig, deserializerProvider, this._delegatingCreator.getValueType(), new Std(null, this._delegatingCreator.getValueType(), this._forClass.getAnnotations(), this._delegatingCreator.getCreator())));
        }
        if (this._propertyBasedCreator != null) {
            for (SettableBeanProperty prop2 : this._propertyBasedCreator.properties()) {
                if (!prop2.hasValueDeserializer()) {
                    prop2.setValueDeserializer(findDeserializer(config, provider, prop2.getType(), prop2));
                }
            }
        }
    }

    public final Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            jp.nextToken();
            return deserializeFromObject(jp, ctxt);
        }
        switch (C09411.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return deserializeFromString(jp, ctxt);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return deserializeFromNumber(jp, ctxt);
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return jp.getEmbeddedObject();
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return deserializeUsingCreator(jp, ctxt);
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return deserializeFromObject(jp, ctxt);
            default:
                throw ctxt.mappingException(getBeanClass());
        }
    }

    public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException, JsonProcessingException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.START_OBJECT) {
            t = jp.nextToken();
        }
        while (t == JsonToken.FIELD_NAME) {
            String propName = jp.getCurrentName();
            SettableBeanProperty prop = this._beanProperties.find(propName);
            jp.nextToken();
            if (prop != null) {
                try {
                    prop.deserializeAndSet(jp, ctxt, bean);
                } catch (Throwable e) {
                    wrapAndThrow(e, bean, propName, ctxt);
                }
            } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                jp.skipChildren();
            } else if (this._anySetter != null) {
                this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
            } else {
                handleUnknownProperty(jp, ctxt, bean, propName);
            }
            t = jp.nextToken();
        }
        return bean;
    }

    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
    }

    public final Class<?> getBeanClass() {
        return this._beanType.getRawClass();
    }

    public JavaType getValueType() {
        return this._beanType;
    }

    public Iterator<SettableBeanProperty> properties() {
        if (this._beanProperties != null) {
            return this._beanProperties.allProperties();
        }
        throw new IllegalStateException("Can only call before BeanDeserializer has been resolved");
    }

    public SettableBeanProperty findBackReference(String logicalName) {
        if (this._backRefs == null) {
            return null;
        }
        return (SettableBeanProperty) this._backRefs.get(logicalName);
    }

    public Object deserializeFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._defaultConstructor != null) {
            Object bean = constructDefaultInstance();
            while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
                String propName = jp.getCurrentName();
                jp.nextToken();
                SettableBeanProperty prop = this._beanProperties.find(propName);
                if (prop != null) {
                    try {
                        prop.deserializeAndSet(jp, ctxt, bean);
                    } catch (Throwable e) {
                        wrapAndThrow(e, bean, propName, ctxt);
                    }
                } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                    jp.skipChildren();
                } else if (this._anySetter != null) {
                    try {
                        this._anySetter.deserializeAndSet(jp, ctxt, bean, propName);
                    } catch (Throwable e2) {
                        wrapAndThrow(e2, bean, propName, ctxt);
                    }
                } else {
                    handleUnknownProperty(jp, ctxt, bean, propName);
                }
                jp.nextToken();
            }
            return bean;
        } else if (this._propertyBasedCreator != null) {
            return _deserializeUsingPropertyBased(jp, ctxt);
        } else {
            if (this._delegatingCreator != null) {
                return this._delegatingCreator.deserialize(jp, ctxt);
            }
            if (this._beanType.isAbstract()) {
                throw JsonMappingException.from(jp, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
            }
            throw JsonMappingException.from(jp, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (need to add/enable type information?)");
        }
    }

    public Object deserializeFromString(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._stringCreator != null) {
            return this._stringCreator.construct(jp.getText());
        }
        if (this._delegatingCreator != null) {
            return this._delegatingCreator.deserialize(jp, ctxt);
        }
        throw ctxt.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON String");
    }

    public Object deserializeFromNumber(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._numberCreator != null) {
            switch (C09411.$SwitchMap$org$codehaus$jackson$JsonParser$NumberType[jp.getNumberType().ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    return this._numberCreator.construct(jp.getIntValue());
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return this._numberCreator.construct(jp.getLongValue());
            }
        }
        if (this._delegatingCreator != null) {
            return this._delegatingCreator.deserialize(jp, ctxt);
        }
        throw ctxt.instantiationException(getBeanClass(), "no suitable creator method found to deserialize from JSON Number");
    }

    public Object deserializeUsingCreator(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (this._delegatingCreator != null) {
            try {
                return this._delegatingCreator.deserialize(jp, ctxt);
            } catch (Exception e) {
                wrapInstantiationProblem(e, ctxt);
            }
        }
        throw ctxt.mappingException(getBeanClass());
    }

    protected final Object _deserializeUsingPropertyBased(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        PropertyBased creator = this._propertyBasedCreator;
        PropertyValueBuffer buffer = creator.startBuilding(jp, ctxt);
        TokenBuffer unknown = null;
        JsonToken t = jp.getCurrentToken();
        while (t == JsonToken.FIELD_NAME) {
            Object bean;
            String propName = jp.getCurrentName();
            jp.nextToken();
            SettableBeanProperty prop = creator.findCreatorProperty(propName);
            if (prop != null) {
                if (buffer.assignParameter(prop.getCreatorIndex(), prop.deserialize(jp, ctxt))) {
                    jp.nextToken();
                    try {
                        bean = creator.build(buffer);
                        if (bean.getClass() != this._beanType.getRawClass()) {
                            return handlePolymorphic(jp, ctxt, bean, unknown);
                        }
                        if (unknown != null) {
                            bean = handleUnknownProperties(ctxt, bean, unknown);
                        }
                        return deserialize(jp, ctxt, bean);
                    } catch (Throwable e) {
                        wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
                    }
                } else {
                    continue;
                }
            } else {
                prop = this._beanProperties.find(propName);
                if (prop != null) {
                    buffer.bufferProperty(prop, prop.deserialize(jp, ctxt));
                } else if (this._ignorableProps != null && this._ignorableProps.contains(propName)) {
                    jp.skipChildren();
                } else if (this._anySetter != null) {
                    buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(jp, ctxt));
                } else {
                    if (unknown == null) {
                        unknown = new TokenBuffer(jp.getCodec());
                    }
                    unknown.writeFieldName(propName);
                    unknown.copyCurrentStructure(jp);
                }
            }
            t = jp.nextToken();
        }
        try {
            bean = creator.build(buffer);
            if (unknown == null) {
                return bean;
            }
            if (bean.getClass() != this._beanType.getRawClass()) {
                return handlePolymorphic(null, ctxt, bean, unknown);
            }
            return handleUnknownProperties(ctxt, bean, unknown);
        } catch (Exception e2) {
            wrapInstantiationProblem(e2, ctxt);
            return null;
        }
    }

    protected void handleUnknownProperty(JsonParser jp, DeserializationContext ctxt, Object beanOrClass, String propName) throws IOException, JsonProcessingException {
        if (this._ignoreAllUnknown || (this._ignorableProps != null && this._ignorableProps.contains(propName))) {
            jp.skipChildren();
        } else {
            super.handleUnknownProperty(jp, ctxt, beanOrClass, propName);
        }
    }

    protected Object handleUnknownProperties(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        unknownTokens.writeEndObject();
        JsonParser bufferParser = unknownTokens.asParser();
        while (bufferParser.nextToken() != JsonToken.END_OBJECT) {
            String propName = bufferParser.getCurrentName();
            bufferParser.nextToken();
            handleUnknownProperty(bufferParser, ctxt, bean, propName);
        }
        return bean;
    }

    protected Object handlePolymorphic(JsonParser jp, DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        JsonDeserializer<Object> subDeser = _findSubclassDeserializer(ctxt, bean, unknownTokens);
        if (subDeser != null) {
            if (unknownTokens != null) {
                unknownTokens.writeEndObject();
                JsonParser p2 = unknownTokens.asParser();
                p2.nextToken();
                bean = subDeser.deserialize(p2, ctxt, bean);
            }
            if (jp != null) {
                bean = subDeser.deserialize(jp, ctxt, bean);
            }
            return bean;
        }
        if (unknownTokens != null) {
            bean = handleUnknownProperties(ctxt, bean, unknownTokens);
        }
        if (jp != null) {
            bean = deserialize(jp, ctxt, bean);
        }
        return bean;
    }

    protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens) throws IOException, JsonProcessingException {
        synchronized (this) {
            JsonDeserializer<Object> subDeser = this._subDeserializers == null ? null : (JsonDeserializer) this._subDeserializers.get(new ClassKey(bean.getClass()));
        }
        if (subDeser != null) {
            return subDeser;
        }
        DeserializerProvider deserProv = ctxt.getDeserializerProvider();
        if (deserProv != null) {
            subDeser = deserProv.findValueDeserializer(ctxt.getConfig(), TypeFactory.type(bean.getClass()), this._property);
            if (subDeser != null) {
                synchronized (this) {
                    if (this._subDeserializers == null) {
                        this._subDeserializers = new HashMap();
                    }
                    this._subDeserializers.put(new ClassKey(bean.getClass()), subDeser);
                }
            }
        }
        return subDeser;
    }

    protected Object constructDefaultInstance() {
        try {
            return this._defaultConstructor.newInstance(new Object[0]);
        } catch (Exception e) {
            ClassUtil.unwrapAndThrowAsIAE(e);
            return null;
        }
    }

    public void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt) throws IOException {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            if (!(wrap && (t instanceof JsonMappingException))) {
                throw ((IOException) t);
            }
        } else if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        throw JsonMappingException.wrapWithPath(t, bean, fieldName);
    }

    public void wrapAndThrow(Throwable t, Object bean, int index, DeserializationContext ctxt) throws IOException {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            if (!(wrap && (t instanceof JsonMappingException))) {
                throw ((IOException) t);
            }
        } else if (!wrap && (t instanceof RuntimeException)) {
            throw ((RuntimeException) t);
        }
        throw JsonMappingException.wrapWithPath(t, bean, index);
    }

    protected void wrapInstantiationProblem(Throwable t, DeserializationContext ctxt) throws IOException {
        while ((t instanceof InvocationTargetException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof Error) {
            throw ((Error) t);
        }
        boolean wrap = ctxt == null || ctxt.isEnabled(Feature.WRAP_EXCEPTIONS);
        if (t instanceof IOException) {
            throw ((IOException) t);
        } else if (wrap || !(t instanceof RuntimeException)) {
            throw JsonMappingException.from(ctxt.getParser(), "Can not construct instance of " + this._beanType.getRawClass().getName() + ", problem: " + t.getMessage(), t);
        } else {
            throw ((RuntimeException) t);
        }
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, String fieldName) throws IOException {
        wrapAndThrow(t, bean, fieldName, null);
    }

    @Deprecated
    public void wrapAndThrow(Throwable t, Object bean, int index) throws IOException {
        wrapAndThrow(t, bean, index, null);
    }
}
