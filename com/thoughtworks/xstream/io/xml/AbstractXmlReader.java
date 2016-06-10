package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.AbstractReader;
import com.thoughtworks.xstream.io.naming.NameCoder;

public abstract class AbstractXmlReader extends AbstractReader {
    protected AbstractXmlReader() {
        this(new XmlFriendlyNameCoder());
    }

    protected AbstractXmlReader(XmlFriendlyReplacer replacer) {
        this((NameCoder) replacer);
    }

    protected AbstractXmlReader(NameCoder nameCoder) {
        super(nameCoder);
    }

    public String unescapeXmlName(String name) {
        return decodeNode(name);
    }

    protected String escapeXmlName(String name) {
        return encodeNode(name);
    }
}
