package com.paypal.android.MEP;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.paypal.android.p024b.C0858j;

/* renamed from: com.paypal.android.MEP.b */
class C0818b extends BroadcastReceiver {
    C0818b(PayPalActivity payPalActivity) {
    }

    public final void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.indexOf(PayPalActivity._pushIntent) != -1) {
            PayPalActivity.f1401c.m1485a(Integer.parseInt(action.substring(PayPalActivity._pushIntent.length())));
        } else if (action.indexOf(PayPalActivity._popIntent) != -1) {
            PayPalActivity.m1486a(PayPalActivity.f1401c);
        } else if (action.indexOf(PayPalActivity._replaceIntent) != -1) {
            PayPalActivity.m1490b(PayPalActivity.f1401c, Integer.parseInt(action.substring(PayPalActivity._replaceIntent.length())));
        } else if (action.indexOf(PayPalActivity._updateIntent) != -1) {
            ((C0858j) PayPalActivity.f1401c.f1405d.lastElement()).m1638b();
        }
    }
}
