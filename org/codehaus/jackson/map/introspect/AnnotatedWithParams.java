package org.codehaus.jackson.map.introspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public abstract class AnnotatedWithParams extends AnnotatedMember {
    protected final AnnotationMap _annotations;
    protected final AnnotationMap[] _paramAnnotations;

    public abstract AnnotatedParameter getParameter(int i);

    public abstract Class<?> getParameterClass(int i);

    public abstract int getParameterCount();

    public abstract Type getParameterType(int i);

    protected AnnotatedWithParams(AnnotationMap classAnn, AnnotationMap[] paramAnnotations) {
        this._annotations = classAnn;
        this._paramAnnotations = paramAnnotations;
    }

    public final void addOrOverride(Annotation a) {
        this._annotations.add(a);
    }

    public final void addOrOverrideParam(int paramIndex, Annotation a) {
        AnnotationMap old = this._paramAnnotations[paramIndex];
        if (old == null) {
            old = new AnnotationMap();
            this._paramAnnotations[paramIndex] = old;
        }
        old.add(a);
    }

    public final void addIfNotPresent(Annotation a) {
        this._annotations.addIfNotPresent(a);
    }

    public final <A extends Annotation> A getAnnotation(Class<A> acls) {
        return this._annotations.get(acls);
    }

    public final AnnotationMap getParameterAnnotations(int index) {
        if (this._paramAnnotations == null || index < 0 || index > this._paramAnnotations.length) {
            return null;
        }
        return this._paramAnnotations[index];
    }

    public final int getAnnotationCount() {
        return this._annotations.size();
    }
}
