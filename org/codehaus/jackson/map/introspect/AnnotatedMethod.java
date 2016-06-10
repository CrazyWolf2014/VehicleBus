package org.codehaus.jackson.map.introspect;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.codehaus.jackson.map.type.TypeBindings;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public final class AnnotatedMethod extends AnnotatedWithParams {
    protected final Method _method;
    protected Class<?>[] _paramTypes;

    public AnnotatedMethod(Method method, AnnotationMap classAnn, AnnotationMap[] paramAnnotations) {
        super(classAnn, paramAnnotations);
        this._method = method;
    }

    public AnnotatedMethod withMethod(Method m) {
        return new AnnotatedMethod(m, this._annotations, this._paramAnnotations);
    }

    public Method getAnnotated() {
        return this._method;
    }

    public int getModifiers() {
        return this._method.getModifiers();
    }

    public String getName() {
        return this._method.getName();
    }

    public Type getGenericType() {
        return this._method.getGenericReturnType();
    }

    public Class<?> getRawType() {
        return this._method.getReturnType();
    }

    public JavaType getType(TypeBindings bindings) {
        TypeVariable<?>[] localTypeParams = this._method.getTypeParameters();
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

    public Class<?> getDeclaringClass() {
        return this._method.getDeclaringClass();
    }

    public Member getMember() {
        return this._method;
    }

    public AnnotatedParameter getParameter(int index) {
        return new AnnotatedParameter(this, getParameterType(index), this._paramAnnotations[index]);
    }

    public int getParameterCount() {
        return getParameterTypes().length;
    }

    public Type[] getParameterTypes() {
        return this._method.getGenericParameterTypes();
    }

    public Class<?> getParameterClass(int index) {
        Class<?>[] types = this._method.getParameterTypes();
        return index >= types.length ? null : types[index];
    }

    public Type getParameterType(int index) {
        Type[] types = this._method.getGenericParameterTypes();
        return index >= types.length ? null : types[index];
    }

    public Class<?>[] getParameterClasses() {
        if (this._paramTypes == null) {
            this._paramTypes = this._method.getParameterTypes();
        }
        return this._paramTypes;
    }

    public String getFullName() {
        return getDeclaringClass().getName() + "#" + getName() + "(" + getParameterCount() + " params)";
    }

    public String toString() {
        return "[method " + getName() + ", annotations: " + this._annotations + "]";
    }
}
