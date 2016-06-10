package org.codehaus.jackson.map;

import org.codehaus.jackson.type.JavaType;

public interface Serializers {
    JsonSerializer<?> findSerializer(SerializationConfig serializationConfig, JavaType javaType, BeanDescription beanDescription, BeanProperty beanProperty);
}
