package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.naming.NameCoder;
import nu.xom.Attribute;
import nu.xom.Element;

public class XomWriter extends AbstractDocumentWriter {
    public XomWriter() {
        this(null);
    }

    public XomWriter(Element parentElement) {
        this(parentElement, new XmlFriendlyNameCoder());
    }

    public XomWriter(Element parentElement, NameCoder nameCoder) {
        super((Object) parentElement, nameCoder);
    }

    public XomWriter(Element parentElement, XmlFriendlyReplacer replacer) {
        this(parentElement, (NameCoder) replacer);
    }

    protected Object createNode(String name) {
        Element newNode = new Element(encodeNode(name));
        if (top() != null) {
            top().appendChild(newNode);
        }
        return newNode;
    }

    public void addAttribute(String name, String value) {
        top().addAttribute(new Attribute(encodeAttribute(name), value));
    }

    public void setValue(String text) {
        top().appendChild(text);
    }

    private Element top() {
        return (Element) getCurrent();
    }
}
