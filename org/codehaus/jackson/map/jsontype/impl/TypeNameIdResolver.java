package org.codehaus.jackson.map.jsontype.impl;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.codehaus.jackson.map.jsontype.NamedType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

public class TypeNameIdResolver extends TypeIdResolverBase {
    protected final HashMap<String, JavaType> _idToType;
    protected final HashMap<String, String> _typeToId;

    protected TypeNameIdResolver(JavaType baseType, HashMap<String, String> typeToId, HashMap<String, JavaType> idToType) {
        super(baseType);
        this._typeToId = typeToId;
        this._idToType = idToType;
    }

    public static TypeNameIdResolver construct(JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser) {
        if (forSer == forDeser) {
            throw new IllegalArgumentException();
        }
        HashMap<String, String> typeToId = null;
        HashMap<String, JavaType> idToType = null;
        if (forSer) {
            typeToId = new HashMap();
        }
        if (forDeser) {
            idToType = new HashMap();
        }
        if (subtypes != null) {
            for (NamedType t : subtypes) {
                Type cls = t.getType();
                String id = t.hasName() ? t.getName() : _defaultTypeId(cls);
                if (forSer) {
                    typeToId.put(cls.getName(), id);
                }
                if (forDeser) {
                    JavaType prev = (JavaType) idToType.get(id);
                    if (prev == null || !cls.isAssignableFrom(prev.getRawClass())) {
                        idToType.put(id, TypeFactory.type(cls));
                    }
                }
            }
        }
        return new TypeNameIdResolver(baseType, typeToId, idToType);
    }

    public Id getMechanism() {
        return Id.NAME;
    }

    public String idFromValue(Object value) {
        Class<?> cls = value.getClass();
        String name = (String) this._typeToId.get(cls.getName());
        if (name == null) {
            return _defaultTypeId(cls);
        }
        return name;
    }

    public JavaType typeFromId(String id) throws IllegalArgumentException {
        return (JavaType) this._idToType.get(id);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[').append(getClass().getName());
        sb.append("; id-to-type=").append(this._idToType);
        sb.append(']');
        return sb.toString();
    }

    protected static String _defaultTypeId(Class<?> cls) {
        String n = cls.getName();
        int ix = n.lastIndexOf(46);
        return ix < 0 ? n : n.substring(ix + 1);
    }
}
