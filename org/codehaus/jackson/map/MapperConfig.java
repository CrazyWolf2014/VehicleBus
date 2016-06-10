package org.codehaus.jackson.map;

import java.text.DateFormat;
import java.util.Map;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.introspect.VisibilityChecker;
import org.codehaus.jackson.map.jsontype.SubtypeResolver;
import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
import org.codehaus.jackson.type.JavaType;

public interface MapperConfig<T extends MapperConfig<T>> extends MixInResolver {
    void addMixInAnnotations(Class<?> cls, Class<?> cls2);

    void appendAnnotationIntrospector(AnnotationIntrospector annotationIntrospector);

    T createUnshared(TypeResolverBuilder<?> typeResolverBuilder, VisibilityChecker<?> visibilityChecker, SubtypeResolver subtypeResolver);

    Class<?> findMixInClassFor(Class<?> cls);

    void fromAnnotations(Class<?> cls);

    AnnotationIntrospector getAnnotationIntrospector();

    DateFormat getDateFormat();

    TypeResolverBuilder<?> getDefaultTyper(JavaType javaType);

    VisibilityChecker<?> getDefaultVisibilityChecker();

    SubtypeResolver getSubtypeResolver();

    void insertAnnotationIntrospector(AnnotationIntrospector annotationIntrospector);

    <DESC extends BeanDescription> DESC introspectClassAnnotations(Class<?> cls);

    <DESC extends BeanDescription> DESC introspectDirectClassAnnotations(Class<?> cls);

    void setAnnotationIntrospector(AnnotationIntrospector annotationIntrospector);

    void setDateFormat(DateFormat dateFormat);

    void setIntrospector(ClassIntrospector<? extends BeanDescription> classIntrospector);

    void setMixInAnnotations(Map<Class<?>, Class<?>> map);

    void setSubtypeResolver(SubtypeResolver subtypeResolver);
}
