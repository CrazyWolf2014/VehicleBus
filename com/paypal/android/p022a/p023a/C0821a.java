package com.paypal.android.p022a.p023a;

import com.paypal.android.p022a.C0842m;
import java.math.BigDecimal;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* renamed from: com.paypal.android.a.a.a */
public final class C0821a {
    private BigDecimal f1514a;
    private String f1515b;

    public static C0821a m1512a(Element element) {
        if (element == null) {
            return null;
        }
        C0821a c0821a = new C0821a();
        NodeList elementsByTagName = element.getElementsByTagName("code");
        if (elementsByTagName.getLength() != 1) {
            return null;
        }
        c0821a.f1515b = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
        elementsByTagName = element.getElementsByTagName("amount");
        if (elementsByTagName.getLength() != 1) {
            return null;
        }
        c0821a.m1514a(C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
        return c0821a;
    }

    public final BigDecimal m1513a() {
        return this.f1514a;
    }

    public final void m1514a(String str) {
        try {
            this.f1514a = new BigDecimal(str);
        } catch (NumberFormatException e) {
            this.f1514a = new BigDecimal("0.0");
        }
    }

    public final String m1515b() {
        return this.f1515b;
    }

    public final void m1516b(String str) {
        this.f1515b = str;
    }
}
