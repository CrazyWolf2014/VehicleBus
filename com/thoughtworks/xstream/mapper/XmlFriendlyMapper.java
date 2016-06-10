package com.thoughtworks.xstream.mapper;

public class XmlFriendlyMapper extends AbstractXmlFriendlyMapper {
    public XmlFriendlyMapper(Mapper wrapped) {
        super(wrapped);
    }

    public String serializedClass(Class type) {
        return escapeClassName(super.serializedClass(type));
    }

    public Class realClass(String elementName) {
        return super.realClass(unescapeClassName(elementName));
    }

    public String serializedMember(Class type, String memberName) {
        return escapeFieldName(super.serializedMember(type, memberName));
    }

    public String realMember(Class type, String serialized) {
        return unescapeFieldName(super.realMember(type, serialized));
    }

    public String mapNameToXML(String javaName) {
        return escapeFieldName(javaName);
    }

    public String mapNameFromXML(String xmlName) {
        return unescapeFieldName(xmlName);
    }
}
