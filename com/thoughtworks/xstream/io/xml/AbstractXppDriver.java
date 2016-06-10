package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.core.util.XmlHeaderAwareReader;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class AbstractXppDriver extends AbstractXmlDriver {
    protected abstract XmlPullParser createParser() throws XmlPullParserException;

    public AbstractXppDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    public HierarchicalStreamReader createReader(Reader in) {
        try {
            return new XppReader(in, createParser(), getNameCoder());
        } catch (XmlPullParserException e) {
            throw new StreamException("Cannot create XmlPullParser");
        }
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        try {
            return createReader(new XmlHeaderAwareReader(in));
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
        return createWriter(new OutputStreamWriter(out));
    }
}
