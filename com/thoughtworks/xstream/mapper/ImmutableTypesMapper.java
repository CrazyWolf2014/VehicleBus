package com.thoughtworks.xstream.mapper;

import java.util.HashSet;
import java.util.Set;

public class ImmutableTypesMapper extends MapperWrapper {
    private final Set immutableTypes;

    public ImmutableTypesMapper(Mapper wrapped) {
        super(wrapped);
        this.immutableTypes = new HashSet();
    }

    public void addImmutableType(Class type) {
        this.immutableTypes.add(type);
    }

    public boolean isImmutableValueType(Class type) {
        if (this.immutableTypes.contains(type)) {
            return true;
        }
        return super.isImmutableValueType(type);
    }
}
