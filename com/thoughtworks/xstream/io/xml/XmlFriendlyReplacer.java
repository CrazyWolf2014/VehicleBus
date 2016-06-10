package com.thoughtworks.xstream.io.xml;

public class XmlFriendlyReplacer extends XmlFriendlyNameCoder {
    public XmlFriendlyReplacer() {
        this("_-", "__");
    }

    public XmlFriendlyReplacer(String dollarReplacement, String underscoreReplacement) {
        super(dollarReplacement, underscoreReplacement);
    }

    public String escapeName(String name) {
        return super.encodeNode(name);
    }

    public String unescapeName(String name) {
        return super.decodeNode(name);
    }
}
