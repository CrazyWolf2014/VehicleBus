package com.autonavi.gelocator.api;

import org.jivesoftware.smackx.Form;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/* renamed from: com.autonavi.gelocator.api.h */
final class C0105h extends DefaultHandler {
    public MyLocaitonBean f814a;
    private StringBuffer f815b;

    C0105h(ParserXml parserXml) {
        this.f814a = new MyLocaitonBean();
        this.f815b = new StringBuffer();
    }

    public final void characters(char[] cArr, int i, int i2) {
        this.f815b.append(new String(cArr, i, i2).toString());
        super.characters(cArr, i, i2);
    }

    public final void endElement(String str, String str2, String str3) {
        if (str2.equals(Form.TYPE_RESULT)) {
            this.f814a.setResult(this.f815b.toString());
        } else if (str2.equals("rdesc")) {
            this.f814a.setrDesc(this.f815b.toString());
        } else if (str2.equals("cenx")) {
            try {
                this.f814a.setCenx(Double.valueOf(this.f815b.toString()).doubleValue());
            } catch (Exception e) {
                this.f814a.setCenx(0.0d);
            }
        } else if (str2.equals("ceny")) {
            try {
                this.f814a.setCeny(Double.valueOf(this.f815b.toString()).doubleValue());
            } catch (Exception e2) {
                this.f814a.setCeny(0.0d);
            }
        } else if (str2.equals("radius")) {
            try {
                this.f814a.setRadius(Double.valueOf(this.f815b.toString()).doubleValue());
            } catch (Exception e3) {
                this.f814a.setRadius(0.0d);
            }
        } else if (str2.equals("citycode")) {
            this.f814a.setCityCode(this.f815b.toString());
        } else if (str2.equals("desc")) {
            this.f814a.setDes(this.f815b.toString());
        }
        super.endElement(str, str2, str3);
    }

    public final void startElement(String str, String str2, String str3, Attributes attributes) {
        this.f815b.delete(0, this.f815b.toString().length());
        super.startElement(str, str2, str3, attributes);
    }
}
