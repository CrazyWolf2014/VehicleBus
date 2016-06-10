package com.paypal.android.MEP.p020a;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.paypal.android.MEP.C0811a.C0801b;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPreapproval;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.MEP.p021b.C0817f;
import com.paypal.android.MEP.p021b.C1278b;
import com.paypal.android.MEP.p021b.C1299a;
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
import java.util.regex.Pattern;
import org.achartengine.renderer.DefaultRenderer;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

/* renamed from: com.paypal.android.MEP.a.g */
public final class C1105g extends C0858j implements OnClickListener, C0801b, C0814b, C0852a {
    private static C0850e f2198n;
    private C0810a f2199a;
    private C1278b f2200b;
    private Button f2201c;
    private Button f2202d;
    private Button f2203e;
    private Button f2204f;
    private C0857i f2205g;
    private C0857i f2206h;
    private String f2207i;
    private LinearLayout f2208j;
    private LinearLayout f2209k;
    private RelativeLayout f2210l;
    private TextView f2211m;

    /* renamed from: com.paypal.android.MEP.a.g.a */
    public enum C0810a {
        STATE_PIN,
        STATE_REVIEW,
        STATE_CONFIRM_PREAPPROVAL,
        STATE_ERROR
    }

    static {
        f2198n = null;
    }

    public C1105g(Context context) {
        super(context);
        this.f2211m = null;
    }

    private void m2345a(C0810a c0810a) {
        this.f2199a = c0810a;
        C08051.m1498b();
    }

    public final void m2346a() {
    }

    public final void m2347a(int i, Object obj) {
        PayPalActivity.getInstance().paymentSucceeded((String) C1107b.m2383e().m2421c("PreapprovalKey"), (String) C1107b.m2383e().m2421c("PaymentExecStatus"), true);
    }

    public final void m2348a(Context context) {
        PayPalPreapproval preapproval = PayPal.getInstance().getPreapproval();
        super.m1637a(context);
        if (preapproval.getPinRequired()) {
            C1107b.m2383e().m2419a("mpl-preapproval-PIN");
            this.f2199a = C0810a.STATE_PIN;
        } else {
            this.f2199a = C0810a.STATE_REVIEW;
        }
        View a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 5, 5, 15);
        a.addView(C0845o.m1619b(C0844a.HELVETICA_16_BOLD, context));
        this.f2200b = new C1278b(context, this);
        this.f2200b.m1633a((C0852a) this);
        a.addView(this.f2200b);
        addView(a);
        this.f2208j = new LinearLayout(context);
        this.f2208j.setOrientation(1);
        this.f2208j.setLayoutParams(new LayoutParams(-1, -1));
        this.f2208j.setPadding(5, 5, 5, 5);
        this.f2208j.setBackgroundDrawable(C0835d.m1553a());
        this.f2208j.addView(new C0854h(C0839h.m1568a("ANDROID_create_code"), context));
        a = new TextView(context);
        a.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        a.setBackgroundColor(0);
        a.setTextColor(-13408615);
        a.setGravity(3);
        a.setTypeface(Typeface.create("Helvetica", 1));
        a.setTextSize(12.0f);
        a.setPadding(5, 5, 5, 5);
        a.setText(C0839h.m1568a("ANDROID_require_pin").replace("%m", preapproval.getMerchantName()));
        this.f2208j.addView(a);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 10, 5, 10);
        this.f2206h = new C0857i(context, C0856a.YELLOW_ALERT);
        this.f2206h.m1635a("This page is currently being used to test components.");
        this.f2206h.setPadding(0, 5, 0, 5);
        this.f2206h.setVisibility(8);
        a.addView(this.f2206h);
        this.f2208j.addView(a);
        a = new EditText(context);
        a.setInputType(3);
        a.setLayoutParams(new LayoutParams(-1, -2));
        a.setHint(C0839h.m1568a("ANDROID_enter_code"));
        a.setSingleLine(true);
        a.setId(8001);
        a.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.f2208j.addView(a);
        a = new EditText(context);
        a.setInputType(3);
        a.setLayoutParams(new LayoutParams(-1, -2));
        a.setHint(C0839h.m1568a("ANDROID_reenter_code"));
        a.setSingleLine(true);
        a.setId(8002);
        a.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.f2208j.addView(a);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setGravity(1);
        this.f2204f = new Button(context);
        this.f2204f.setText(C0839h.m1568a("ANDROID_create"));
        this.f2204f.setLayoutParams(new LinearLayout.LayoutParams(-1, C0835d.m1557b(), 0.5f));
        this.f2204f.setGravity(17);
        this.f2204f.setBackgroundDrawable(C0836e.m1559a());
        this.f2204f.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2204f.setOnClickListener(this);
        View a2 = C0835d.m1555a(context, -1, -2);
        a2.setOrientation(1);
        a2.setGravity(1);
        a2.addView(this.f2204f);
        a2.setPadding(0, 15, 0, 15);
        a.addView(a2);
        this.f2202d = new Button(context);
        this.f2202d.setText(C0839h.m1568a("ANDROID_cancel"));
        this.f2202d.setLayoutParams(new LinearLayout.LayoutParams(-1, C0835d.m1557b(), 0.5f));
        this.f2202d.setGravity(17);
        this.f2202d.setBackgroundDrawable(C0836e.m1561b());
        this.f2202d.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2202d.setOnClickListener(this);
        a.addView(this.f2202d);
        this.f2208j.addView(a);
        addView(this.f2208j);
        this.f2209k = new LinearLayout(context);
        this.f2209k.setOrientation(1);
        this.f2209k.setLayoutParams(new LayoutParams(-1, -1));
        this.f2209k.setPadding(5, 5, 5, 5);
        this.f2209k.setBackgroundDrawable(C0835d.m1553a());
        this.f2209k.addView(new C0854h(C0839h.m1568a("ANDROID_review"), context));
        a = new LinearLayout(context);
        a.setOrientation(1);
        a.setBackgroundDrawable(C0835d.m1554a(-1, -1510918, -7829368));
        a.setPadding(10, 10, 10, 10);
        this.f2209k.addView(a);
        a2 = new TextView(context);
        a2.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        a2.setBackgroundColor(0);
        a2.setTextColor(-7829368);
        a2.setGravity(3);
        a2.setTypeface(Typeface.create("Helvetica", 1));
        a2.setTextSize(12.0f);
        a2.setText(C0839h.m1568a("ANDROID_payment_method"));
        a.addView(a2);
        a2 = new LinearLayout(context);
        a2.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        a2.setOrientation(0);
        a.addView(a2);
        a = new TextView(context);
        a.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
        a.setBackgroundColor(0);
        a.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        a.setGravity(3);
        a.setTypeface(Typeface.create("Helvetica", 0));
        a.setTextSize(12.0f);
        a.setText(C0839h.m1568a("ANDROID_primary_source") + ":");
        a2.addView(a);
        a = new TextView(context);
        a.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        a.setBackgroundColor(0);
        a.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        a.setGravity(5);
        a.setTypeface(Typeface.create("Helvetica", 0));
        a.setTextSize(12.0f);
        a.setText(C0839h.m1568a("ANDROID_paypal_balance"));
        a2.addView(a);
        a = new TextView(context);
        a.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        a.setBackgroundColor(0);
        a.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        a.setGravity(3);
        a.setTypeface(Typeface.create("Helvetica", 0));
        a.setTextSize(12.0f);
        a.setPadding(10, 10, 10, 10);
        String str = C0839h.m1568a("ANDROID_preapproval_agreement").replace("%m", preapproval.getMerchantName()) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + C0839h.m1568a("ANDROID_view_policies");
        CharSequence spannableString = new SpannableString(str);
        spannableString.setSpan(new UnderlineSpan(), str.indexOf(C0839h.m1568a("ANDROID_view_policies")), spannableString.length(), 0);
        spannableString.setSpan(new URLSpan(new String("https://www.paypal.com/" + PayPalActivity._paypal.getLanguage().substring(0, 2) + "/cgi-bin/webscr?cmd=xpt/Marketing/popup/FundingMixEducation-outside")), str.indexOf(C0839h.m1568a("ANDROID_view_policies")), spannableString.length(), 33);
        Linkify.addLinks(spannableString, Pattern.compile(C0839h.m1568a("ANDROID_view_policies")), "https://");
        a.setText(spannableString);
        a.setMovementMethod(LinkMovementMethod.getInstance());
        this.f2209k.addView(a);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setPadding(5, 10, 5, 10);
        this.f2205g = new C0857i(context, C0856a.YELLOW_ALERT);
        this.f2205g.m1635a("This page is currently being used to test components.");
        this.f2205g.setPadding(0, 5, 0, 5);
        this.f2205g.setVisibility(8);
        a.addView(this.f2205g);
        this.f2209k.addView(a);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setGravity(1);
        this.f2201c = new Button(context);
        if (PayPal.getInstance().getPreapproval().getType() == 1) {
            this.f2201c.setText(C0839h.m1568a("ANDROID_agree_pay"));
        } else {
            this.f2201c.setText(C0839h.m1568a("ANDROID_agree"));
        }
        this.f2201c.setLayoutParams(new LinearLayout.LayoutParams(-1, C0835d.m1557b(), 0.5f));
        this.f2201c.setGravity(17);
        this.f2201c.setBackgroundDrawable(C0836e.m1559a());
        this.f2201c.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2201c.setOnClickListener(this);
        a2 = C0835d.m1555a(context, -1, -2);
        a2.setOrientation(1);
        a2.setGravity(1);
        a2.addView(this.f2201c);
        a2.setPadding(0, 15, 0, 15);
        a.addView(a2);
        this.f2203e = new Button(context);
        this.f2203e.setText(C0839h.m1568a("ANDROID_cancel"));
        this.f2203e.setLayoutParams(new LinearLayout.LayoutParams(-1, C0835d.m1557b(), 0.5f));
        this.f2203e.setGravity(17);
        this.f2203e.setBackgroundDrawable(C0836e.m1561b());
        this.f2203e.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2203e.setOnClickListener(this);
        a.addView(this.f2203e);
        this.f2209k.addView(a);
        addView(this.f2209k);
        this.f2210l = new RelativeLayout(context);
        this.f2210l.setLayoutParams(new LayoutParams(-1, -1));
        this.f2210l.setBackgroundDrawable(C0835d.m1553a());
        a2 = C0835d.m1555a(context, -1, -2);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(a2.getLayoutParams());
        layoutParams.addRule(13);
        a2.setLayoutParams(layoutParams);
        a2.setOrientation(1);
        a2.setGravity(1);
        if (f2198n == null) {
            f2198n = new C0850e(context);
        } else {
            ((LinearLayout) f2198n.getParent()).removeAllViews();
        }
        this.f2211m = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, context);
        this.f2211m.setGravity(1);
        this.f2211m.setTextColor(-13408615);
        this.f2211m.setText(C0839h.m1568a("ANDROID_processing_transaction_message"));
        a2.addView(f2198n);
        a2.addView(this.f2211m);
        this.f2210l.addView(a2);
        this.f2210l.setVisibility(8);
        addView(this.f2210l);
        if (preapproval.getPinRequired()) {
            this.f2209k.setVisibility(8);
        } else {
            this.f2208j.setVisibility(8);
        }
    }

    public final void m2349a(C1299a c1299a, int i) {
    }

    public final void m2350a(C0853g c0853g, int i) {
        if (i == 1 && this.f2200b != null && c0853g != this.f2200b) {
            this.f2200b.m2434a(0);
        }
    }

    public final void m2351a(String str, Object obj) {
    }

    public final void m2352b() {
        if (this.f2199a == C0810a.STATE_CONFIRM_PREAPPROVAL) {
            this.f2200b.m2582a(false, true);
            this.f2208j.setVisibility(8);
            this.f2209k.setVisibility(8);
            this.f2210l.setVisibility(0);
            f2198n.m1624a();
        } else if (this.f2199a == C0810a.STATE_PIN || this.f2199a == C0810a.STATE_REVIEW || this.f2199a == C0810a.STATE_ERROR) {
            this.f2200b.m2582a(true, false);
            f2198n.m1625b();
            this.f2210l.setVisibility(8);
            if (this.f2199a == C0810a.STATE_PIN) {
                this.f2208j.setVisibility(0);
                this.f2209k.setVisibility(8);
            } else if (this.f2199a == C0810a.STATE_REVIEW) {
                this.f2208j.setVisibility(8);
                this.f2209k.setVisibility(0);
            } else if (this.f2209k.getVisibility() == 0) {
                this.f2205g.m1635a(this.f2207i);
                this.f2205g.setVisibility(0);
            } else {
                this.f2206h.m1635a(this.f2207i);
                this.f2206h.setVisibility(0);
            }
        }
    }

    public final C0810a m2353c() {
        return this.f2199a;
    }

    public final void m2354d(String str) {
        if (this.f2199a == C0810a.STATE_CONFIRM_PREAPPROVAL) {
            this.f2207i = str;
            m2345a(C0810a.STATE_ERROR);
        }
    }

    public final void m2355l() {
    }

    public final void onClick(View view) {
        int i = 0;
        if (this.f2202d == view || this.f2203e == view) {
            new C0817f(PayPalActivity.getInstance()).show();
        } else if (this.f2201c == view) {
            m2345a(C0810a.STATE_CONFIRM_PREAPPROVAL);
            if (PayPal.getInstance().getServer() == 2) {
                PayPalActivity.getInstance().paymentSucceeded("Demo Preapproval Key", "COMPLETED", true);
                return;
            }
            C1107b.m2383e().m2420a("delegate", (Object) this);
            C1107b.m2383e().m2417a(14);
        } else if (this.f2204f == view) {
            try {
                ((InputMethodManager) PayPalActivity.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(findViewById(8001).getWindowToken(), 0);
            } catch (Exception e) {
            }
            try {
                ((InputMethodManager) PayPalActivity.getInstance().getSystemService("input_method")).hideSoftInputFromWindow(findViewById(8002).getWindowToken(), 0);
            } catch (Exception e2) {
            }
            Editable text = ((EditText) findViewById(8001)).getText();
            Editable text2 = ((EditText) findViewById(8002)).getText();
            Object obj = text.toString();
            String obj2 = text2.toString();
            int i2 = (obj == null || obj2 == null) ? true : 0;
            if (!obj.equals(obj2)) {
                i2 = true;
            }
            if (obj.length() < 4 || obj.length() > 8) {
                i2 = true;
            }
            while (i < obj.length()) {
                if (obj.charAt(i) < '0' || obj.charAt(i) > '9') {
                    i2 = true;
                }
                i++;
            }
            if (i2 != 0) {
                this.f2207i = C0839h.m1568a("ANDROID_pin_invalid");
                m2345a(C0810a.STATE_ERROR);
                return;
            }
            C1107b.m2383e().m2420a("Pin", obj);
            m2345a(C0810a.STATE_REVIEW);
        }
    }
}
