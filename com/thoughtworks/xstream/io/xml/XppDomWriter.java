package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.xppdom.XppDom;

public class XppDomWriter extends AbstractDocumentWriter {
    public XppDomWriter() {
        this(null, new XmlFriendlyNameCoder());
    }

    public XppDomWriter(XppDom parent) {
        this(parent, new XmlFriendlyNameCoder());
    }

    public XppDomWriter(NameCoder nameCoder) {
        this(null, nameCoder);
    }

    public XppDomWriter(XppDom parent, NameCoder nameCoder) {
        super((Object) parent, nameCoder);
    }

    public XppDomWriter(XmlFriendlyReplacer replacer) {
        this(null, replacer);
    }

    public XppDomWriter(XppDom parent, XmlFriendlyReplacer replacer) {
        this(parent, (NameCoder) replacer);
    }

    public XppDom getConfiguration() {
        return (XppDom) getTopLevelNodes().get(0);
    }

    protected Object createNode(String name) {
        XppDom newNode = new XppDom(encodeNode(name));
        if (top() != null) {
            top().addChild(newNode);
        }
        return newNode;
    }

    public void setValue(String text) {
        top().setValue(text);
    }

    public void addAttribute(String key, String value) {
        top().setAttribute(encodeAttribute(key), value);
    }

    private XppDom top() {
        return (XppDom) getCurrent();
    }
}
