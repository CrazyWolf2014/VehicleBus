package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

public class DomDriver extends AbstractXmlDriver {
    private final DocumentBuilderFactory documentBuilderFactory;
    private final String encoding;

    public DomDriver() {
        this(null);
    }

    public DomDriver(String encoding) {
        this(encoding, new XmlFriendlyNameCoder());
    }

    public DomDriver(String encoding, NameCoder nameCoder) {
        super(nameCoder);
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.encoding = encoding;
    }

    public DomDriver(String encoding, XmlFriendlyReplacer replacer) {
        this(encoding, (NameCoder) replacer);
    }

    public HierarchicalStreamReader createReader(Reader in) {
        return createReader(new InputSource(in));
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        return createReader(new InputSource(in));
    }

    public HierarchicalStreamReader createReader(URL in) {
        return createReader(new InputSource(in.toExternalForm()));
    }

    public HierarchicalStreamReader createReader(File in) {
        return createReader(new InputSource(in.toURI().toASCIIString()));
    }

    private HierarchicalStreamReader createReader(InputSource source) {
        try {
            DocumentBuilder documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
            if (this.encoding != null) {
                source.setEncoding(this.encoding);
            }
            return new DomReader(documentBuilder.parse(source), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        } catch (Throwable e22) {
            throw new StreamException(e22);
        } catch (Throwable e222) {
            throw new StreamException(e222);
        }
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out, getNameCoder());
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        try {
            return createWriter(this.encoding != null ? new OutputStreamWriter(out, this.encoding) : new OutputStreamWriter(out));
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }
}
