package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.naming.NameCoder;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XppDomDriver extends AbstractXppDomDriver {
    private static XmlPullParserFactory factory;

    public XppDomDriver() {
        super(new XmlFriendlyNameCoder());
    }

    public XppDomDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    public XppDomDriver(XmlFriendlyReplacer replacer) {
        super(replacer);
    }

    protected synchronized XmlPullParser createParser() throws XmlPullParserException {
        if (factory == null) {
            factory = XmlPullParserFactory.newInstance(null, XppDomDriver.class);
        }
        return factory.newPullParser();
    }
}
