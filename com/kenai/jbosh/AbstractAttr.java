package com.kenai.jbosh;

abstract class AbstractAttr<T extends Comparable> implements Comparable {
    private final T value;

    protected AbstractAttr(T t) {
        this.value = t;
    }

    public final T getValue() {
        return this.value;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractAttr)) {
            return false;
        }
        return this.value.equals(((AbstractAttr) obj).value);
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public String toString() {
        return this.value.toString();
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return 1;
        }
        return this.value.compareTo(obj);
    }
}
