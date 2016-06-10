package com.paypal.android.MEP.p020a;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.p021b.C1278b;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p022a.C1107b;
import com.paypal.android.p024b.C0851f;
import com.paypal.android.p024b.C0853g;
import com.paypal.android.p024b.C0853g.C0852a;
import com.paypal.android.p024b.C0854h;
import com.paypal.android.p024b.C0858j;
import org.achartengine.renderer.DefaultRenderer;

/* renamed from: com.paypal.android.MEP.a.h */
public final class C1106h extends C0858j implements OnClickListener, C0852a {
    public static String f2212a;
    private C1278b f2213b;
    private Button f2214c;

    static {
        f2212a = null;
    }

    public C1106h(Context context) {
        super(context);
    }

    public final void m2356a() {
    }

    public final void m2357a(Context context) {
        PayPal instance = PayPal.getInstance();
        super.m1637a(context);
        View a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 5, 5, 15);
        this.f2213b = new C1278b(context, this);
        this.f2213b.m1633a((C0852a) this);
        a.addView(this.f2213b);
        addView(a);
        View linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-1, -1));
        linearLayout.setPadding(5, 5, 5, 5);
        linearLayout.setBackgroundDrawable(C0835d.m1553a());
        if (instance.getTextType() == 1) {
            linearLayout.addView(new C0854h(C0839h.m1568a("ANDROID_donation_made"), context));
        } else {
            linearLayout.addView(new C0854h(C0839h.m1568a("ANDROID_payment_made"), context));
        }
        View c0851f = new C0851f(context, C0844a.HELVETICA_16_NORMAL, C0844a.HELVETICA_16_NORMAL);
        c0851f.setLayoutParams(new LayoutParams(-1, -2));
        String a2 = C0839h.m1568a("ANDROID_successfully_paid_amount_to_recipient");
        if (instance.getTextType() == 1) {
            a2 = C0839h.m1568a("ANDROID_successfully_donated_amount_to_recipient");
        }
        c0851f.m1627a(a2.replace("{1}", instance.getPayment().getTotal().toString()) + ".");
        linearLayout.addView(c0851f);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setGravity(1);
        this.f2214c = new Button(context);
        this.f2214c.setText(C0839h.m1568a("ANDROID_done"));
        this.f2214c.setLayoutParams(new LayoutParams(-2, C0835d.m1557b()));
        this.f2214c.setBackgroundDrawable(C0836e.m1559a());
        this.f2214c.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2214c.setOnClickListener(this);
        a.addView(this.f2214c);
        linearLayout.addView(a);
        addView(linearLayout);
    }

    public final void m2358a(C0853g c0853g, int i) {
    }

    public final void m2359b() {
    }

    public final void onClick(View view) {
        if (view == this.f2214c) {
            if (f2212a == null || f2212a.length() == 0) {
                f2212a = "1111111";
            }
            PayPalActivity.getInstance().paymentSucceeded((String) C1107b.m2383e().m2421c("PayKey"), (String) C1107b.m2383e().m2421c("PaymentExecStatus"), true);
        }
    }
}
