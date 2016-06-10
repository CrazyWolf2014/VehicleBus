package com.thoughtworks.xstream.core;

import com.thoughtworks.xstream.core.util.CompositeClassLoader;

public final class ClassLoaderReference {
    private transient ClassLoader reference;

    public ClassLoaderReference(ClassLoader reference) {
        setReference(reference);
    }

    public ClassLoader getReference() {
        return this.reference;
    }

    public void setReference(ClassLoader reference) {
        if (reference instanceof com.thoughtworks.xstream.core.util.ClassLoaderReference) {
            reference = ((com.thoughtworks.xstream.core.util.ClassLoaderReference) reference).getReference();
        }
        this.reference = reference;
    }

    private Object readResolve() {
        this.reference = new CompositeClassLoader();
        return this;
    }
}
