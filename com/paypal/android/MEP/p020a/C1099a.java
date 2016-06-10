package com.paypal.android.MEP.p020a;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.paypal.android.MEP.C0811a;
import com.paypal.android.MEP.C0811a.C0801b;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.MEP.p021b.C0817f;
import com.paypal.android.MEP.p021b.C1278b;
import com.paypal.android.MEP.p021b.C1299a;
import com.paypal.android.MEP.p021b.C1299a.C0813a;
import com.paypal.android.MEP.p021b.C1299a.C0814b;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p022a.C1107b;
import com.paypal.android.p024b.C0850e;
import com.paypal.android.p024b.C0853g;
import com.paypal.android.p024b.C0853g.C0852a;
import com.paypal.android.p024b.C0854h;
import com.paypal.android.p024b.C0857i;
import com.paypal.android.p024b.C0857i.C0856a;
import com.paypal.android.p024b.C0858j;
import java.util.Hashtable;
import org.achartengine.renderer.DefaultRenderer;

/* renamed from: com.paypal.android.MEP.a.a */
public final class C1099a extends C0858j implements OnClickListener, C0801b, C0814b, C0852a {
    public static Hashtable<String, Object> f2141a;
    private static C0850e f2142n;
    private C0803a f2143b;
    private C1278b f2144c;
    private Button f2145d;
    private Button f2146e;
    private C1299a f2147f;
    private C1299a f2148g;
    private C1299a f2149h;
    private C0857i f2150i;
    private String f2151j;
    private LinearLayout f2152k;
    private RelativeLayout f2153l;
    private TextView f2154m;
    private Context f2155o;

    /* renamed from: com.paypal.android.MEP.a.a.1 */
    static /* synthetic */ class C08021 {
        static final /* synthetic */ int[] f1460a;

        static {
            f1460a = new int[C0813a.values().length];
            try {
                f1460a[C0813a.PAYMENT_DETAILS_FEES.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1460a[C0813a.PAYMENT_DETAILS_FUNDING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1460a[C0813a.PAYMENT_DETAILS_SHIPPING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* renamed from: com.paypal.android.MEP.a.a.a */
    public enum C0803a {
        STATE_NORMAL,
        STATE_SENDING_PAYMENT,
        STATE_ERROR,
        STATE_UPDATING
    }

    static {
        f2141a = null;
        f2142n = null;
    }

    public C1099a(Context context) {
        super(context);
        this.f2154m = null;
        this.f2155o = context;
    }

    private void m2297a(String str) {
        View a = C0835d.m1555a(this.f2155o, -1, -2);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(a.getLayoutParams());
        layoutParams.addRule(13);
        a.setLayoutParams(layoutParams);
        a.setOrientation(1);
        a.setGravity(1);
        if (f2142n == null) {
            f2142n = new C0850e(this.f2155o);
        } else {
            ((LinearLayout) f2142n.getParent()).removeAllViews();
        }
        this.f2154m = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, this.f2155o);
        this.f2154m.setGravity(1);
        this.f2154m.setTextColor(-13408615);
        this.f2154m.setText(str);
        a.addView(f2142n);
        a.addView(this.f2154m);
        this.f2153l.removeAllViews();
        this.f2153l.addView(a);
    }

    public final void m2298a() {
    }

    public final void m2299a(int i, Object obj) {
        if (this.f2143b == C0803a.STATE_SENDING_PAYMENT) {
            PayPal instance = PayPal.getInstance();
            PayPalActivity instance2 = PayPalActivity.getInstance();
            C1107b e = C1107b.m2383e();
            String str = (String) e.m2421c("PayKey");
            String str2 = (String) e.m2421c("PaymentExecStatus");
            if (instance.getServer() == 2) {
                instance2.setTransactionSuccessful(true);
                instance2.paymentSucceeded(str, str2, false);
                C1103e.f2180a = (String) obj;
                C08051.m1499b(7);
            } else if (instance.hasCreatedPIN() || instance.isLightCountry() || instance.getPayType() == 3) {
                instance2.paymentSucceeded(str, str2, true);
            } else {
                instance2.setTransactionSuccessful(true);
                instance2.paymentSucceeded(str, str2, false);
                C1103e.f2180a = (String) obj;
                C08051.m1499b(7);
            }
        }
    }

    public final void m2300a(Context context) {
        PayPal instance = PayPal.getInstance();
        super.m1637a(context);
        this.f2155o = context;
        this.f2143b = C0803a.STATE_NORMAL;
        View a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 5, 5, 15);
        a.addView(C0845o.m1619b(C0844a.HELVETICA_16_BOLD, context));
        this.f2144c = new C1278b(context, this);
        this.f2144c.m1633a((C0852a) this);
        a.addView(this.f2144c);
        addView(a);
        this.f2152k = new LinearLayout(context);
        this.f2152k.setOrientation(1);
        this.f2152k.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.f2152k.setPadding(5, 5, 5, 5);
        this.f2152k.setBackgroundDrawable(C0835d.m1553a());
        this.f2152k.addView(new C0854h(C0839h.m1568a("ANDROID_review"), context));
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 10, 5, 10);
        this.f2150i = new C0857i(context, C0856a.YELLOW_ALERT);
        this.f2150i.m1635a("This page is currently being used to test components.");
        this.f2150i.setPadding(0, 5, 0, 5);
        this.f2150i.setVisibility(8);
        a.addView(this.f2150i);
        this.f2152k.addView(a);
        this.f2148g = new C1299a(context, C0813a.PAYMENT_DETAILS_FEES, this);
        this.f2148g.m1633a((C0852a) this);
        this.f2148g.setPadding(0, 5, 0, 5);
        this.f2148g.m2683a((C0814b) this);
        if (PayPal.getInstance().shouldShowFees()) {
            this.f2152k.addView(this.f2148g);
        }
        this.f2147f = new C1299a(context, C0813a.PAYMENT_DETAILS_FUNDING, this);
        this.f2147f.m1633a((C0852a) this);
        this.f2147f.setPadding(0, 5, 0, 5);
        this.f2147f.m2683a((C0814b) this);
        this.f2152k.addView(this.f2147f);
        this.f2149h = new C1299a(context, C0813a.PAYMENT_DETAILS_SHIPPING, this);
        this.f2149h.m1633a((C0852a) this);
        this.f2149h.setPadding(0, 5, 0, 5);
        this.f2149h.m2683a((C0814b) this);
        if (instance.getShippingEnabled()) {
            this.f2152k.addView(this.f2149h);
        }
        a = C0835d.m1555a(context, -1, -2);
        a.setGravity(1);
        a.setPadding(5, 10, 5, 5);
        this.f2146e = new Button(context);
        if (instance.getTextType() == 1) {
            this.f2146e.setText(C0839h.m1568a("ANDROID_donate"));
        } else {
            this.f2146e.setText(C0839h.m1568a("ANDROID_pay"));
        }
        this.f2146e.setLayoutParams(new LinearLayout.LayoutParams(-1, C0835d.m1557b(), 0.5f));
        this.f2146e.setGravity(17);
        this.f2146e.setBackgroundDrawable(C0836e.m1559a());
        this.f2146e.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2146e.setOnClickListener(this);
        a.addView(this.f2146e);
        this.f2152k.addView(a);
        View a2 = C0835d.m1555a(context, -1, -2);
        a2.setGravity(1);
        a2.setPadding(5, 5, 5, 10);
        this.f2145d = new Button(context);
        this.f2145d.setText(C0839h.m1568a("ANDROID_cancel"));
        this.f2145d.setLayoutParams(new LinearLayout.LayoutParams(-1, C0835d.m1557b(), 0.5f));
        this.f2145d.setGravity(17);
        this.f2145d.setBackgroundDrawable(C0836e.m1561b());
        this.f2145d.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2145d.setOnClickListener(this);
        a2.addView(this.f2145d);
        this.f2152k.addView(a2);
        addView(this.f2152k);
        this.f2153l = new RelativeLayout(context);
        this.f2153l.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.f2153l.setBackgroundDrawable(C0835d.m1553a());
        a = C0835d.m1555a(context, -1, -2);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(a.getLayoutParams());
        layoutParams.addRule(13);
        a.setLayoutParams(layoutParams);
        a.setOrientation(1);
        a.setGravity(1);
        if (f2142n == null) {
            f2142n = new C0850e(context);
        } else {
            ((LinearLayout) f2142n.getParent()).removeAllViews();
        }
        this.f2154m = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, context);
        this.f2154m.setGravity(1);
        this.f2154m.setTextColor(-13408615);
        this.f2154m.setText(C0839h.m1568a("ANDROID_processing_transaction_message"));
        a.addView(f2142n);
        a.addView(this.f2154m);
        this.f2153l.addView(a);
        this.f2153l.setVisibility(8);
        addView(this.f2153l);
        if (!PayPal.getInstance().canShowCart()) {
            this.f2144c.m2582a(false, false);
        }
    }

    public final void m2301a(C0803a c0803a) {
        this.f2143b = c0803a;
        C08051.m1498b();
    }

    public final void m2302a(C1299a c1299a, int i) {
        switch (C08021.f1460a[c1299a.m2685b().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (this.f2147f != null) {
                    this.f2147f.setNextFocusUpId(i);
                }
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (this.f2149h != null) {
                    this.f2149h.setNextFocusUpId(i);
                }
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                if (this.f2146e != null) {
                    this.f2146e.setNextFocusUpId(i);
                }
            default:
        }
    }

    public final void m2303a(C0853g c0853g, int i) {
        if (i == 1) {
            if (!(this.f2144c == null || c0853g == this.f2144c)) {
                this.f2144c.m2434a(0);
            }
            if (!(this.f2147f == null || c0853g == this.f2147f)) {
                this.f2147f.m2681a(0);
            }
            if (!(this.f2148g == null || c0853g == this.f2148g)) {
                this.f2148g.m2681a(0);
            }
            if (this.f2149h != null && c0853g != this.f2149h) {
                this.f2149h.m2681a(0);
            }
        }
    }

    public final void m2304a(String str, Object obj) {
    }

    public final void m2305b() {
        if (this.f2147f != null) {
            this.f2147f.m2687c();
        }
        if (this.f2148g != null) {
            this.f2148g.m2687c();
        }
        if (this.f2149h != null) {
            this.f2149h.m2687c();
        }
        if (this.f2143b == C0803a.STATE_SENDING_PAYMENT) {
            m2297a(C0839h.m1568a("ANDROID_processing_transaction_message"));
            this.f2144c.m2582a(false, true);
            this.f2152k.setVisibility(8);
            this.f2153l.setVisibility(0);
            f2142n.m1624a();
        } else if (this.f2143b == C0803a.STATE_UPDATING) {
            m2297a(C0839h.m1568a("ANDROID_getting_information"));
            this.f2144c.m2582a(false, true);
            this.f2152k.setVisibility(8);
            this.f2153l.setVisibility(0);
            f2142n.m1624a();
        } else if (this.f2143b == C0803a.STATE_NORMAL || this.f2143b == C0803a.STATE_ERROR) {
            if (PayPal.getInstance().canShowCart()) {
                this.f2144c.m2582a(true, false);
            } else {
                this.f2144c.m2582a(false, false);
            }
            f2142n.m1625b();
            this.f2153l.setVisibility(8);
            this.f2152k.setVisibility(0);
            if (this.f2143b == C0803a.STATE_ERROR) {
                this.f2150i.m1635a(this.f2151j);
                this.f2150i.setVisibility(0);
            }
        }
    }

    public final C0803a m2306c() {
        return this.f2143b;
    }

    public final void m2307d() {
        if (this.f2144c != null) {
            this.f2144c.m2434a(0);
        }
        if (this.f2147f != null) {
            this.f2147f.m2681a(0);
        }
        if (this.f2148g != null) {
            this.f2148g.m2681a(0);
        }
        if (this.f2149h != null) {
            this.f2149h.m2681a(0);
        }
    }

    public final void m2308d(String str) {
        if (this.f2143b == C0803a.STATE_SENDING_PAYMENT) {
            this.f2151j = str;
            m2301a(C0803a.STATE_ERROR);
        }
    }

    public final void m2309l() {
        C1107b.m2383e().m2420a("delegate", (Object) this);
        C1107b.m2383e().m2417a(4);
    }

    public final void onClick(View view) {
        if (this.f2145d == view) {
            new C0817f(PayPalActivity.getInstance()).show();
        } else if (this.f2146e == view) {
            m2301a(C0803a.STATE_SENDING_PAYMENT);
            if (PayPal.getInstance().getServer() == 2) {
                m2299a(4, (Object) "10088342");
            } else {
                C0811a.m1500a().m1501a(this);
            }
        } else if ((view.getId() & 2130706432) == 2130706432) {
            this.f2147f.m2681a(0);
        } else if ((view.getId() & 2113929216) == 2113929216) {
            this.f2148g.m2681a(0);
        } else if ((view.getId() & 2097152000) == 2097152000) {
            this.f2149h.m2681a(0);
        }
    }
}
