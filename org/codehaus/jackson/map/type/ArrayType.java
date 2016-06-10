package org.codehaus.jackson.map.type;

import java.lang.reflect.Array;
import org.codehaus.jackson.type.JavaType;

public final class ArrayType extends TypeBase {
    final JavaType _componentType;
    final Object _emptyArray;

    private ArrayType(JavaType componentType, Object emptyInstance) {
        super(emptyInstance.getClass(), componentType.hashCode());
        this._componentType = componentType;
        this._emptyArray = emptyInstance;
    }

    public static ArrayType construct(JavaType componentType) {
        return new ArrayType(componentType, Array.newInstance(componentType.getRawClass(), 0));
    }

    public ArrayType withTypeHandler(Object h) {
        ArrayType newInstance = new ArrayType(this._componentType, this._emptyArray);
        newInstance._typeHandler = h;
        return newInstance;
    }

    public ArrayType withContentTypeHandler(Object h) {
        return new ArrayType(this._componentType.withTypeHandler(h), this._emptyArray);
    }

    protected String buildCanonicalName() {
        return this._class.getName();
    }

    protected JavaType _narrow(Class<?> subclass) {
        if (subclass.isArray()) {
            return construct(TypeFactory.type(subclass.getComponentType()));
        }
        throw new IllegalArgumentException("Incompatible narrowing operation: trying to narrow " + toString() + " to class " + subclass.getName());
    }

    public JavaType narrowContentsBy(Class<?> contentClass) {
        return contentClass == this._componentType.getRawClass() ? this : construct(this._componentType.narrowBy(contentClass)).copyHandlers(this);
    }

    public boolean isArrayType() {
        return true;
    }

    public boolean isAbstract() {
        return false;
    }

    public boolean isConcrete() {
        return true;
    }

    public boolean hasGenericTypes() {
        return this._componentType.hasGenericTypes();
    }

    public String containedTypeName(int index) {
        if (index == 0) {
            return "E";
        }
        return null;
    }

    public boolean isContainerType() {
        return true;
    }

    public JavaType getContentType() {
        return this._componentType;
    }

    public int containedTypeCount() {
        return 1;
    }

    public JavaType containedType(int index) {
        return index == 0 ? this._componentType : null;
    }

    public StringBuilder getGenericSignature(StringBuilder sb) {
        sb.append('[');
        return this._componentType.getGenericSignature(sb);
    }

    public StringBuilder getErasedSignature(StringBuilder sb) {
        sb.append('[');
        return this._componentType.getErasedSignature(sb);
    }

    public String toString() {
        return "[array type, component type: " + this._componentType + "]";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        return this._componentType.equals(((ArrayType) o)._componentType);
    }
}
