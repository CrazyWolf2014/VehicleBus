package com.thoughtworks.xstream.mapper;

import com.thoughtworks.xstream.core.util.Primitives;
import java.util.HashMap;
import java.util.Map;

public class ClassAliasingMapper extends MapperWrapper {
    private final Map classToName;
    private transient Map nameToType;
    private final Map typeToName;

    public ClassAliasingMapper(Mapper wrapped) {
        super(wrapped);
        this.typeToName = new HashMap();
        this.classToName = new HashMap();
        this.nameToType = new HashMap();
    }

    public void addClassAlias(String name, Class type) {
        this.nameToType.put(name, type.getName());
        this.classToName.put(type.getName(), name);
    }

    public void addClassAttributeAlias(String name, Class type) {
        addClassAlias(name, type);
    }

    public void addTypeAlias(String name, Class type) {
        this.nameToType.put(name, type.getName());
        this.typeToName.put(type, name);
    }

    public String serializedClass(Class type) {
        String alias = (String) this.classToName.get(type.getName());
        if (alias != null) {
            return alias;
        }
        for (Class compatibleType : this.typeToName.keySet()) {
            if (compatibleType.isAssignableFrom(type)) {
                return (String) this.typeToName.get(compatibleType);
            }
        }
        return super.serializedClass(type);
    }

    public Class realClass(String elementName) {
        String mappedName = (String) this.nameToType.get(elementName);
        if (mappedName != null) {
            Class type = Primitives.primitiveType(mappedName);
            if (type != null) {
                return type;
            }
            elementName = mappedName;
        }
        return super.realClass(elementName);
    }

    public boolean itemTypeAsAttribute(Class clazz) {
        return this.classToName.containsKey(clazz);
    }

    public boolean aliasIsAttribute(String name) {
        return this.nameToType.containsKey(name);
    }

    private Object readResolve() {
        this.nameToType = new HashMap();
        for (Object type : this.classToName.keySet()) {
            this.nameToType.put(this.classToName.get(type), type);
        }
        for (Class type2 : this.typeToName.keySet()) {
            this.nameToType.put(this.typeToName.get(type2), type2.getName());
        }
        return this;
    }
}
