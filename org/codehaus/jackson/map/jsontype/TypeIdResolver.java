package org.codehaus.jackson.map.jsontype;

import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.type.JavaType;

public interface TypeIdResolver {
    Id getMechanism();

    String idFromValue(Object obj);

    void init(JavaType javaType);

    JavaType typeFromId(String str);
}
