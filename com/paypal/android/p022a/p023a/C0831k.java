package com.paypal.android.p022a.p023a;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* renamed from: com.paypal.android.a.a.k */
public final class C0831k {
    public C0821a f1556a;
    public C0827g f1557b;

    public C0831k() {
        this.f1556a = null;
        this.f1557b = null;
    }

    public static C0831k m1544a(Element element) {
        if (element == null) {
            return null;
        }
        C0831k c0831k = new C0831k();
        c0831k.f1556a = null;
        c0831k.f1557b = null;
        NodeList childNodes = element.getChildNodes();
        int length = childNodes.getLength();
        for (int i = 0; i < length; i++) {
            Element element2 = (Element) childNodes.item(i);
            String tagName = element2.getTagName();
            if (tagName.equals("charge")) {
                C0821a a = C0821a.m1512a(element2);
                if (a != null) {
                    c0831k.f1556a = a;
                }
            } else if (tagName.equals("fundingSource")) {
                C0827g a2 = C0827g.m1522a(element2);
                if (a2 != null) {
                    c0831k.f1557b = a2;
                }
            }
        }
        return (c0831k.f1556a == null || c0831k.f1557b == null) ? null : c0831k;
    }
}
