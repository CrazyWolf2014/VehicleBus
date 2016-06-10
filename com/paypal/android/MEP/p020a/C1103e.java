package com.paypal.android.MEP.p020a;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.paypal.android.MEP.C0811a;
import com.paypal.android.MEP.C0811a.C0801b;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.MEP.p021b.C1278b;
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
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.MEP.a.e */
public final class C1103e extends C0858j implements TextWatcher, OnClickListener, C0801b, C0852a {
    public static String f2180a;
    private static C0850e f2181l;
    private C0809a f2182b;
    private String f2183c;
    private LinearLayout f2184d;
    private RelativeLayout f2185e;
    private C1278b f2186f;
    private EditText f2187g;
    private EditText f2188h;
    private EditText f2189i;
    private Button f2190j;
    private Button f2191k;
    private TextView f2192m;
    private Hashtable<String, Object> f2193n;
    private C0857i f2194o;
    private LinearLayout f2195p;
    private Context f2196q;

    /* renamed from: com.paypal.android.MEP.a.e.1 */
    class C08081 implements Runnable {
        C08081(C1103e c1103e) {
        }

        public final void run() {
            long currentTimeMillis = System.currentTimeMillis();
            float f = 0.0f;
            while (f < 3.0f) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                long currentTimeMillis2 = System.currentTimeMillis();
                f = (((float) (currentTimeMillis2 - currentTimeMillis)) / 1000.0f) + f;
                currentTimeMillis = currentTimeMillis2;
            }
            C1103e.f2181l.m1625b();
            PayPalActivity.getInstance().paymentSucceeded((String) C1107b.m2383e().m2421c("PayKey"), (String) C1107b.m2383e().m2421c("PaymentExecStatus"), true);
        }
    }

    /* renamed from: com.paypal.android.MEP.a.e.a */
    public enum C0809a {
        STATE_NORMAL,
        STATE_ERROR,
        STATE_PIN_SUCCESS
    }

    static {
        f2181l = null;
        f2180a = null;
    }

    public C1103e(Context context) {
        super(context);
    }

    private void m2329a(String str, boolean z) {
        View a = C0835d.m1555a(this.f2196q, -1, -2);
        LayoutParams layoutParams = new RelativeLayout.LayoutParams(a.getLayoutParams());
        layoutParams.addRule(13);
        a.setLayoutParams(layoutParams);
        a.setOrientation(1);
        a.setGravity(1);
        if (f2181l == null) {
            f2181l = new C0850e(this.f2196q);
        } else {
            ((LinearLayout) f2181l.getParent()).removeAllViews();
        }
        this.f2192m = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, this.f2196q);
        this.f2192m.setGravity(1);
        this.f2192m.setTextColor(-13408615);
        this.f2192m.setText(str);
        if (z) {
            View a2 = C0835d.m1555a(this.f2196q, -2, -2);
            a2.setOrientation(1);
            a2.setPadding(5, 10, 5, 10);
            View c0857i = new C0857i(this.f2196q, C0856a.GREEN_ALERT);
            c0857i.m1635a(C0839h.m1568a("ANDROID_pin_success"));
            c0857i.setPadding(5, 5, 5, 5);
            a2.addView(c0857i);
            a.addView(a2);
        }
        a.addView(f2181l);
        a.addView(this.f2192m);
        this.f2185e.removeAllViews();
        this.f2185e.addView(a);
    }

    private boolean m2331d() {
        return this.f2187g.getText().toString().length() < 9 ? false : C0839h.m1575e(this.f2187g.getText().toString());
    }

    private boolean m2332e() {
        String obj = this.f2188h.getText().toString();
        String obj2 = this.f2189i.getText().toString();
        return (obj.length() < 4 || obj.length() > 8 || obj2.length() < 4 || obj2.length() > 8 || !obj.equals(obj2)) ? false : C0839h.m1576f(obj);
    }

    private void m2333f() {
        try {
            ((InputMethodManager) PayPalActivity.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(this.f2187g.getWindowToken(), 0);
        } catch (Exception e) {
        }
        try {
            ((InputMethodManager) PayPalActivity.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(this.f2188h.getWindowToken(), 0);
        } catch (Exception e2) {
        }
        try {
            ((InputMethodManager) PayPalActivity.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(this.f2189i.getWindowToken(), 0);
        } catch (Exception e3) {
        }
    }

    public final void m2334a() {
    }

    public final void m2335a(int i, Object obj) {
        this.f2182b = C0809a.STATE_PIN_SUCCESS;
        C08051.m1498b();
    }

    public final void m2336a(Context context) {
        super.m1637a(context);
        this.f2196q = context;
        this.f2182b = C0809a.STATE_NORMAL;
        View a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 5, 5, 15);
        a.addView(C0845o.m1619b(C0844a.HELVETICA_16_BOLD, context));
        this.f2186f = new C1278b(context, this);
        this.f2186f.m1633a((C0852a) this);
        a.addView(this.f2186f);
        addView(a);
        this.f2184d = C0835d.m1555a(context, -1, -1);
        this.f2184d.setOrientation(1);
        this.f2184d.setPadding(10, 5, 10, 5);
        this.f2184d.setBackgroundDrawable(C0835d.m1553a());
        this.f2184d.addView(new C0854h(C0839h.m1568a("ANDROID_payment_made"), context));
        this.f2195p = C0835d.m1555a(context, -1, -2);
        this.f2195p.setOrientation(1);
        this.f2195p.setPadding(5, 10, 5, 10);
        this.f2194o = new C0857i(context, C0856a.YELLOW_ALERT);
        this.f2194o.m1635a("This page is currently being used to test components.");
        this.f2194o.setPadding(0, 5, 0, 5);
        this.f2195p.setVisibility(8);
        this.f2195p.addView(this.f2194o);
        this.f2184d.addView(this.f2195p);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 0, 5, 5);
        View a2 = C0845o.m1618a(C0844a.HELVETICA_16_BOLD, context);
        a2.setTextColor(-14993820);
        a2.setText(C0839h.m1568a("ANDROID_create_a_pin"));
        a2.setGravity(3);
        a.addView(a2);
        this.f2184d.addView(a);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(10, 10, 10, 5);
        a.setBackgroundDrawable(C0835d.m1553a());
        this.f2187g = new EditText(context);
        this.f2187g.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        this.f2187g.setInputType(3);
        this.f2187g.setHint(C0839h.m1568a("ANDROID_enter_mobile"));
        this.f2187g.setSingleLine(true);
        this.f2187g.addTextChangedListener(this);
        a.addView(this.f2187g);
        this.f2188h = new EditText(context);
        this.f2188h.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        this.f2188h.setInputType(3);
        this.f2188h.setHint(C0839h.m1568a("ANDROID_enter_pin"));
        this.f2188h.setSingleLine(true);
        this.f2188h.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.f2188h.addTextChangedListener(this);
        a.addView(this.f2188h);
        this.f2189i = new EditText(context);
        this.f2189i.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        this.f2189i.setInputType(3);
        this.f2189i.setHint(C0839h.m1568a("ANDROID_reenter_pin"));
        this.f2189i.setSingleLine(true);
        this.f2189i.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.f2189i.addTextChangedListener(this);
        a.addView(this.f2189i);
        this.f2184d.addView(a);
        a = C0835d.m1555a(context, -1, -2);
        a.setGravity(1);
        a.setOrientation(1);
        a.setPadding(5, 10, 5, 5);
        this.f2190j = new Button(context);
        this.f2190j.setLayoutParams(new RelativeLayout.LayoutParams(-1, C0835d.m1557b()));
        this.f2190j.setText(C0839h.m1568a("ANDROID_create_pin"));
        this.f2190j.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2190j.setBackgroundDrawable(C0836e.m1559a());
        this.f2190j.setOnClickListener(this);
        this.f2190j.setEnabled(false);
        a.addView(this.f2190j);
        this.f2184d.addView(a);
        a = C0835d.m1555a(context, -1, -2);
        a.setGravity(1);
        a.setOrientation(1);
        a.setPadding(5, 5, 5, 10);
        this.f2191k = new Button(context);
        this.f2191k.setLayoutParams(new RelativeLayout.LayoutParams(-1, C0835d.m1557b()));
        this.f2191k.setText(C0839h.m1568a("ANDROID_skip"));
        this.f2191k.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2191k.setBackgroundDrawable(C0836e.m1561b());
        this.f2191k.setOnClickListener(this);
        a.addView(this.f2191k);
        this.f2184d.addView(a);
        addView(this.f2184d);
        this.f2185e = new RelativeLayout(context);
        this.f2185e.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        this.f2185e.setBackgroundDrawable(C0835d.m1553a());
        m2329a(C0839h.m1568a("ANDROID_creating_pin"), false);
        this.f2185e.setVisibility(8);
        addView(this.f2185e);
        this.f2193n = new Hashtable();
        if (!PayPal.getInstance().canShowCart()) {
            this.f2186f.m2582a(false, false);
        }
    }

    public final void m2337a(C0853g c0853g, int i) {
    }

    public final void m2338a(String str, Object obj) {
        this.f2193n.put(str, obj);
    }

    public final void afterTextChanged(Editable editable) {
        if (m2331d() && m2332e()) {
            this.f2190j.setEnabled(true);
        } else {
            this.f2190j.setEnabled(false);
        }
    }

    public final void m2339b() {
        if (this.f2182b == C0809a.STATE_ERROR) {
            this.f2184d.setVisibility(0);
            this.f2185e.setVisibility(8);
            this.f2194o.m1635a(this.f2183c);
            this.f2195p.setVisibility(0);
        } else if (this.f2182b == C0809a.STATE_PIN_SUCCESS) {
            f2181l.m1625b();
            m2329a(C0839h.m1568a("ANDROID_returning_to_merchant"), true);
            f2181l.m1624a();
            new Thread(new C08081(this)).start();
        }
    }

    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public final void m2340d(String str) {
        f2181l.m1625b();
        this.f2183c = str;
        this.f2182b = C0809a.STATE_ERROR;
        this.f2188h.setText(XmlPullParser.NO_NAMESPACE);
        this.f2189i.setText(XmlPullParser.NO_NAMESPACE);
        C08051.m1498b();
    }

    public final void m2341l() {
        C1107b.m2383e().m2420a("NewPhone", this.f2193n.get("mobileNumber"));
        C1107b.m2383e().m2420a("NewPin", this.f2193n.get("newPIN"));
        C1107b.m2383e().m2420a("delegate", (Object) this);
        C1107b.m2383e().m2417a(11);
    }

    public final void onClick(View view) {
        if (view == this.f2190j) {
            if (m2331d() && m2332e()) {
                m2333f();
                if (PayPal.getInstance().getServer() != 2) {
                    C0811a.m1500a().m1503b(this, this.f2187g.getText().toString(), this.f2188h.getText().toString());
                }
                this.f2184d.setVisibility(8);
                this.f2185e.setVisibility(0);
                f2181l.m1624a();
                if (PayPal.getInstance().getServer() == 2) {
                    this.f2182b = C0809a.STATE_PIN_SUCCESS;
                    C08051.m1498b();
                }
            }
        } else if (view == this.f2191k) {
            m2333f();
            if (f2180a == null || f2180a.length() == 0) {
                f2180a = "11111111";
            }
            PayPalActivity.getInstance().paymentSucceeded((String) C1107b.m2383e().m2421c("PayKey"), (String) C1107b.m2383e().m2421c("PaymentExecStatus"));
        }
    }

    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
