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
import nu.xom.Builder;

public class XomDriver extends AbstractXmlDriver {
    private final Builder builder;

    public XomDriver() {
        this(new Builder());
    }

    public XomDriver(Builder builder) {
        this(builder, new XmlFriendlyNameCoder());
    }

    public XomDriver(NameCoder nameCoder) {
        this(new Builder(), nameCoder);
    }

    public XomDriver(Builder builder, NameCoder nameCoder) {
        super(nameCoder);
        this.builder = builder;
    }

    public XomDriver(XmlFriendlyReplacer replacer) {
        this(new Builder(), replacer);
    }

    public XomDriver(Builder builder, XmlFriendlyReplacer replacer) {
        this((NameCoder) replacer);
    }

    protected Builder getBuilder() {
        return this.builder;
    }

    public HierarchicalStreamReader createReader(Reader text) {
        try {
            return new XomReader(this.builder.build(text), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        } catch (Throwable e22) {
            throw new StreamException(e22);
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            return new XomReader(this.builder.build(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        } catch (Throwable e22) {
            throw new StreamException(e22);
        }
    }

    public HierarchicalStreamReader createReader(URL in) {
        try {
            return new XomReader(this.builder.build(in.toExternalForm()), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        } catch (Throwable e22) {
            throw new StreamException(e22);
        }
    }

    public HierarchicalStreamReader createReader(File in) {
        try {
            return new XomReader(this.builder.build(in), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        } catch (Throwable e22) {
            throw new StreamException(e22);
        }
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
        return new PrettyPrintWriter(out, getNameCoder());
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        return new PrettyPrintWriter(new OutputStreamWriter(out), getNameCoder());
    }
}
