package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.io.File;
import java.io.FilterWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import org.dom4j.DocumentFactory;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4JDriver extends AbstractXmlDriver {
    private DocumentFactory documentFactory;
    private OutputFormat outputFormat;

    /* renamed from: com.thoughtworks.xstream.io.xml.Dom4JDriver.1 */
    class C09031 extends FilterWriter {
        final /* synthetic */ HierarchicalStreamWriter[] val$writer;

        C09031(Writer x0, HierarchicalStreamWriter[] hierarchicalStreamWriterArr) {
            this.val$writer = hierarchicalStreamWriterArr;
            super(x0);
        }

        public void close() {
            this.val$writer[0].close();
        }
    }

    public Dom4JDriver() {
        this(new XmlFriendlyNameCoder());
    }

    public Dom4JDriver(NameCoder nameCoder) {
        this(new DocumentFactory(), OutputFormat.createPrettyPrint(), nameCoder);
        this.outputFormat.setTrimText(false);
    }

    public Dom4JDriver(DocumentFactory documentFactory, OutputFormat outputFormat) {
        this(documentFactory, outputFormat, new XmlFriendlyNameCoder());
    }

    public Dom4JDriver(DocumentFactory documentFactory, OutputFormat outputFormat, NameCoder nameCoder) {
        super(nameCoder);
        this.documentFactory = documentFactory;
        this.outputFormat = outputFormat;
    }

    public Dom4JDriver(DocumentFactory documentFactory, OutputFormat outputFormat, XmlFriendlyReplacer replacer) {
        this(documentFactory, outputFormat, (NameCoder) replacer);
    }

    public DocumentFactory getDocumentFactory() {
        return this.documentFactory;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    public OutputFormat getOutputFormat() {
        return this.outputFormat;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public HierarchicalStreamReader createReader(Reader text) {
        try {
            return new Dom4JReader(new SAXReader().read(text), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            return new Dom4JReader(new SAXReader().read(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(URL in) {
        try {
            return new Dom4JReader(new SAXReader().read(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(File in) {
        try {
            return new Dom4JReader(new SAXReader().read(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
        return new HierarchicalStreamWriter[]{new Dom4JXmlWriter(new XMLWriter(new C09031(out, writer), this.outputFormat), getNameCoder())}[0];
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        return createWriter(new OutputStreamWriter(out));
    }
}
