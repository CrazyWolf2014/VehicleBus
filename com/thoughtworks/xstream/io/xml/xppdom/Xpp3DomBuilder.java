package com.thoughtworks.xstream.io.xml.xppdom;

import java.io.Reader;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;

public class Xpp3DomBuilder {
    public static Xpp3Dom build(Reader reader) throws Exception {
        XmlPullParser parser = new MXParser();
        parser.setInput(reader);
        try {
            Xpp3Dom xpp3Dom = (Xpp3Dom) XppDom.build(parser);
            return xpp3Dom;
        } finally {
            reader.close();
        }
    }
}
