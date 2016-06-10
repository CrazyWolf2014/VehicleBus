package com.autonavi.gelocator.api;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.autonavi.gelocator.api.i */
final class C0106i extends DefaultHandler {
    public String f816a;
    private boolean f817b;

    C0106i(ParserXml parserXml) {
        this.f816a = XmlPullParser.NO_NAMESPACE;
        this.f817b = false;
    }

    public final void characters(char[] cArr, int i, int i2) {
        if (this.f817b) {
            this.f816a = new String(cArr, i, i2);
        }
        super.characters(cArr, i, i2);
    }

    public final void endElement(String str, String str2, String str3) {
        if (str2.equals("sres")) {
            this.f817b = false;
        }
        super.endElement(str, str2, str3);
    }

    public final void startElement(String str, String str2, String str3, Attributes attributes) {
        if (str2.equals("sres")) {
            this.f817b = true;
        }
        super.startElement(str, str2, str3, attributes);
    }
}
