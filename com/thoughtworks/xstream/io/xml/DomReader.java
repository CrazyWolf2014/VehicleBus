package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.naming.NameCoder;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DomReader extends AbstractDocumentReader {
    private List childElements;
    private Element currentElement;
    private StringBuffer textBuffer;

    public DomReader(Element rootElement) {
        this(rootElement, new XmlFriendlyNameCoder());
    }

    public DomReader(Document document) {
        this(document.getDocumentElement());
    }

    public DomReader(Element rootElement, NameCoder nameCoder) {
        super((Object) rootElement, nameCoder);
        this.textBuffer = new StringBuffer();
    }

    public DomReader(Document document, NameCoder nameCoder) {
        this(document.getDocumentElement(), nameCoder);
    }

    public DomReader(Element rootElement, XmlFriendlyReplacer replacer) {
        this(rootElement, (NameCoder) replacer);
    }

    public DomReader(Document document, XmlFriendlyReplacer replacer) {
        this(document.getDocumentElement(), (NameCoder) replacer);
    }

    public String getNodeName() {
        return decodeNode(this.currentElement.getTagName());
    }

    public String getValue() {
        NodeList childNodes = this.currentElement.getChildNodes();
        this.textBuffer.setLength(0);
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
            Node childNode = childNodes.item(i);
            if (childNode instanceof Text) {
                this.textBuffer.append(((Text) childNode).getData());
            }
        }
        return this.textBuffer.toString();
    }

    public String getAttribute(String name) {
        Attr attribute = this.currentElement.getAttributeNode(encodeAttribute(name));
        return attribute == null ? null : attribute.getValue();
    }

    public String getAttribute(int index) {
        return ((Attr) this.currentElement.getAttributes().item(index)).getValue();
    }

    public int getAttributeCount() {
        return this.currentElement.getAttributes().getLength();
    }

    public String getAttributeName(int index) {
        return decodeAttribute(((Attr) this.currentElement.getAttributes().item(index)).getName());
    }

    protected Object getParent() {
        return this.currentElement.getParentNode();
    }

    protected Object getChild(int index) {
        return this.childElements.get(index);
    }

    protected int getChildCount() {
        return this.childElements.size();
    }

    protected void reassignCurrentElement(Object current) {
        this.currentElement = (Element) current;
        NodeList childNodes = this.currentElement.getChildNodes();
        this.childElements = new ArrayList();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                this.childElements.add(node);
            }
        }
    }

    public String peekNextChild() {
        NodeList childNodes = this.currentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node instanceof Element) {
                return decodeNode(((Element) node).getTagName());
            }
        }
        return null;
    }
}
