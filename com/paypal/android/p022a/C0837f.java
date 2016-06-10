package com.paypal.android.p022a;

import com.paypal.android.MEP.PayPal;

/* renamed from: com.paypal.android.a.f */
public final class C0837f extends Exception {
    private static final long serialVersionUID = 1;

    public C0837f(String str) {
        super(str);
        PayPal.loge("BadXMLException", "BadXMLException: " + str);
    }
}
