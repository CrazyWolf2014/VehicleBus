package org.codehaus.jackson.map.type;

import org.codehaus.jackson.type.JavaType;

public final class MapType extends TypeBase {
    final JavaType _keyType;
    final JavaType _valueType;

    private MapType(Class<?> mapType, JavaType keyT, JavaType valueT) {
        super(mapType, keyT.hashCode() ^ valueT.hashCode());
        this._keyType = keyT;
        this._valueType = valueT;
    }

    public static MapType construct(Class<?> rawType, JavaType keyT, JavaType valueT) {
        return new MapType(rawType, keyT, valueT);
    }

    protected JavaType _narrow(Class<?> subclass) {
        return new MapType(subclass, this._keyType, this._valueType);
    }

    public JavaType narrowContentsBy(Class<?> contentClass) {
        if (contentClass == this._valueType.getRawClass()) {
            return this;
        }
        return new MapType(this._class, this._keyType, this._valueType.narrowBy(contentClass)).copyHandlers(this);
    }

    public JavaType narrowKey(Class<?> keySubclass) {
        if (keySubclass == this._keyType.getRawClass()) {
            return this;
        }
        return new MapType(this._class, this._keyType.narrowBy(keySubclass), this._valueType).copyHandlers(this);
    }

    public MapType withTypeHandler(Object h) {
        MapType newInstance = new MapType(this._class, this._keyType, this._valueType);
        newInstance._typeHandler = h;
        return newInstance;
    }

    public MapType withContentTypeHandler(Object h) {
        return new MapType(this._class, this._keyType, this._valueType.withTypeHandler(h));
    }

    protected String buildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._keyType != null) {
            sb.append('<');
            sb.append(this._keyType.toCanonical());
            sb.append(',');
            sb.append(this._valueType.toCanonical());
            sb.append('>');
        }
        return sb.toString();
    }

    public boolean isContainerType() {
        return true;
    }

    public JavaType getKeyType() {
        return this._keyType;
    }

    public JavaType getContentType() {
        return this._valueType;
    }

    public int containedTypeCount() {
        return 2;
    }

    public JavaType containedType(int index) {
        if (index == 0) {
            return this._keyType;
        }
        if (index == 1) {
            return this._valueType;
        }
        return null;
    }

    public String containedTypeName(int index) {
        if (index == 0) {
            return "K";
        }
        if (index == 1) {
            return "V";
        }
        return null;
    }

    public StringBuilder getErasedSignature(StringBuilder sb) {
        return TypeBase._classSignature(this._class, sb, true);
    }

    public StringBuilder getGenericSignature(StringBuilder sb) {
        TypeBase._classSignature(this._class, sb, false);
        sb.append('<');
        this._keyType.getGenericSignature(sb);
        this._valueType.getGenericSignature(sb);
        sb.append(">;");
        return sb;
    }

    public String toString() {
        return "[map type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
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
        MapType other = (MapType) o;
        if (this._class == other._class && this._keyType.equals(other._keyType) && this._valueType.equals(other._valueType)) {
            return true;
        }
        return false;
    }
}
