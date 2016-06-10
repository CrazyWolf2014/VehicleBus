package com.paypal.android.MEP.p021b;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.p020a.C1099a;
import com.paypal.android.MEP.p020a.C1099a.C0803a;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.p022a.C1107b;

/* renamed from: com.paypal.android.MEP.b.d */
class C0815d implements OnClickListener {
    private /* synthetic */ C1299a f1488a;

    C0815d(C1299a c1299a) {
        this.f1488a = c1299a;
    }

    public final void onClick(View view) {
        int id = view.getId();
        if (this.f1488a.f2421f != null || (id & 2113929216) == 2113929216) {
            if ((id & 2130706432) == 2130706432) {
                int i = id - 2130706432;
                if (this.f1488a.f2421f != null) {
                    (PayPal.getInstance().getServer() == 2 ? C1099a.f2141a : C1107b.m2383e().m2424g()).put("FundingPlanId", this.f1488a.f2430p.get(i));
                    PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
                }
            } else if ((id & 2113929216) == 2113929216) {
                C1107b.m2383e().m2420a("FeeBearer", id - 2113929216 == 0 ? "ApplyFeeToSender" : "ApplyFeeToReceiver");
            } else {
                (PayPal.getInstance().getServer() == 2 ? C1099a.f2141a : C1107b.m2383e().m2424g()).put("ShippingAddressId", (String) this.f1488a.f2429o.get(id - 2097152000));
                if ((PayPal.getInstance().getServer() != 2 ? C1107b.m2383e().m2427k() : 0) == 0) {
                    PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
                } else {
                    this.f1488a.f2428n.m2301a(C0803a.STATE_UPDATING);
                }
            }
            this.f1488a.f2420e = true;
            this.f1488a.m2681a(0);
            if (PayPal.getInstance().getServer() == 2) {
                this.f1488a.m2682a(6, null);
            }
            C08051.m1498b();
        }
    }
}
