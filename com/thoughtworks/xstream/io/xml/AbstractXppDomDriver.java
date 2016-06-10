package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.core.util.XmlHeaderAwareReader;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.xppdom.XppDom;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class AbstractXppDomDriver extends AbstractXmlDriver {
    protected abstract XmlPullParser createParser() throws XmlPullParserException;

    public AbstractXppDomDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    public HierarchicalStreamReader createReader(Reader in) {
        try {
            XmlPullParser parser = createParser();
            parser.setInput(in);
            return new XppDomReader(XppDom.build(parser), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
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
