package com.paypal.android.p022a.p023a;

import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0842m;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.a.a.g */
public final class C0827g {
    private String f1541a;
    private String f1542b;

    public static C0827g m1522a(Element element) {
        if (element == null) {
            return null;
        }
        C0827g c0827g = new C0827g();
        NodeList elementsByTagName = element.getElementsByTagName(SharedPref.TYPE);
        if (elementsByTagName.getLength() != 1) {
            return null;
        }
        c0827g.f1542b = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
        NodeList elementsByTagName2 = element.getElementsByTagName("lastFourOfAccountNumber");
        if (elementsByTagName2.getLength() == 1) {
            c0827g.f1541a = C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes());
        }
        elementsByTagName2 = element.getElementsByTagName("displayName");
        if (elementsByTagName2.getLength() == 1) {
            C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes());
        }
        return c0827g;
    }

    public final String m1523a() {
        return this.f1542b != null ? this.f1542b.equals("BALANCE") ? C0839h.m1568a("ANDROID_balance") : (this.f1542b.equals("BANK_DELAYED") || this.f1542b.equals("BANK_INSTANT")) ? C0839h.m1568a("ANDROID_bank") : (this.f1542b.equals("CREDITCARD") || this.f1542b.equals("DEBITCARD")) ? C0839h.m1568a("ANDROID_card") : this.f1542b : XmlPullParser.NO_NAMESPACE;
    }

    public final void m1524a(String str) {
        this.f1541a = str;
    }

    public final String m1525b() {
        return this.f1541a;
    }

    public final void m1526b(String str) {
        this.f1542b = str;
    }

    public final String m1527c() {
        return this.f1542b;
    }
}
