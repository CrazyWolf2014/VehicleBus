package com.amap.mapapi.core;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.core.u */
public class XmlObject {
    private static String f396a;
    private String f397b;
    private Object f398c;
    private List<XmlObject> f399d;
    private List<XmlObject> f400e;

    /* renamed from: com.amap.mapapi.core.u.a */
    static class XmlObject {
        private String f394a;
        private Object f395b;

        public XmlObject(String str, Object obj) {
            this.f394a = str;
            this.f395b = obj;
        }

        public String m545a() {
            return this.f394a;
        }

        public Object m547b() {
            return this.f395b;
        }

        public void m546a(Object obj) {
            this.f395b = obj;
        }
    }

    static {
        f396a = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    }

    public XmlObject(String str) {
        this.f397b = str;
    }

    public final void m552a(Object obj) {
        this.f398c = obj;
    }

    public final void m553a(String str, Object obj) {
        if (this.f399d == null) {
            this.f399d = new ArrayList();
        }
        for (XmlObject xmlObject : this.f399d) {
            if (str.equalsIgnoreCase(xmlObject.m545a())) {
                break;
            }
        }
        XmlObject xmlObject2 = null;
        if (xmlObject2 == null) {
            this.f399d.add(new XmlObject(str, obj));
            return;
        }
        xmlObject2.m546a(obj);
    }

    public final void m551a(XmlObject xmlObject) {
        if (this.f400e == null) {
            this.f400e = new ArrayList();
        }
        this.f400e.add(xmlObject);
    }

    public final String m549a() {
        return f396a + m550a(XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE);
    }

    protected final String m550a(String str, String str2) {
        return m548a(str, str2, 0);
    }

    private final String m548a(String str, String str2, int i) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append(str);
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(stringBuffer + "<" + this.f397b);
        if (this.f399d != null) {
            for (XmlObject xmlObject : this.f399d) {
                stringBuffer2.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + xmlObject.m545a() + "=\"" + xmlObject.m547b() + "\"");
            }
        }
        if (this.f400e != null) {
            stringBuffer2.append(">" + str2);
            int i3 = i + 1;
            for (XmlObject a : this.f400e) {
                stringBuffer2.append(a.m548a(str, str2, i3));
            }
            stringBuffer2.append(stringBuffer + "</" + this.f397b + ">" + str2);
        } else if (this.f398c == null) {
            stringBuffer2.append("/>" + str2);
        } else {
            stringBuffer2.append(">");
            stringBuffer2.append(this.f398c);
            stringBuffer2.append("</" + this.f397b + ">" + str2);
        }
        return stringBuffer2.toString();
    }
}
