package com.paypal.android.MEP.p020a;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.p021b.C1278b;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p022a.C1107b;
import com.paypal.android.p024b.C0853g;
import com.paypal.android.p024b.C0853g.C0852a;
import com.paypal.android.p024b.C0854h;
import com.paypal.android.p024b.C0857i;
import com.paypal.android.p024b.C0857i.C0856a;
import com.paypal.android.p024b.C0858j;
import org.achartengine.renderer.DefaultRenderer;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.MEP.a.c */
public final class C1101c extends C0858j implements OnClickListener, C0852a {
    String f2157a;
    String f2158b;
    private Intent f2159c;
    private C0857i f2160d;
    private C1278b f2161e;

    /* renamed from: com.paypal.android.MEP.a.c.1 */
    class C08041 implements OnClickListener {
        private /* synthetic */ C1101c f1466a;

        C08041(C1101c c1101c) {
            this.f1466a = c1101c;
        }

        public final void onClick(View view) {
            PayPalActivity.getInstance().paymentFailed((String) C1107b.m2383e().m2421c("CorrelationId"), (String) C1107b.m2383e().m2421c("PayKey"), this.f1466a.f2157a, this.f1466a.f2158b, true, false);
        }
    }

    public C1101c(Context context) {
        super(context);
        this.f2159c = null;
        this.f2157a = XmlPullParser.NO_NAMESPACE;
        this.f2158b = XmlPullParser.NO_NAMESPACE;
    }

    public C1101c(Context context, Intent intent) {
        super(context);
        this.f2159c = null;
        this.f2157a = XmlPullParser.NO_NAMESPACE;
        this.f2158b = XmlPullParser.NO_NAMESPACE;
        this.f2159c = intent;
        try {
            this.f2157a = this.f2159c.getStringExtra("FATAL_ERROR_ID");
            this.f2158b = this.f2159c.getStringExtra("FATAL_ERROR_MESSAGE");
            this.f2160d.m1635a(this.f2158b);
        } catch (Exception e) {
            this.f2157a = "10001";
            this.f2158b = C0839h.m1568a("ANDROID_10001");
        }
    }

    public final void m2312a() {
    }

    protected final void m2313a(Context context) {
        super.m1637a(context);
        View a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 5, 5, 15);
        a.addView(C0845o.m1619b(C0844a.HELVETICA_16_BOLD, context));
        this.f2161e = new C1278b(context, this);
        this.f2161e.m1633a((C0852a) this);
        a.addView(this.f2161e);
        addView(a);
        a = C0835d.m1555a(context, -1, -1);
        a.setBackgroundDrawable(C0835d.m1553a());
        a.setPadding(10, 5, 10, 5);
        a.setOrientation(1);
        a.addView(new C0854h(C0839h.m1568a("ANDROID_error_heading"), context));
        View a2 = C0835d.m1555a(context, -1, -2);
        a2.setOrientation(1);
        a2.setPadding(5, 10, 5, 10);
        this.f2160d = new C0857i(context, C0856a.RED_ALERT);
        this.f2160d.m1635a(C0839h.m1568a("ANDROID_10001"));
        this.f2160d.setPadding(0, 5, 0, 5);
        this.f2160d.setVisibility(0);
        a2.addView(this.f2160d);
        a.addView(a2);
        a2 = new Button(context);
        a2.setText(C0839h.m1568a("ANDROID_ok"));
        a2.setLayoutParams(new LayoutParams(-1, C0835d.m1557b()));
        a2.setGravity(1);
        a2.setBackgroundDrawable(C0836e.m1559a());
        a2.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        a2.setOnClickListener(new C08041(this));
        a.addView(a2);
        addView(a);
    }

    public final void m2314a(C0853g c0853g, int i) {
    }

    public final void m2315b() {
    }

    public final void onClick(View view) {
    }
}
