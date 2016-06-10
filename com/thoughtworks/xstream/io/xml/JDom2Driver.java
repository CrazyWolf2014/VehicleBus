package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.AbstractDriver;
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
import org.jdom2.input.SAXBuilder;

public class JDom2Driver extends AbstractDriver {
    public JDom2Driver() {
        super(new XmlFriendlyNameCoder());
    }

    public JDom2Driver(NameCoder nameCoder) {
        super(nameCoder);
    }

    public HierarchicalStreamReader createReader(Reader reader) {
        try {
            return new JDom2Reader(new SAXBuilder().build(reader), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            return new JDom2Reader(new SAXBuilder().build(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamReader createReader(URL in) {
        try {
            return new JDom2Reader(new SAXBuilder().build(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    public HierarchicalStreamReader createReader(File in) {
        try {
            return new JDom2Reader(new SAXBuilder().build(in), getNameCoder());
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
