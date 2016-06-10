package com.paypal.android.p022a;

import com.paypal.android.MEP.PayPal;
import com.paypal.android.p025c.C0860a;
import junit.framework.Assert;

/* renamed from: com.paypal.android.a.j */
class C1109j implements C0860a {
    C1109j() {
    }

    public final void m2431a(int i, Object obj) {
        Assert.assertTrue(i == 12);
        PayPal.getInstance().setLibraryInitialized(false);
    }

    public final void m2432b(int i, Object obj) {
        Assert.assertTrue(i == 12);
        PayPal.getInstance().setLibraryInitialized(false);
    }
}
