package com.paypal.android.p022a.p023a;

import com.paypal.android.p022a.C0842m;
import com.tencent.mm.sdk.plugin.BaseProfile;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* renamed from: com.paypal.android.a.a.h */
public final class C0828h {
    private String f1543a;
    private String f1544b;
    private String f1545c;
    private String f1546d;
    private String f1547e;
    private String f1548f;
    private String f1549g;
    private String f1550h;

    public C0828h() {
        this.f1543a = null;
        this.f1544b = null;
        this.f1545c = null;
        this.f1546d = null;
        this.f1547e = null;
        this.f1548f = null;
        this.f1549g = null;
        this.f1550h = null;
    }

    public final String m1528a() {
        return this.f1543a;
    }

    public final void m1529a(String str) {
        this.f1544b = str;
    }

    public final boolean m1530a(Element element) {
        NodeList elementsByTagName = element.getElementsByTagName("baseAddress");
        if (elementsByTagName.getLength() == 0) {
            return false;
        }
        Element element2 = (Element) elementsByTagName.item(0);
        NodeList elementsByTagName2 = element2.getElementsByTagName("line1");
        if (elementsByTagName2.getLength() == 0) {
            return false;
        }
        this.f1546d = C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes());
        elementsByTagName2 = element2.getElementsByTagName("line2");
        if (elementsByTagName2.getLength() > 0) {
            this.f1547e = C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes());
        }
        elementsByTagName2 = element2.getElementsByTagName(BaseProfile.COL_CITY);
        if (elementsByTagName2.getLength() == 0) {
            return false;
        }
        this.f1544b = C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes());
        elementsByTagName2 = element2.getElementsByTagName("state");
        if (elementsByTagName2.getLength() > 0) {
            this.f1549g = C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes());
        }
        elementsByTagName2 = element2.getElementsByTagName("postalCode");
        if (elementsByTagName2.getLength() > 0) {
            this.f1548f = C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes());
        }
        elementsByTagName2 = element2.getElementsByTagName("countryCode");
        if (elementsByTagName2.getLength() == 0) {
            return false;
        }
        this.f1545c = C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes());
        elementsByTagName = element2.getElementsByTagName(SharedPref.TYPE);
        if (elementsByTagName.getLength() > 0) {
            C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
        }
        elementsByTagName = element.getElementsByTagName("addressId");
        if (elementsByTagName.getLength() == 0) {
            return false;
        }
        this.f1550h = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
        elementsByTagName = element.getElementsByTagName("addresseeName");
        if (elementsByTagName.getLength() > 0) {
            this.f1543a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
        }
        return true;
    }

    public final String m1531b() {
        return this.f1544b;
    }

    public final void m1532b(String str) {
        this.f1545c = str;
    }

    public final String m1533c() {
        return this.f1545c;
    }

    public final void m1534c(String str) {
        this.f1546d = str;
    }

    public final String m1535d() {
        return this.f1546d;
    }

    public final void m1536d(String str) {
        this.f1547e = str;
    }

    public final String m1537e() {
        return this.f1547e;
    }

    public final void m1538e(String str) {
        this.f1548f = str;
    }

    public final String m1539f() {
        return this.f1548f;
    }

    public final void m1540f(String str) {
        this.f1549g = str;
    }

    public final String m1541g() {
        return this.f1549g;
    }

    public final void m1542g(String str) {
        this.f1550h = str;
    }

    public final String m1543h() {
        return this.f1550h;
    }
}
