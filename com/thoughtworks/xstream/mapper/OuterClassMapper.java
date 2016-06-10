package com.thoughtworks.xstream.mapper;

public class OuterClassMapper extends MapperWrapper {
    private final String alias;

    public OuterClassMapper(Mapper wrapped) {
        this(wrapped, "outer-class");
    }

    public OuterClassMapper(Mapper wrapped, String alias) {
        super(wrapped);
        this.alias = alias;
    }

    public String serializedMember(Class type, String memberName) {
        if (memberName.equals("this$0")) {
            return this.alias;
        }
        return super.serializedMember(type, memberName);
    }

    public String realMember(Class type, String serialized) {
        if (serialized.equals(this.alias)) {
            return "this$0";
        }
        return super.realMember(type, serialized);
    }
}
