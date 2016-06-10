package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.naming.NameCoder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XppDriver extends AbstractXppDriver {
    private static XmlPullParserFactory factory;

    public XppDriver() {
        super(new XmlFriendlyNameCoder());
    }

    public XppDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    public XppDriver(XmlFriendlyReplacer replacer) {
        this((NameCoder) replacer);
    }

    protected synchronized XmlPullParser createParser() throws XmlPullParserException {
        if (factory == null) {
            factory = XmlPullParserFactory.newInstance();
        }
        return factory.newPullParser();
    }
}
