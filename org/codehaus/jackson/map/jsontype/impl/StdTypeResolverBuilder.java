package org.codehaus.jackson.map.jsontype.impl;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.util.Collection;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.jsontype.TypeIdResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.type.JavaType;

public class StdTypeResolverBuilder implements TypeResolverBuilder<StdTypeResolverBuilder> {
    protected TypeIdResolver _customIdResolver;
    protected Id _idType;
    protected As _includeAs;
    protected String _typeProperty;

    /* renamed from: org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder.1 */
    static /* synthetic */ class C09451 {
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As;
        static final /* synthetic */ int[] $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id;

        static {
            $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id = new int[Id.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.CLASS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.MINIMAL_CLASS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.NAME.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.CUSTOM.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[Id.NONE.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As = new int[As.values().length];
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[As.WRAPPER_ARRAY.ordinal()] = 1;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[As.PROPERTY.ordinal()] = 2;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[As.WRAPPER_OBJECT.ordinal()] = 3;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public StdTypeResolverBuilder init(Id idType, TypeIdResolver idRes) {
        if (idType == null) {
            throw new IllegalArgumentException("idType can not be null");
        }
        this._idType = idType;
        this._customIdResolver = idRes;
        this._typeProperty = idType.getDefaultPropertyName();
        return this;
    }

    public TypeSerializer buildTypeSerializer(JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
        TypeIdResolver idRes = idResolver(baseType, subtypes, true, false);
        switch (C09451.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[this._includeAs.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return new AsArrayTypeSerializer(idRes, property);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return new AsPropertyTypeSerializer(idRes, property, this._typeProperty);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return new AsWrapperTypeSerializer(idRes, property);
            default:
                throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
        }
    }

    public TypeDeserializer buildTypeDeserializer(JavaType baseType, Collection<NamedType> subtypes, BeanProperty property) {
        TypeIdResolver idRes = idResolver(baseType, subtypes, false, true);
        switch (C09451.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$As[this._includeAs.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return new AsArrayTypeDeserializer(baseType, idRes, property);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return new AsPropertyTypeDeserializer(baseType, idRes, property, this._typeProperty);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return new AsWrapperTypeDeserializer(baseType, idRes, property);
            default:
                throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
        }
    }

    public StdTypeResolverBuilder inclusion(As includeAs) {
        if (includeAs == null) {
            throw new IllegalArgumentException("includeAs can not be null");
        }
        this._includeAs = includeAs;
        return this;
    }

    public StdTypeResolverBuilder typeProperty(String typeIdPropName) {
        if (typeIdPropName == null || typeIdPropName.length() == 0) {
            typeIdPropName = this._idType.getDefaultPropertyName();
        }
        this._typeProperty = typeIdPropName;
        return this;
    }

    public String getTypeProperty() {
        return this._typeProperty;
    }

    protected TypeIdResolver idResolver(JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser) {
        if (this._customIdResolver != null) {
            return this._customIdResolver;
        }
        if (this._idType == null) {
            throw new IllegalStateException("Can not build, 'init()' not yet called");
        }
        switch (C09451.$SwitchMap$org$codehaus$jackson$annotate$JsonTypeInfo$Id[this._idType.ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return new ClassNameIdResolver(baseType);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return new MinimalClassNameIdResolver(baseType);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return TypeNameIdResolver.construct(baseType, subtypes, forSer, forDeser);
            default:
                throw new IllegalStateException("Do not know how to construct standard type id resolver for idType: " + this._idType);
        }
    }
}
