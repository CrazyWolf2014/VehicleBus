package org.codehaus.jackson.map.type;

import org.codehaus.jackson.type.JavaType;

public final class CollectionType extends TypeBase {
    final JavaType _elementType;

    private CollectionType(Class<?> collT, JavaType elemT) {
        super(collT, elemT.hashCode());
        this._elementType = elemT;
    }

    protected JavaType _narrow(Class<?> subclass) {
        return new CollectionType(subclass, this._elementType);
    }

    public JavaType narrowContentsBy(Class<?> contentClass) {
        if (contentClass == this._elementType.getRawClass()) {
            return this;
        }
        return new CollectionType(this._class, this._elementType.narrowBy(contentClass)).copyHandlers(this);
    }

    public static CollectionType construct(Class<?> rawType, JavaType elemT) {
        return new CollectionType(rawType, elemT);
    }

    public CollectionType withTypeHandler(Object h) {
        CollectionType newInstance = new CollectionType(this._class, this._elementType);
        newInstance._typeHandler = h;
        return newInstance;
    }

    public CollectionType withContentTypeHandler(Object h) {
        return new CollectionType(this._class, this._elementType.withTypeHandler(h));
    }

    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._elementType != null) {
            sb.append('<');
            sb.append(this._elementType.toCanonical());
            sb.append('>');
        }
        return sb.toString();
    }

    public JavaType getContentType() {
        return this._elementType;
    }

    public int containedTypeCount() {
        return 1;
    }

    public JavaType containedType(int index) {
        return index == 0 ? this._elementType : null;
    }

    public String containedTypeName(int index) {
        if (index == 0) {
            return "E";
        }
        return null;
    }

    public StringBuilder getErasedSignature(StringBuilder sb) {
        return TypeBase._classSignature(this._class, sb, true);
    }

    public StringBuilder getGenericSignature(StringBuilder sb) {
        TypeBase._classSignature(this._class, sb, false);
        sb.append('<');
        this._elementType.getGenericSignature(sb);
        sb.append(">;");
        return sb;
    }

    public boolean isContainerType() {
        return true;
    }

    public String toString() {
        return "[collection type; class " + this._class.getName() + ", contains " + this._elementType + "]";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        CollectionType other = (CollectionType) o;
        if (this._class == other._class && this._elementType.equals(other._elementType)) {
            return true;
        }
        return false;
    }
}
