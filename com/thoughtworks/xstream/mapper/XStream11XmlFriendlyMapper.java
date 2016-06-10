package com.thoughtworks.xstream.mapper;

public class XStream11XmlFriendlyMapper extends AbstractXmlFriendlyMapper {
    public XStream11XmlFriendlyMapper(Mapper wrapped) {
        super(wrapped);
    }

    public Class realClass(String elementName) {
        return super.realClass(unescapeClassName(elementName));
    }

    public String realMember(Class type, String serialized) {
        return unescapeFieldName(super.realMember(type, serialized));
    }

    public String mapNameFromXML(String xmlName) {
        return unescapeFieldName(xmlName);
    }
}
