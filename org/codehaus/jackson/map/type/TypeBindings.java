package org.codehaus.jackson.map.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.type.JavaType;

public class TypeBindings {
    private static final JavaType[] NO_TYPES;
    public static final JavaType UNBOUND;
    protected Map<String, JavaType> _bindings;
    protected final Class<?> _contextClass;
    protected final JavaType _contextType;
    private final TypeBindings _parentBindings;
    protected HashSet<String> _placeholders;

    static {
        NO_TYPES = new JavaType[0];
        UNBOUND = new SimpleType(Object.class);
    }

    public TypeBindings(Class<?> cc) {
        this(null, cc, null);
    }

    public TypeBindings(JavaType type) {
        this(null, type.getRawClass(), type);
    }

    public TypeBindings childInstance() {
        return new TypeBindings(this, this._contextClass, this._contextType);
    }

    private TypeBindings(TypeBindings parent, Class<?> cc, JavaType type) {
        this._parentBindings = parent;
        this._contextClass = cc;
        this._contextType = type;
    }

    public int getBindingCount() {
        if (this._bindings == null) {
            _resolve();
        }
        return this._bindings.size();
    }

    public JavaType findType(String name) {
        if (this._bindings == null) {
            _resolve();
        }
        JavaType t = (JavaType) this._bindings.get(name);
        if (t != null) {
            return t;
        }
        if (this._placeholders != null && this._placeholders.contains(name)) {
            return UNBOUND;
        }
        if (this._parentBindings != null) {
            return this._parentBindings.findType(name);
        }
        String className;
        if (this._contextClass != null) {
            Class<?> enclosing = this._contextClass.getEnclosingClass();
            if (enclosing != null) {
                Package pkg = enclosing.getPackage();
                if (pkg != null && pkg.getName().startsWith("java.util")) {
                    return UNBOUND;
                }
            }
        }
        if (this._contextClass != null) {
            className = this._contextClass.getName();
        } else if (this._contextType != null) {
            className = this._contextType.toString();
        } else {
            className = "UNKNOWN";
        }
        throw new IllegalArgumentException("Type variable '" + name + "' can not be resolved (with context of class " + className + ")");
    }

    public void addBinding(String name, JavaType type) {
        if (this._bindings == null) {
            this._bindings = new LinkedHashMap();
        }
        this._bindings.put(name, type);
    }

    public JavaType[] typesAsArray() {
        if (this._bindings == null) {
            _resolve();
        }
        if (this._bindings.size() == 0) {
            return NO_TYPES;
        }
        return (JavaType[]) this._bindings.values().toArray(new JavaType[this._bindings.size()]);
    }

    protected void _resolve() {
        _resolveBindings(this._contextClass);
        if (this._contextType != null) {
            int count = this._contextType.containedTypeCount();
            if (count > 0) {
                if (this._bindings == null) {
                    this._bindings = new LinkedHashMap();
                }
                for (int i = 0; i < count; i++) {
                    this._bindings.put(this._contextType.containedTypeName(i), this._contextType.containedType(i));
                }
            }
        }
        if (this._bindings == null) {
            this._bindings = Collections.emptyMap();
        }
    }

    public void _addPlaceholder(String name) {
        if (this._placeholders == null) {
            this._placeholders = new HashSet();
        }
        this._placeholders.add(name);
    }

    protected void _resolveBindings(Type t) {
        if (t != null) {
            Class<?> raw;
            TypeVariable<?>[] vars;
            String name;
            if (t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) t;
                Type[] args = pt.getActualTypeArguments();
                if (args != null && args.length > 0) {
                    Class<?> rawType = (Class) pt.getRawType();
                    vars = rawType.getTypeParameters();
                    if (vars.length != args.length) {
                        throw new IllegalArgumentException("Strange parametrized type (in class " + rawType.getName() + "): number of type arguments != number of type parameters (" + args.length + " vs " + vars.length + ")");
                    }
                    int len = args.length;
                    for (int i = 0; i < len; i++) {
                        name = vars[i].getName();
                        if (this._bindings == null) {
                            this._bindings = new LinkedHashMap();
                        } else {
                            if (this._bindings.containsKey(name)) {
                            }
                        }
                        _addPlaceholder(name);
                        this._bindings.put(name, TypeFactory.instance._fromType(args[i], this));
                    }
                }
                raw = (Class) pt.getRawType();
            } else if (t instanceof Class) {
                raw = (Class) t;
                vars = raw.getTypeParameters();
                if (vars != null && vars.length > 0) {
                    for (TypeVariable<?> var : vars) {
                        name = var.getName();
                        Type varType = var.getBounds()[0];
                        if (varType != null) {
                            if (this._bindings == null) {
                                this._bindings = new LinkedHashMap();
                            } else {
                                if (this._bindings.containsKey(name)) {
                                }
                            }
                            _addPlaceholder(name);
                            this._bindings.put(name, TypeFactory.instance._fromType(varType, this));
                        }
                    }
                }
            } else {
                return;
            }
            _resolveBindings(raw.getGenericSuperclass());
            for (Type intType : raw.getGenericInterfaces()) {
                _resolveBindings(intType);
            }
        }
    }

    public String toString() {
        if (this._bindings == null) {
            _resolve();
        }
        StringBuilder sb = new StringBuilder("[TypeBindings for ");
        if (this._contextType != null) {
            sb.append(this._contextType.toString());
        } else {
            sb.append(this._contextClass.getName());
        }
        sb.append(": ").append(this._bindings).append("]");
        return sb.toString();
    }
}
