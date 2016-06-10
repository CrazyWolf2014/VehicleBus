package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xmlpull.v1.XmlPullParser;

public class Dom4JXmlWriter extends AbstractXmlWriter {
    private AttributesImpl attributes;
    private boolean children;
    private final FastStack elementStack;
    private boolean started;
    private final XMLWriter writer;

    public Dom4JXmlWriter(XMLWriter writer) {
        this(writer, new XmlFriendlyNameCoder());
    }

    public Dom4JXmlWriter(XMLWriter writer, NameCoder nameCoder) {
        super(nameCoder);
        this.writer = writer;
        this.elementStack = new FastStack(16);
        this.attributes = new AttributesImpl();
        try {
            writer.startDocument();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public Dom4JXmlWriter(XMLWriter writer, XmlFriendlyReplacer replacer) {
        this(writer, (NameCoder) replacer);
    }

    public void startNode(String name) {
        if (this.elementStack.size() > 0) {
            try {
                startElement();
                this.started = false;
            } catch (Throwable e) {
                throw new StreamException(e);
            }
        }
        this.elementStack.push(encodeNode(name));
        this.children = false;
    }

    public void setValue(String text) {
        char[] value = text.toCharArray();
        if (value.length > 0) {
            try {
                startElement();
                this.writer.characters(value, 0, value.length);
                this.children = true;
            } catch (Throwable e) {
                throw new StreamException(e);
            }
        }
    }

    public void addAttribute(String key, String value) {
        this.attributes.addAttribute(XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, encodeAttribute(key), "string", value);
    }

    public void endNode() {
        try {
            if (this.children) {
                startElement();
                this.writer.endElement(XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, (String) this.elementStack.pop());
                return;
            }
            Element element = new DefaultElement((String) this.elementStack.pop());
            for (int i = 0; i < this.attributes.getLength(); i++) {
                element.addAttribute(this.attributes.getQName(i), this.attributes.getValue(i));
            }
            this.writer.write(element);
            this.attributes.clear();
            this.children = true;
            this.started = true;
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public void flush() {
        try {
            this.writer.flush();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public void close() {
        try {
            this.writer.endDocument();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    private void startElement() throws SAXException {
        if (!this.started) {
            this.writer.startElement(XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, (String) this.elementStack.peek(), this.attributes);
            this.attributes.clear();
            this.started = true;
        }
    }
}
