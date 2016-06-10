package com.thoughtworks.xstream.converters.reflection;

public class FieldKey {
    private final Class declaringClass;
    private final int depth;
    private final String fieldName;
    private final int order;

    public FieldKey(String fieldName, Class declaringClass, int order) {
        if (fieldName == null || declaringClass == null) {
            throw new IllegalArgumentException("fieldName or declaringClass is null");
        }
        this.fieldName = fieldName;
        this.declaringClass = declaringClass;
        this.order = order;
        int i = 0;
        for (Class c = declaringClass; c.getSuperclass() != null; c = c.getSuperclass()) {
            i++;
        }
        this.depth = i;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public Class getDeclaringClass() {
        return this.declaringClass;
    }

    public int getDepth() {
        return this.depth;
    }

    public int getOrder() {
        return this.order;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldKey)) {
            return false;
        }
        FieldKey fieldKey = (FieldKey) o;
        if (!this.declaringClass.equals(fieldKey.declaringClass)) {
            return false;
        }
        if (this.fieldName.equals(fieldKey.fieldName)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (this.fieldName.hashCode() * 29) + this.declaringClass.hashCode();
    }

    public String toString() {
        return "FieldKey{order=" + this.order + ", writer=" + this.depth + ", declaringClass=" + this.declaringClass + ", fieldName='" + this.fieldName + "'" + "}";
    }
}
