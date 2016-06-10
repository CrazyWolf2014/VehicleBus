package com.paypal.android.p022a.p023a;

import com.paypal.android.p022a.C0842m;
import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/* renamed from: com.paypal.android.a.a.c */
public final class C0823c {
    public C0821a f1518a;
    public C0821a f1519b;
    public C0826f f1520c;
    public Vector<C0831k> f1521d;
    private String f1522e;

    public static C0823c m1517a(Element element) {
        int i = 0;
        if (element == null) {
            return null;
        }
        C0823c c0823c = new C0823c();
        NodeList elementsByTagName = element.getElementsByTagName("fundingPlanId");
        if (elementsByTagName.getLength() != 1) {
            return null;
        }
        c0823c.f1522e = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
        elementsByTagName = element.getElementsByTagName("fundingAmount");
        if (elementsByTagName.getLength() != 1) {
            return null;
        }
        c0823c.f1518a = C0821a.m1512a((Element) elementsByTagName.item(0));
        elementsByTagName = element.getElementsByTagName("backupFundingSource");
        if (elementsByTagName.getLength() == 1) {
            C0827g.m1522a((Element) elementsByTagName.item(0));
        }
        elementsByTagName = element.getElementsByTagName("senderFees");
        if (elementsByTagName.getLength() == 1) {
            c0823c.f1519b = C0821a.m1512a((Element) elementsByTagName.item(0));
        }
        elementsByTagName = element.getElementsByTagName("currencyConversion");
        if (elementsByTagName.getLength() == 1) {
            c0823c.f1520c = C0826f.m1520a((Element) elementsByTagName.item(0));
        }
        c0823c.f1521d = new Vector();
        NodeList elementsByTagName2 = element.getElementsByTagName("charge");
        while (i < elementsByTagName2.getLength()) {
            C0831k a = C0831k.m1544a((Element) elementsByTagName2.item(i));
            if (a != null) {
                c0823c.f1521d.add(a);
            }
            i++;
        }
        return c0823c;
    }

    public final String m1518a() {
        return this.f1522e;
    }

    public final void m1519a(String str) {
        this.f1522e = str;
    }
}
