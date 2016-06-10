package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.xbill.DNS.KEYRecord.Flags;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xmlpull.v1.XmlPullParser;

public final class SaxWriter extends AbstractXmlWriter implements XMLReader {
    public static final String CONFIGURED_XSTREAM_PROPERTY = "http://com.thoughtworks.xstream/sax/property/configured-xstream";
    public static final String SOURCE_OBJECT_LIST_PROPERTY = "http://com.thoughtworks.xstream/sax/property/source-object-list";
    private final AttributesImpl attributeList;
    private char[] buffer;
    private ContentHandler contentHandler;
    private int depth;
    private DTDHandler dtdHandler;
    private List elementStack;
    private EntityResolver entityResolver;
    private ErrorHandler errorHandler;
    private Map features;
    private final boolean includeEnclosingDocument;
    private final Map properties;
    private boolean startTagInProgress;

    public SaxWriter(NameCoder nameCoder) {
        this(true, nameCoder);
    }

    public SaxWriter(boolean includeEnclosingDocument, NameCoder nameCoder) {
        super(nameCoder);
        this.entityResolver = null;
        this.dtdHandler = null;
        this.contentHandler = null;
        this.errorHandler = null;
        this.features = new HashMap();
        this.properties = new HashMap();
        this.depth = 0;
        this.elementStack = new LinkedList();
        this.buffer = new char[Flags.FLAG8];
        this.startTagInProgress = false;
        this.attributeList = new AttributesImpl();
        this.includeEnclosingDocument = includeEnclosingDocument;
    }

    public SaxWriter(XmlFriendlyReplacer replacer) {
        this(true, replacer);
    }

    public SaxWriter(boolean includeEnclosingDocument, XmlFriendlyReplacer replacer) {
        this(includeEnclosingDocument, (NameCoder) replacer);
    }

    public SaxWriter(boolean includeEnclosingDocument) {
        this(includeEnclosingDocument, new XmlFriendlyNameCoder());
    }

    public SaxWriter() {
        this(true);
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
        if (name.equals("http://xml.org/sax/features/namespaces") || name.equals("http://xml.org/sax/features/namespace-prefixes")) {
            this.features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
            return;
        }
        throw new SAXNotRecognizedException(name);
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException {
        if (name.equals("http://xml.org/sax/features/namespaces") || name.equals("http://xml.org/sax/features/namespace-prefixes")) {
            Boolean value = (Boolean) this.features.get(name);
            if (value == null) {
                value = Boolean.FALSE;
            }
            return value.booleanValue();
        }
        throw new SAXNotRecognizedException(name);
    }

    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals(CONFIGURED_XSTREAM_PROPERTY)) {
            if (!(value instanceof XStream)) {
                throw new SAXNotSupportedException("Value for property \"http://com.thoughtworks.xstream/sax/property/configured-xstream\" must be a non-null XStream object");
            }
        } else if (!name.equals(SOURCE_OBJECT_LIST_PROPERTY)) {
            throw new SAXNotRecognizedException(name);
        } else if (value instanceof List) {
            List list = (List) value;
            if (list.isEmpty()) {
                throw new SAXNotSupportedException("Value for property \"http://com.thoughtworks.xstream/sax/property/source-object-list\" shall not be an empty list");
            }
            value = Collections.unmodifiableList(new ArrayList(list));
        } else {
            throw new SAXNotSupportedException("Value for property \"http://com.thoughtworks.xstream/sax/property/source-object-list\" must be a non-null List object");
        }
        this.properties.put(name, value);
    }

    public Object getProperty(String name) throws SAXNotRecognizedException {
        if (name.equals(CONFIGURED_XSTREAM_PROPERTY) || name.equals(SOURCE_OBJECT_LIST_PROPERTY)) {
            return this.properties.get(name);
        }
        throw new SAXNotRecognizedException(name);
    }

    public void setEntityResolver(EntityResolver resolver) {
        if (resolver == null) {
            throw new NullPointerException("resolver");
        }
        this.entityResolver = resolver;
    }

    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }

    public void setDTDHandler(DTDHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        this.dtdHandler = handler;
    }

    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }

    public void setContentHandler(ContentHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        this.contentHandler = handler;
    }

    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }

    public void setErrorHandler(ErrorHandler handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        this.errorHandler = handler;
    }

    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }

    public void parse(String systemId) throws SAXException {
        parse();
    }

    public void parse(InputSource input) throws SAXException {
        parse();
    }

    private void parse() throws SAXException {
        XStream xstream = (XStream) this.properties.get(CONFIGURED_XSTREAM_PROPERTY);
        if (xstream == null) {
            xstream = new XStream();
        }
        List<Object> source = (List) this.properties.get(SOURCE_OBJECT_LIST_PROPERTY);
        if (source == null || source.isEmpty()) {
            throw new SAXException("Missing or empty source object list. Setting property \"http://com.thoughtworks.xstream/sax/property/source-object-list\" is mandatory");
        }
        try {
            startDocument(true);
            for (Object marshal : source) {
                xstream.marshal(marshal, this);
            }
            endDocument(true);
        } catch (StreamException e) {
            if (e.getCause() instanceof SAXException) {
                throw ((SAXException) e.getCause());
            }
            throw new SAXException(e);
        }
    }

    public void startNode(String name) {
        try {
            if (this.depth != 0) {
                flushStartTag();
            } else if (this.includeEnclosingDocument) {
                startDocument(false);
            }
            this.elementStack.add(0, escapeXmlName(name));
            this.startTagInProgress = true;
            this.depth++;
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public void addAttribute(String name, String value) {
        if (this.startTagInProgress) {
            String escapedName = escapeXmlName(name);
            this.attributeList.addAttribute(XmlPullParser.NO_NAMESPACE, escapedName, escapedName, "CDATA", value);
            return;
        }
        throw new StreamException(new IllegalStateException("No startElement being processed"));
    }

    public void setValue(String text) {
        try {
            flushStartTag();
            int lg = text.length();
            if (lg > this.buffer.length) {
                this.buffer = new char[lg];
            }
            text.getChars(0, lg, this.buffer, 0);
            this.contentHandler.characters(this.buffer, 0, lg);
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public void endNode() {
        try {
            flushStartTag();
            String tagName = (String) this.elementStack.remove(0);
            this.contentHandler.endElement(XmlPullParser.NO_NAMESPACE, tagName, tagName);
            this.depth--;
            if (this.depth == 0 && this.includeEnclosingDocument) {
                endDocument(false);
            }
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    private void startDocument(boolean multiObjectMode) throws SAXException {
        if (this.depth == 0) {
            this.contentHandler.startDocument();
            if (multiObjectMode) {
                this.depth++;
            }
        }
    }

    private void endDocument(boolean multiObjectMode) throws SAXException {
        if (this.depth == 0 || (this.depth == 1 && multiObjectMode)) {
            this.contentHandler.endDocument();
            this.depth = 0;
        }
    }

    private void flushStartTag() throws SAXException {
        if (this.startTagInProgress) {
            String tagName = (String) this.elementStack.get(0);
            this.contentHandler.startElement(XmlPullParser.NO_NAMESPACE, tagName, tagName, this.attributeList);
            this.attributeList.clear();
            this.startTagInProgress = false;
        }
    }

    public void flush() {
    }

    public void close() {
    }
}
