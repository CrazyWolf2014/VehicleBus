package org.codehaus.jackson.map.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codehaus.jackson.annotate.JacksonAnnotation;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.JsonSerializer.None;

@JacksonAnnotation
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSerialize {

    public enum Inclusion {
        ALWAYS,
        NON_NULL,
        NON_DEFAULT
    }

    public enum Typing {
        DYNAMIC,
        STATIC
    }

    Class<?> as() default NoClass.class;

    Inclusion include() default Inclusion.ALWAYS;

    Typing typing() default Typing.DYNAMIC;

    Class<? extends JsonSerializer<?>> using() default None.class;
}
