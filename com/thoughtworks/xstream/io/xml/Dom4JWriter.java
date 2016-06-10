package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.naming.NameCoder;
import org.dom4j.Branch;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

public class Dom4JWriter extends AbstractDocumentWriter {
    private final DocumentFactory documentFactory;

    public Dom4JWriter(Branch root, DocumentFactory factory, NameCoder nameCoder) {
        super((Object) root, nameCoder);
        this.documentFactory = factory;
    }

    public Dom4JWriter(DocumentFactory factory, NameCoder nameCoder) {
        this(null, factory, nameCoder);
    }

    public Dom4JWriter(Branch root, NameCoder nameCoder) {
        this(root, new DocumentFactory(), nameCoder);
    }

    public Dom4JWriter(Branch root, DocumentFactory factory, XmlFriendlyReplacer replacer) {
        this(root, factory, (NameCoder) replacer);
    }

    public Dom4JWriter(DocumentFactory factory, XmlFriendlyReplacer replacer) {
        this(null, factory, (NameCoder) replacer);
    }

    public Dom4JWriter(DocumentFactory documentFactory) {
        this(documentFactory, new XmlFriendlyNameCoder());
    }

    public Dom4JWriter(Branch root, XmlFriendlyReplacer replacer) {
        this(root, new DocumentFactory(), (NameCoder) replacer);
    }

    public Dom4JWriter(Branch root) {
        this(root, new DocumentFactory(), new XmlFriendlyNameCoder());
    }

    public Dom4JWriter() {
        this(new DocumentFactory(), new XmlFriendlyNameCoder());
    }

    protected Object createNode(String name) {
        Element element = this.documentFactory.createElement(encodeNode(name));
        if (top() != null) {
            top().add(element);
        }
        return element;
    }

    public void setValue(String text) {
        top().setText(text);
    }

    public void addAttribute(String key, String value) {
        ((Element) top()).addAttribute(encodeAttribute(key), value);
    }

    private Branch top() {
        return (Branch) getCurrent();
    }
}
