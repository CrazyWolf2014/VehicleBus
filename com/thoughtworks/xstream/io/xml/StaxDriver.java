package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.ReaderWrapper;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

public class StaxDriver extends AbstractXmlDriver {
    private XMLInputFactory inputFactory;
    private XMLOutputFactory outputFactory;
    private QNameMap qnameMap;

    /* renamed from: com.thoughtworks.xstream.io.xml.StaxDriver.1 */
    class C13021 extends ReaderWrapper {
        final /* synthetic */ InputStream val$stream;

        C13021(HierarchicalStreamReader x0, InputStream inputStream) {
            this.val$stream = inputStream;
            super(x0);
        }

        public void close() {
            super.close();
            try {
                this.val$stream.close();
            } catch (IOException e) {
            }
        }
    }

    /* renamed from: com.thoughtworks.xstream.io.xml.StaxDriver.2 */
    class C13032 extends ReaderWrapper {
        final /* synthetic */ InputStream val$stream;

        C13032(HierarchicalStreamReader x0, InputStream inputStream) {
            this.val$stream = inputStream;
            super(x0);
        }

        public void close() {
            super.close();
            try {
                this.val$stream.close();
            } catch (IOException e) {
            }
        }
    }

    public StaxDriver() {
        this(new QNameMap());
    }

    public StaxDriver(QNameMap qnameMap) {
        this(qnameMap, new XmlFriendlyNameCoder());
    }

    public StaxDriver(QNameMap qnameMap, NameCoder nameCoder) {
        super(nameCoder);
        this.qnameMap = qnameMap;
    }

    public StaxDriver(NameCoder nameCoder) {
        this(new QNameMap(), nameCoder);
    }

    public StaxDriver(QNameMap qnameMap, XmlFriendlyReplacer replacer) {
        this(qnameMap, (NameCoder) replacer);
    }

    public StaxDriver(XmlFriendlyReplacer replacer) {
        this(new QNameMap(), (NameCoder) replacer);
    }

    public HierarchicalStreamReader createReader(Reader xml) {
        try {
            return createStaxReader(createParser(xml));
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            return createStaxReader(createParser(in));
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(URL in) {
        try {
            InputStream stream = in.openStream();
            return new C13021(createStaxReader(createParser(new StreamSource(stream, in.toExternalForm()))), stream);
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamReader createReader(File in) {
        try {
            InputStream stream = new FileInputStream(in);
            return new C13032(createStaxReader(createParser(new StreamSource(stream, in.toURI().toASCIIString()))), stream);
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
        try {
            return createStaxWriter(getOutputFactory().createXMLStreamWriter(out));
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        try {
            return createStaxWriter(getOutputFactory().createXMLStreamWriter(out));
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public AbstractPullReader createStaxReader(XMLStreamReader in) {
        return new StaxReader(this.qnameMap, in, getNameCoder());
    }

    public StaxWriter createStaxWriter(XMLStreamWriter out, boolean writeStartEndDocument) throws XMLStreamException {
        return new StaxWriter(this.qnameMap, out, writeStartEndDocument, isRepairingNamespace(), getNameCoder());
    }

    public StaxWriter createStaxWriter(XMLStreamWriter out) throws XMLStreamException {
        return createStaxWriter(out, true);
    }

    public QNameMap getQnameMap() {
        return this.qnameMap;
    }

    public void setQnameMap(QNameMap qnameMap) {
        this.qnameMap = qnameMap;
    }

    public XMLInputFactory getInputFactory() {
        if (this.inputFactory == null) {
            this.inputFactory = createInputFactory();
        }
        return this.inputFactory;
    }

    public XMLOutputFactory getOutputFactory() {
        if (this.outputFactory == null) {
            this.outputFactory = createOutputFactory();
        }
        return this.outputFactory;
    }

    public boolean isRepairingNamespace() {
        return Boolean.TRUE.equals(getOutputFactory().getProperty("javax.xml.stream.isRepairingNamespaces"));
    }

    public void setRepairingNamespace(boolean repairing) {
        getOutputFactory().setProperty("javax.xml.stream.isRepairingNamespaces", repairing ? Boolean.TRUE : Boolean.FALSE);
    }

    protected XMLStreamReader createParser(Reader xml) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(xml);
    }

    protected XMLStreamReader createParser(InputStream xml) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(xml);
    }

    protected XMLStreamReader createParser(Source source) throws XMLStreamException {
        return getInputFactory().createXMLStreamReader(source);
    }

    protected XMLInputFactory createInputFactory() {
        return XMLInputFactory.newInstance();
    }

    protected XMLOutputFactory createOutputFactory() {
        return XMLOutputFactory.newInstance();
    }
}
