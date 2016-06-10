package org.codehaus.jackson.map.introspect;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ClassIntrospector;
import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.MapperConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.map.util.ClassUtil;
import org.codehaus.jackson.type.JavaType;

public class BasicClassIntrospector extends ClassIntrospector<BasicBeanDescription> {
    public static final BasicClassIntrospector instance;

    public static class GetterMethodFilter implements MethodFilter {
        public static final GetterMethodFilter instance;

        static {
            instance = new GetterMethodFilter();
        }

        private GetterMethodFilter() {
        }

        public boolean includeMethod(Method m) {
            return ClassUtil.hasGetterSignature(m);
        }
    }

    public static class SetterMethodFilter implements MethodFilter {
        public static final SetterMethodFilter instance;

        static {
            instance = new SetterMethodFilter();
        }

        public boolean includeMethod(Method m) {
            if (Modifier.isStatic(m.getModifiers())) {
                return false;
            }
            switch (m.getParameterTypes().length) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    return true;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return true;
                default:
                    return false;
            }
        }
    }

    public static final class SetterAndGetterMethodFilter extends SetterMethodFilter {
        public static final SetterAndGetterMethodFilter instance;

        static {
            instance = new SetterAndGetterMethodFilter();
        }

        public boolean includeMethod(Method m) {
            if (super.includeMethod(m)) {
                return true;
            }
            if (!ClassUtil.hasGetterSignature(m)) {
                return false;
            }
            Class<?> rt = m.getReturnType();
            if (Collection.class.isAssignableFrom(rt) || Map.class.isAssignableFrom(rt)) {
                return true;
            }
            return false;
        }
    }

    static {
        instance = new BasicClassIntrospector();
    }

    public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, MixInResolver r) {
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
        ac.resolveMemberMethods(getSerializationMethodFilter(cfg), false);
        ac.resolveCreators(true);
        ac.resolveFields(false);
        return new BasicBeanDescription(type, ac, ai);
    }

    public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
        ac.resolveMemberMethods(getDeserializationMethodFilter(cfg), true);
        ac.resolveCreators(true);
        ac.resolveFields(true);
        return new BasicBeanDescription(type, ac, ai);
    }

    public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, MixInResolver r) {
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        AnnotatedClass ac = AnnotatedClass.construct(type.getRawClass(), ai, r);
        ac.resolveCreators(true);
        return new BasicBeanDescription(type, ac, ai);
    }

    public BasicBeanDescription forClassAnnotations(MapperConfig<?> cfg, Class<?> c, MixInResolver r) {
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        return new BasicBeanDescription(TypeFactory.type((Type) c), AnnotatedClass.construct(c, ai, r), ai);
    }

    public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> cfg, Class<?> c, MixInResolver r) {
        AnnotationIntrospector ai = cfg.getAnnotationIntrospector();
        return new BasicBeanDescription(TypeFactory.type((Type) c), AnnotatedClass.constructWithoutSuperTypes(c, ai, r), ai);
    }

    protected MethodFilter getSerializationMethodFilter(SerializationConfig cfg) {
        return GetterMethodFilter.instance;
    }

    protected MethodFilter getDeserializationMethodFilter(DeserializationConfig cfg) {
        if (cfg.isEnabled(Feature.USE_GETTERS_AS_SETTERS)) {
            return SetterAndGetterMethodFilter.instance;
        }
        return SetterMethodFilter.instance;
    }
}
