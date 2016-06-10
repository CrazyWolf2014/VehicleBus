package com.paypal.android.p022a;

import com.paypal.android.MEP.PayPal;
import com.paypal.android.p025c.C0860a;
import junit.framework.Assert;

/* renamed from: com.paypal.android.a.i */
class C1108i implements C0860a {
    C1108i() {
    }

    public final void m2429a(int i, Object obj) {
        Assert.assertTrue(i == 8);
        PayPal.getInstance().setLibraryInitialized(true);
        PayPal.getInstance().onInitializeCompletedOK(i, obj);
    }

    public final void m2430b(int i, Object obj) {
        Assert.assertTrue(i == 8);
        PayPal.getInstance().setLibraryInitialized(false);
        PayPal.getInstance().onInitializeCompletedError(i, obj);
    }
}
