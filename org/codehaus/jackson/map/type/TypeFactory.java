package org.codehaus.jackson.map.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

public class TypeFactory {
    private static final JavaType[] NO_TYPES;
    public static final TypeFactory instance;
    protected final TypeParser _parser;

    static {
        instance = new TypeFactory();
        NO_TYPES = new JavaType[0];
    }

    private TypeFactory() {
        this._parser = new TypeParser(this);
    }

    public static JavaType type(Type t) {
        return instance._fromType(t, null);
    }

    public static JavaType type(Type type, Class<?> context) {
        return type(type, new TypeBindings((Class) context));
    }

    public static JavaType type(Type type, JavaType context) {
        return type(type, new TypeBindings(context));
    }

    public static JavaType type(Type type, TypeBindings bindings) {
        return instance._fromType(type, bindings);
    }

    public static JavaType type(TypeReference<?> ref) {
        return type(ref.getType());
    }

    public static JavaType arrayType(Class<?> elementType) {
        return arrayType(type((Type) elementType));
    }

    public static JavaType arrayType(JavaType elementType) {
        return ArrayType.construct(elementType);
    }

    public static JavaType collectionType(Class<? extends Collection> collectionType, Class<?> elementType) {
        return collectionType((Class) collectionType, type((Type) elementType));
    }

    public static JavaType collectionType(Class<? extends Collection> collectionType, JavaType elementType) {
        return CollectionType.construct(collectionType, elementType);
    }

    public static JavaType mapType(Class<? extends Map> mapType, Class<?> keyType, Class<?> valueType) {
        return mapType((Class) mapType, type((Type) keyType), type((Type) valueType));
    }

    public static JavaType mapType(Class<? extends Map> mapType, JavaType keyType, JavaType valueType) {
        return MapType.construct(mapType, keyType, valueType);
    }

    public static JavaType parametricType(Class<?> parametrized, Class<?>... parameterClasses) {
        int len = parameterClasses.length;
        JavaType[] pt = new JavaType[len];
        for (int i = 0; i < len; i++) {
            pt[i] = instance._fromClass(parameterClasses[i], null);
        }
        return parametricType((Class) parametrized, pt);
    }

    public static JavaType parametricType(Class<?> parametrized, JavaType... parameterTypes) {
        if (parametrized.isArray()) {
            if (parameterTypes.length == 1) {
                return ArrayType.construct(parameterTypes[0]);
            }
            throw new IllegalArgumentException("Need exactly 1 parameter type for arrays (" + parametrized.getName() + ")");
        } else if (Map.class.isAssignableFrom(parametrized)) {
            if (parameterTypes.length == 2) {
                return MapType.construct(parametrized, parameterTypes[0], parameterTypes[1]);
            }
            throw new IllegalArgumentException("Need exactly 2 parameter types for Map types (" + parametrized.getName() + ")");
        } else if (!Collection.class.isAssignableFrom(parametrized)) {
            return _constructSimple(parametrized, parameterTypes);
        } else {
            if (parameterTypes.length == 1) {
                return CollectionType.construct(parametrized, parameterTypes[0]);
            }
            throw new IllegalArgumentException("Need exactly 1 parameter type for Collection types (" + parametrized.getName() + ")");
        }
    }

    public static JavaType fromCanonical(String canonical) throws IllegalArgumentException {
        return instance._parser.parse(canonical);
    }

    public static JavaType specialize(JavaType baseType, Class<?> subclass) {
        if (!(baseType instanceof SimpleType) || (!subclass.isArray() && !Map.class.isAssignableFrom(subclass) && !Collection.class.isAssignableFrom(subclass))) {
            return baseType.narrowBy(subclass);
        }
        if (baseType.getRawClass().isAssignableFrom(subclass)) {
            JavaType subtype = instance._fromClass(subclass, new TypeBindings(baseType.getRawClass()));
            Object h = baseType.getValueHandler();
            if (h != null) {
                subtype.setValueHandler(h);
            }
            h = baseType.getTypeHandler();
            if (h != null) {
                return subtype.withTypeHandler(h);
            }
            return subtype;
        }
        throw new IllegalArgumentException("Class " + subclass.getClass().getName() + " not subtype of " + baseType);
    }

    public static JavaType fastSimpleType(Class<?> cls) {
        return new SimpleType(cls, null, null);
    }

    public static JavaType[] findParameterTypes(Class<?> clz, Class<?> expType) {
        return findParameterTypes(clz, expType, new TypeBindings((Class) clz));
    }

    public static JavaType[] findParameterTypes(Class<?> clz, Class<?> expType, TypeBindings bindings) {
        HierarchicType subType = _findSuperTypeChain(clz, expType);
        if (subType == null) {
            throw new IllegalArgumentException("Class " + clz.getName() + " is not a subtype of " + expType.getName());
        }
        HierarchicType superType = subType;
        while (superType.getSuperType() != null) {
            superType = superType.getSuperType();
            Class raw = superType.getRawClass();
            TypeBindings newBindings = new TypeBindings(raw);
            if (superType.isGeneric()) {
                Type[] actualTypes = superType.asGeneric().getActualTypeArguments();
                TypeVariable<?>[] vars = raw.getTypeParameters();
                int len = actualTypes.length;
                for (int i = 0; i < len; i++) {
                    newBindings.addBinding(vars[i].getName(), instance._fromType(actualTypes[i], bindings));
                }
            }
            bindings = newBindings;
        }
        if (superType.isGeneric()) {
            return bindings.typesAsArray();
        }
        return null;
    }

    public static JavaType[] findParameterTypes(JavaType type, Class<?> expType) {
        Class<?> raw = type.getRawClass();
        if (raw != expType) {
            return findParameterTypes(raw, expType, new TypeBindings(type));
        }
        int count = type.containedTypeCount();
        if (count == 0) {
            return null;
        }
        JavaType[] result = new JavaType[count];
        for (int i = 0; i < count; i++) {
            result[i] = type.containedType(i);
        }
        return result;
    }

    @Deprecated
    public static JavaType fromClass(Class<?> clz) {
        return instance._fromClass(clz, null);
    }

    @Deprecated
    public static JavaType fromTypeReference(TypeReference<?> ref) {
        return type(ref.getType());
    }

    @Deprecated
    public static JavaType fromType(Type type) {
        return instance._fromType(type, null);
    }

    protected JavaType _fromClass(Class<?> clz, TypeBindings context) {
        if (clz.isArray()) {
            return ArrayType.construct(_fromType(clz.getComponentType(), null));
        }
        if (clz.isEnum()) {
            return new SimpleType(clz);
        }
        if (Map.class.isAssignableFrom(clz)) {
            return _mapType(clz);
        }
        if (Collection.class.isAssignableFrom(clz)) {
            return _collectionType(clz);
        }
        return new SimpleType(clz);
    }

    protected JavaType _fromParameterizedClass(Class<?> clz, List<JavaType> paramTypes) {
        if (clz.isArray()) {
            return ArrayType.construct(_fromType(clz.getComponentType(), null));
        }
        if (clz.isEnum()) {
            return new SimpleType(clz);
        }
        if (Map.class.isAssignableFrom(clz)) {
            if (paramTypes.size() <= 0) {
                return _mapType(clz);
            }
            return MapType.construct(clz, (JavaType) paramTypes.get(0), paramTypes.size() >= 2 ? (JavaType) paramTypes.get(1) : _unknownType());
        } else if (Collection.class.isAssignableFrom(clz)) {
            if (paramTypes.size() >= 1) {
                return CollectionType.construct(clz, (JavaType) paramTypes.get(0));
            }
            return _collectionType(clz);
        } else if (paramTypes.size() == 0) {
            return new SimpleType(clz);
        } else {
            return _constructSimple(clz, (JavaType[]) paramTypes.toArray(new JavaType[paramTypes.size()]));
        }
    }

    public JavaType _fromType(Type type, TypeBindings context) {
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (context == null) {
                context = new TypeBindings(cls);
            }
            return _fromClass(cls, context);
        } else if (type instanceof ParameterizedType) {
            return _fromParamType((ParameterizedType) type, context);
        } else {
            if (type instanceof GenericArrayType) {
                return _fromArrayType((GenericArrayType) type, context);
            }
            if (type instanceof TypeVariable) {
                return _fromVariable((TypeVariable) type, context);
            }
            if (type instanceof WildcardType) {
                return _fromWildcard((WildcardType) type, context);
            }
            throw new IllegalArgumentException("Unrecognized Type: " + type.toString());
        }
    }

    protected JavaType _fromParamType(ParameterizedType type, TypeBindings context) {
        JavaType[] pt;
        Class<?> rawType = (Class) type.getRawType();
        Type[] args = type.getActualTypeArguments();
        int paramCount = args == null ? 0 : args.length;
        if (paramCount == 0) {
            pt = NO_TYPES;
        } else {
            pt = new JavaType[paramCount];
            for (int i = 0; i < paramCount; i++) {
                pt[i] = _fromType(args[i], context);
            }
        }
        if (Map.class.isAssignableFrom(rawType)) {
            JavaType[] mapParams = findParameterTypes(_constructSimple(rawType, pt), Map.class);
            if (mapParams.length == 2) {
                return MapType.construct(rawType, mapParams[0], mapParams[1]);
            }
            throw new IllegalArgumentException("Could not find 2 type parameters for Map class " + rawType.getName() + " (found " + mapParams.length + ")");
        } else if (Collection.class.isAssignableFrom(rawType)) {
            JavaType[] collectionParams = findParameterTypes(_constructSimple(rawType, pt), Collection.class);
            if (collectionParams.length == 1) {
                return CollectionType.construct(rawType, collectionParams[0]);
            }
            throw new IllegalArgumentException("Could not find 1 type parameter for Collection class " + rawType.getName() + " (found " + collectionParams.length + ")");
        } else if (paramCount == 0) {
            return new SimpleType(rawType);
        } else {
            return _constructSimple(rawType, pt);
        }
    }

    protected static SimpleType _constructSimple(Class<?> rawType, JavaType[] parameterTypes) {
        TypeVariable<?>[] typeVars = rawType.getTypeParameters();
        if (typeVars.length != parameterTypes.length) {
            throw new IllegalArgumentException("Parameter type mismatch for " + rawType.getName() + ": expected " + typeVars.length + " parameters, was given " + parameterTypes.length);
        }
        String[] names = new String[typeVars.length];
        int len = typeVars.length;
        for (int i = 0; i < len; i++) {
            names[i] = typeVars[i].getName();
        }
        return new SimpleType(rawType, names, parameterTypes);
    }

    protected JavaType _fromArrayType(GenericArrayType type, TypeBindings context) {
        return ArrayType.construct(_fromType(type.getGenericComponentType(), context));
    }

    protected JavaType _fromVariable(TypeVariable<?> type, TypeBindings context) {
        if (context == null) {
            return _unknownType();
        }
        String name = type.getName();
        JavaType actualType = context.findType(name);
        if (actualType != null) {
            return actualType;
        }
        Type[] bounds = type.getBounds();
        context._addPlaceholder(name);
        return _fromType(bounds[0], context);
    }

    protected JavaType _fromWildcard(WildcardType type, TypeBindings context) {
        return _fromType(type.getUpperBounds()[0], context);
    }

    private JavaType _mapType(Class<?> rawClass) {
        JavaType[] typeParams = findParameterTypes((Class) rawClass, Map.class);
        if (typeParams == null) {
            return MapType.construct(rawClass, _unknownType(), _unknownType());
        }
        if (typeParams.length == 2) {
            return MapType.construct(rawClass, typeParams[0], typeParams[1]);
        }
        throw new IllegalArgumentException("Strange Map type " + rawClass.getName() + ": can not determine type parameters");
    }

    private JavaType _collectionType(Class<?> rawClass) {
        JavaType[] typeParams = findParameterTypes((Class) rawClass, Collection.class);
        if (typeParams == null) {
            return CollectionType.construct(rawClass, _unknownType());
        }
        if (typeParams.length == 1) {
            return CollectionType.construct(rawClass, typeParams[0]);
        }
        throw new IllegalArgumentException("Strange Collection type " + rawClass.getName() + ": can not determine type parameters");
    }

    protected static JavaType _resolveVariableViaSubTypes(HierarchicType leafType, String variableName, TypeBindings bindings) {
        if (leafType != null && leafType.isGeneric()) {
            TypeVariable<?>[] typeVariables = leafType.getRawClass().getTypeParameters();
            int len = typeVariables.length;
            for (int i = 0; i < len; i++) {
                if (variableName.equals(typeVariables[i].getName())) {
                    Type type = leafType.asGeneric().getActualTypeArguments()[i];
                    if (type instanceof TypeVariable) {
                        return _resolveVariableViaSubTypes(leafType.getSubType(), ((TypeVariable) type).getName(), bindings);
                    }
                    return instance._fromType(type, bindings);
                }
            }
        }
        return instance._unknownType();
    }

    protected JavaType _unknownType() {
        return _fromClass(Object.class, null);
    }

    protected static HierarchicType _findSuperTypeChain(Class<?> subtype, Class<?> supertype) {
        if (supertype.isInterface()) {
            return _findSuperInterfaceChain(subtype, supertype);
        }
        return _findSuperClassChain(subtype, supertype);
    }

    protected static HierarchicType _findSuperClassChain(Type currentType, Class<?> target) {
        HierarchicType current = new HierarchicType(currentType);
        Class<?> raw = current.getRawClass();
        if (raw == target) {
            return current;
        }
        Type parent = raw.getGenericSuperclass();
        if (parent != null) {
            HierarchicType sup = _findSuperClassChain(parent, target);
            if (sup != null) {
                sup.setSubType(current);
                current.setSuperType(sup);
                return current;
            }
        }
        return null;
    }

    protected static HierarchicType _findSuperInterfaceChain(Type currentType, Class<?> target) {
        HierarchicType current = new HierarchicType(currentType);
        Class<?> raw = current.getRawClass();
        if (raw == target) {
            return current;
        }
        HierarchicType sup;
        Type[] parents = raw.getGenericInterfaces();
        if (parents != null) {
            for (Type parent : parents) {
                sup = _findSuperInterfaceChain(parent, target);
                if (sup != null) {
                    sup.setSubType(current);
                    current.setSuperType(sup);
                    return current;
                }
            }
        }
        Type parent2 = raw.getGenericSuperclass();
        if (parent2 != null) {
            sup = _findSuperInterfaceChain(parent2, target);
            if (sup != null) {
                sup.setSubType(current);
                current.setSuperType(sup);
                return current;
            }
        }
        return null;
    }
}
