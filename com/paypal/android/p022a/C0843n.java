package com.paypal.android.p022a;

import com.paypal.android.MEP.PayPal;

/* renamed from: com.paypal.android.a.n */
public final class C0843n extends Exception {
    private static final long serialVersionUID = 1;

    public C0843n(String str) {
        super(str);
        PayPal.loge("BadPhoneNumberException", "BadPhoneNumberException: " + str);
    }
}
