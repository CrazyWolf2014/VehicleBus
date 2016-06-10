package org.codehaus.jackson.map;

import java.lang.reflect.Type;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.deser.BeanDeserializerModifier;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public abstract class DeserializerFactory {
    protected static final Deserializers[] NO_DESERIALIZERS;

    public static abstract class Config {
        public abstract Iterable<BeanDeserializerModifier> deserializerModifiers();

        public abstract Iterable<Deserializers> deserializers();

        public abstract boolean hasDeserializerModifiers();

        public abstract boolean hasDeserializers();

        public abstract Config withAdditionalDeserializers(Deserializers deserializers);

        public abstract Config withDeserializerModifier(BeanDeserializerModifier beanDeserializerModifier);
    }

    public abstract JsonDeserializer<?> createArrayDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, ArrayType arrayType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, CollectionType collectionType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract JsonDeserializer<?> createEnumDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract JsonDeserializer<?> createMapDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, MapType mapType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract JsonDeserializer<?> createTreeDeserializer(DeserializationConfig deserializationConfig, DeserializerProvider deserializerProvider, JavaType javaType, BeanProperty beanProperty) throws JsonMappingException;

    public abstract Config getConfig();

    public abstract DeserializerFactory withConfig(Config config);

    static {
        NO_DESERIALIZERS = new Deserializers[0];
    }

    public final DeserializerFactory withAdditionalDeserializers(Deserializers additional) {
        return withConfig(getConfig().withAdditionalDeserializers(additional));
    }

    public final DeserializerFactory withDeserializerModifier(BeanDeserializerModifier modifier) {
        return withConfig(getConfig().withDeserializerModifier(modifier));
    }

    public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType, BeanProperty property) {
        return null;
    }

    @Deprecated
    public final TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType) {
        return findTypeDeserializer(config, baseType, null);
    }

    @Deprecated
    public final JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, JavaType type, DeserializerProvider p) throws JsonMappingException {
        return createBeanDeserializer(config, p, type, null);
    }

    @Deprecated
    public final JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, ArrayType type, DeserializerProvider p) throws JsonMappingException {
        return createArrayDeserializer(config, p, type, null);
    }

    @Deprecated
    public final JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig config, CollectionType type, DeserializerProvider p) throws JsonMappingException {
        return createCollectionDeserializer(config, p, type, null);
    }

    @Deprecated
    public final JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, Class<?> enumClass, DeserializerProvider p) throws JsonMappingException {
        return createEnumDeserializer(config, p, TypeFactory.type((Type) enumClass), null);
    }

    @Deprecated
    public final JsonDeserializer<?> createMapDeserializer(DeserializationConfig config, MapType type, DeserializerProvider p) throws JsonMappingException {
        return createMapDeserializer(config, p, type, null);
    }

    @Deprecated
    public final JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, Class<? extends JsonNode> nodeClass, DeserializerProvider p) throws JsonMappingException {
        return createTreeDeserializer(config, p, TypeFactory.type((Type) nodeClass), null);
    }
}
