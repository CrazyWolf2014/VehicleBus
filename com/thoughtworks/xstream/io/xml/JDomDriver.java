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
import org.jdom.input.SAXBuilder;

public class JDomDriver extends AbstractXmlDriver {
    public JDomDriver() {
        super(new XmlFriendlyNameCoder());
    }

    public JDomDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    public JDomDriver(XmlFriendlyReplacer replacer) {
        this((NameCoder) replacer);
    }

    public HierarchicalStreamReader createReader(Reader reader) {
        try {
            return new JDomReader(new SAXBuilder().build(reader), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            return new JDomReader(new SAXBuilder().build(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamReader createReader(URL in) {
        try {
            return new JDomReader(new SAXBuilder().build(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamReader createReader(File in) {
        try {
            return new JDomReader(new SAXBuilder().build(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out, getNameCoder());
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        return new PrettyPrintWriter(new OutputStreamWriter(out));
    }
}
