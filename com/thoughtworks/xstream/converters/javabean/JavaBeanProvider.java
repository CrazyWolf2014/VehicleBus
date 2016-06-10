package com.thoughtworks.xstream.converters.javabean;

public interface JavaBeanProvider {

    public interface Visitor {
        boolean shouldVisit(String str, Class cls);

        void visit(String str, Class cls, Class cls2, Object obj);
    }

    boolean canInstantiate(Class cls);

    Class getPropertyType(Object obj, String str);

    Object newInstance(Class cls);

    boolean propertyDefinedInClass(String str, Class cls);

    void visitSerializableProperties(Object obj, Visitor visitor);

    void writeProperty(Object obj, String str, Object obj2);
}
