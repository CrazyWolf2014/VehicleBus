package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.naming.NameCoder;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

public class JDomReader extends AbstractDocumentReader {
    private Element currentElement;

    public JDomReader(Element root) {
        super(root);
    }

    public JDomReader(Document document) {
        super(document.getRootElement());
    }

    public JDomReader(Element root, NameCoder nameCoder) {
        super((Object) root, nameCoder);
    }

    public JDomReader(Document document, NameCoder nameCoder) {
        super(document.getRootElement(), nameCoder);
    }

    public JDomReader(Element root, XmlFriendlyReplacer replacer) {
        this(root, (NameCoder) replacer);
    }

    public JDomReader(Document document, XmlFriendlyReplacer replacer) {
        this(document.getRootElement(), (NameCoder) replacer);
    }

    protected void reassignCurrentElement(Object current) {
        this.currentElement = (Element) current;
    }

    protected Object getParent() {
        return this.currentElement.getParentElement();
    }

    protected Object getChild(int index) {
        return this.currentElement.getChildren().get(index);
    }

    protected int getChildCount() {
        return this.currentElement.getChildren().size();
    }

    public String getNodeName() {
        return decodeNode(this.currentElement.getName());
    }

    public String getValue() {
        return this.currentElement.getText();
    }

    public String getAttribute(String name) {
        return this.currentElement.getAttributeValue(encodeAttribute(name));
    }

    public String getAttribute(int index) {
        return ((Attribute) this.currentElement.getAttributes().get(index)).getValue();
    }

    public int getAttributeCount() {
        return this.currentElement.getAttributes().size();
    }

    public String getAttributeName(int index) {
        return decodeAttribute(((Attribute) this.currentElement.getAttributes().get(index)).getQualifiedName());
    }

    public String peekNextChild() {
        List list = this.currentElement.getChildren();
        if (list == null || list.isEmpty()) {
            return null;
        }
        return decodeNode(((Element) list.get(0)).getName());
    }
}
