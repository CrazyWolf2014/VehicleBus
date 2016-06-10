package com.thoughtworks.xstream.mapper;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAttributeAliasingMapper extends MapperWrapper {
    protected final Map aliasToName;
    protected transient Map nameToAlias;

    public AbstractAttributeAliasingMapper(Mapper wrapped) {
        super(wrapped);
        this.aliasToName = new HashMap();
        this.nameToAlias = new HashMap();
    }

    public void addAliasFor(String attributeName, String alias) {
        this.aliasToName.put(alias, attributeName);
        this.nameToAlias.put(attributeName, alias);
    }

    private Object readResolve() {
        this.nameToAlias = new HashMap();
        for (Object alias : this.aliasToName.keySet()) {
            this.nameToAlias.put(this.aliasToName.get(alias), alias);
        }
        return this;
    }
}
