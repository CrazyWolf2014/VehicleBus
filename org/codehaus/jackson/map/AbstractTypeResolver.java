package org.codehaus.jackson.map;

import org.codehaus.jackson.type.JavaType;

public abstract class AbstractTypeResolver {
    public abstract JavaType resolveAbstractType(DeserializationConfig deserializationConfig, JavaType javaType);
}
