package com.paypal.android.p022a.p023a;

import com.paypal.android.p022a.C0842m;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* renamed from: com.paypal.android.a.a.f */
public final class C0826f {
    public C0821a f1538a;
    public C0821a f1539b;
    private String f1540c;

    public static C0826f m1520a(Element element) {
        if (element == null) {
            return null;
        }
        C0826f c0826f = new C0826f();
        NodeList elementsByTagName = element.getElementsByTagName(PrivacyRule.SUBSCRIPTION_FROM);
        if (elementsByTagName.getLength() == 1) {
            c0826f.f1538a = C0821a.m1512a((Element) elementsByTagName.item(0));
        }
        elementsByTagName = element.getElementsByTagName(MultipleAddresses.TO);
        if (elementsByTagName.getLength() == 1) {
            c0826f.f1539b = C0821a.m1512a((Element) elementsByTagName.item(0));
        }
        elementsByTagName = element.getElementsByTagName("exchangeRate");
        if (elementsByTagName.getLength() != 1) {
            return null;
        }
        c0826f.f1540c = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
        return c0826f;
    }

    public final String m1521a() {
        return this.f1540c;
    }
}
