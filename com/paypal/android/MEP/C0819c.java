package com.paypal.android.MEP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.p022a.C1107b;

/* renamed from: com.paypal.android.MEP.c */
class C0819c extends BroadcastReceiver {
    private /* synthetic */ PayPalActivity f1503a;

    C0819c(PayPalActivity payPalActivity) {
        this.f1503a = payPalActivity;
    }

    public final void onReceive(Context context, Intent intent) {
        PayPalActivity.f1402h = null;
        String action = intent.getAction();
        if (action.equals(PayPalActivity.LOGIN_SUCCESS)) {
            if (PayPal.getInstance().getPayType() == 3) {
                C08051.m1499b(6);
                return;
            }
            C1107b.m2383e().m2419a("mpl-review");
            C08051.m1499b(3);
        } else if (action.equals(PayPalActivity.LOGIN_FAIL)) {
            C08051.m1499b(0);
            if (intent.getStringExtra("ERROR_TIMEOUT") != null) {
                PayPalActivity.f1402h = intent.getStringExtra("ERROR_TIMEOUT");
            }
        } else if (action.equals(PayPalActivity.CREATE_PAYMENT_SUCCESS)) {
            if (PayPal.getInstance().getPayType() == 3) {
                C08051.m1499b(6);
                return;
            }
            C1107b.m2383e().m2419a("mpl-review");
            C08051.m1499b(3);
        } else if (action.equals(PayPalActivity.FATAL_ERROR)) {
            this.f1503a.f1407f = intent;
            C08051.m1499b(5);
        }
    }
}
