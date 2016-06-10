package com.paypal.android.p022a;

import java.math.BigDecimal;

/* renamed from: com.paypal.android.a.l */
public final class C0841l {
    static {
    }

    public static String m1578a(String str, String str2) {
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(str);
        } catch (NumberFormatException e) {
            bigDecimal = new BigDecimal("0.0");
        }
        return C0839h.m1570a(bigDecimal, str2);
    }

    public static String m1579a(BigDecimal bigDecimal, String str) {
        return C0839h.m1570a(bigDecimal, str);
    }
}
