package org.codehaus.jackson.map;

import org.codehaus.jackson.map.ser.BeanSerializerModifier;
import org.codehaus.jackson.type.JavaType;

public abstract class SerializerFactory {

    public static abstract class Config {
        public abstract boolean hasSerializerModifiers();

        public abstract boolean hasSerializers();

        public abstract Iterable<BeanSerializerModifier> serializerModifiers();

        public abstract Iterable<Serializers> serializers();

        public abstract Config withAdditionalSerializers(Serializers serializers);

        public abstract Config withSerializerModifier(BeanSerializerModifier beanSerializerModifier);
    }

    public abstract JsonSerializer<Object> createSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanProperty beanProperty);

    public abstract TypeSerializer createTypeSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanProperty beanProperty);

    public abstract Config getConfig();

    public abstract SerializerFactory withConfig(Config config);

    public final SerializerFactory withAdditionalSerializers(Serializers additional) {
        return withConfig(getConfig().withAdditionalSerializers(additional));
    }

    public final SerializerFactory withSerializerModifier(BeanSerializerModifier modifier) {
        return withConfig(getConfig().withSerializerModifier(modifier));
    }

    @Deprecated
    public final JsonSerializer<Object> createSerializer(JavaType type, SerializationConfig config) {
        return createSerializer(config, type, null);
    }

    @Deprecated
    public final TypeSerializer createTypeSerializer(JavaType baseType, SerializationConfig config) {
        return createTypeSerializer(config, baseType, null);
    }
}
