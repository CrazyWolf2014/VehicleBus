package com.thoughtworks.xstream.core.util;

import org.xmlpull.v1.XmlPullParser;

public final class FastField {
    private final String declaringClass;
    private final String name;

    public FastField(String definedIn, String name) {
        this.name = name;
        this.declaringClass = definedIn;
    }

    public FastField(Class definedIn, String name) {
        this(definedIn == null ? null : definedIn.getName(), name);
    }

    public String getName() {
        return this.name;
    }

    public String getDeclaringClass() {
        return this.declaringClass;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof FastField)) {
            return false;
        }
        FastField field = (FastField) obj;
        if (this.declaringClass == null && field.declaringClass != null) {
            return false;
        }
        if (this.declaringClass != null && field.declaringClass == null) {
            return false;
        }
        if (!(this.name.equals(field.getName()) && (this.declaringClass == null || this.declaringClass.equals(field.getDeclaringClass())))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (this.declaringClass == null ? 0 : this.declaringClass.hashCode()) ^ this.name.hashCode();
    }

    public String toString() {
        return (this.declaringClass == null ? XmlPullParser.NO_NAMESPACE : this.declaringClass + ".") + this.name;
    }
}
