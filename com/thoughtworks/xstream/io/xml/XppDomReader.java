package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.xppdom.XppDom;
import org.xmlpull.v1.XmlPullParser;

public class XppDomReader extends AbstractDocumentReader {
    private XppDom currentElement;

    public XppDomReader(XppDom xppDom) {
        super(xppDom);
    }

    public XppDomReader(XppDom xppDom, NameCoder nameCoder) {
        super((Object) xppDom, nameCoder);
    }

    public XppDomReader(XppDom xppDom, XmlFriendlyReplacer replacer) {
        this(xppDom, (NameCoder) replacer);
    }

    public String getNodeName() {
        return decodeNode(this.currentElement.getName());
    }

    public String getValue() {
        String text = null;
        try {
            text = this.currentElement.getValue();
        } catch (Exception e) {
        }
        return text == null ? XmlPullParser.NO_NAMESPACE : text;
    }

    public String getAttribute(String attributeName) {
        return this.currentElement.getAttribute(encodeAttribute(attributeName));
    }

    public String getAttribute(int index) {
        return this.currentElement.getAttribute(this.currentElement.getAttributeNames()[index]);
    }

    public int getAttributeCount() {
        return this.currentElement.getAttributeNames().length;
    }

    public String getAttributeName(int index) {
        return decodeAttribute(this.currentElement.getAttributeNames()[index]);
    }

    protected Object getParent() {
        return this.currentElement.getParent();
    }

    protected Object getChild(int index) {
        return this.currentElement.getChild(index);
    }

    protected int getChildCount() {
        return this.currentElement.getChildCount();
    }

    protected void reassignCurrentElement(Object current) {
        this.currentElement = (XppDom) current;
    }

    public String peekNextChild() {
        if (this.currentElement.getChildCount() == 0) {
            return null;
        }
        return decodeNode(this.currentElement.getChild(0).getName());
    }
}
