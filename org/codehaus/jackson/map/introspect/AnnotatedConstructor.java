package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public final class AnnotatedConstructor extends AnnotatedWithParams {
    protected final Constructor<?> _constructor;

    public AnnotatedConstructor(Constructor<?> constructor, AnnotationMap classAnn, AnnotationMap[] paramAnn) {
        super(classAnn, paramAnn);
        if (constructor == null) {
            throw new IllegalArgumentException("Null constructor not allowed");
        }
        this._constructor = constructor;
    }

    public Constructor<?> getAnnotated() {
        return this._constructor;
    }

    public int getModifiers() {
        return this._constructor.getModifiers();
    }

    public String getName() {
        return this._constructor.getName();
    }

    public Type getGenericType() {
        return getRawType();
    }

    public Class<?> getRawType() {
        return this._constructor.getDeclaringClass();
    }

    public JavaType getType(TypeBindings bindings) {
        TypeVariable<?>[] localTypeParams = this._constructor.getTypeParameters();
        if (localTypeParams != null && localTypeParams.length > 0) {
            bindings = bindings.childInstance();
            for (TypeVariable<?> var : localTypeParams) {
                bindings._addPlaceholder(var.getName());
                Type lowerBound = var.getBounds()[0];
                bindings.addBinding(var.getName(), lowerBound == null ? TypeFactory.fastSimpleType(Object.class) : TypeFactory.type(lowerBound, bindings));
            }
        }
        return TypeFactory.type(getGenericType(), bindings);
    }

    public AnnotatedParameter getParameter(int index) {
        return new AnnotatedParameter(this, getParameterType(index), this._paramAnnotations[index]);
    }

    public int getParameterCount() {
        return this._constructor.getParameterTypes().length;
    }

    public Class<?> getParameterClass(int index) {
        Class<?>[] types = this._constructor.getParameterTypes();
        return index >= types.length ? null : types[index];
    }

    public Type getParameterType(int index) {
        Type[] types = this._constructor.getGenericParameterTypes();
        return index >= types.length ? null : types[index];
    }

    public Class<?> getDeclaringClass() {
        return this._constructor.getDeclaringClass();
    }

    public Member getMember() {
        return this._constructor;
    }

    public String toString() {
        return "[constructor for " + getName() + ", annotations: " + this._annotations + "]";
    }
}
