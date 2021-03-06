package org.codehaus.jackson.map.jsontype;

import java.util.Collection;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.TypeDeserializer;
import org.codehaus.jackson.map.TypeSerializer;
import org.codehaus.jackson.type.JavaType;

public interface TypeResolverBuilder<T extends TypeResolverBuilder<T>> {
    TypeDeserializer buildTypeDeserializer(JavaType javaType, Collection<NamedType> collection, BeanProperty beanProperty);

    TypeSerializer buildTypeSerializer(JavaType javaType, Collection<NamedType> collection, BeanProperty beanProperty);

    T inclusion(As as);

    T init(Id id, TypeIdResolver typeIdResolver);

    T typeProperty(String str);
}
