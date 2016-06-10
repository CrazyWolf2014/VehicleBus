package com.paypal.android.MEP.p020a;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.ifoer.mine.Contact;
import com.paypal.android.MEP.C0811a;
import com.paypal.android.MEP.C0811a.C0801b;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalPreapproval;
import com.paypal.android.MEP.p021b.C0816e;
import com.paypal.android.MEP.p021b.C0817f;
import com.paypal.android.MEP.p021b.C1278b;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p022a.C1107b;
import com.paypal.android.p022a.p023a.C0821a;
import com.paypal.android.p022a.p023a.C0823c;
import com.paypal.android.p022a.p023a.C0827g;
import com.paypal.android.p022a.p023a.C0828h;
import com.paypal.android.p022a.p023a.C0831k;
import com.paypal.android.p024b.C0847a;
import com.paypal.android.p024b.C0850e;
import com.paypal.android.p024b.C0853g;
import com.paypal.android.p024b.C0853g.C0852a;
import com.paypal.android.p024b.C0854h;
import com.paypal.android.p024b.C0857i;
import com.paypal.android.p024b.C0857i.C0856a;
import com.paypal.android.p024b.C0858j;
import java.util.Hashtable;
import java.util.Vector;
import org.achartengine.renderer.DefaultRenderer;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.MEP.a.d */
public final class C1102d extends C0858j implements TextWatcher, OnClickListener, C0801b, C0852a {
    public static boolean f2162a;
    private static C0850e f2163l;
    private C0806a f2164b;
    private Button f2165c;
    private Button f2166d;
    private Button f2167e;
    private TextView f2168f;
    private C1278b f2169g;
    private C0857i f2170h;
    private C0857i f2171i;
    private LinearLayout f2172j;
    private RelativeLayout f2173k;
    private TextView f2174m;
    private C0816e f2175n;
    private C0847a f2176o;
    private WebView f2177p;
    private String f2178q;
    private Hashtable<String, Object> f2179r;

    /* renamed from: com.paypal.android.MEP.a.d.1 */
    static /* synthetic */ class C08051 {
        public static void m1496a() {
            if (PayPalActivity.getInstance() != null) {
                PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity._popIntent));
            }
        }

        public static void m1497a(int i) {
            if (i < 0 || i >= 9) {
                throw new IllegalArgumentException("Attempted to push an unknown dialog.");
            } else if (PayPalActivity.getInstance() != null) {
                PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity._pushIntent + i));
            }
        }

        public static void m1498b() {
            if (PayPalActivity.getInstance() != null) {
                PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity._updateIntent));
            }
        }

        public static void m1499b(int i) {
            if (PayPalActivity.getInstance() != null) {
                PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity._replaceIntent + i));
            }
        }
    }

    /* renamed from: com.paypal.android.MEP.a.d.a */
    public enum C0806a {
        STATE_NORMAL,
        STATE_LOGGING_IN,
        STATE_ERROR,
        STATE_LOGGING_OUT
    }

    /* renamed from: com.paypal.android.MEP.a.d.b */
    private class C0807b extends WebViewClient {
        private /* synthetic */ C1102d f1472a;

        private C0807b(C1102d c1102d, byte b) {
            this.f1472a = c1102d;
        }

        public final boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (str.equals("About.Quick.Pay")) {
                this.f1472a.onClick(this.f1472a.f2177p);
            }
            return true;
        }
    }

    static {
        f2163l = null;
        f2162a = false;
    }

    public C1102d(Context context) {
        super(context);
        this.f2179r = new Hashtable();
    }

    private void m2317d() {
        Object obj = 1;
        String a = this.f2175n.m1507a();
        String b = this.f2175n.m1508b();
        Object obj2 = (C0839h.m1574d(a) || C0839h.m1575e(a)) ? 1 : null;
        if (b == null || b.length() <= 0) {
            obj = null;
        }
        if (obj2 != null && r0 != null) {
            this.f2175n.m1510d().setText(XmlPullParser.NO_NAMESPACE);
            m2322a(C0806a.STATE_LOGGING_IN);
            C0811a.m1500a().m1502a(this, a, b);
        }
    }

    private boolean m2318e() {
        String a = this.f2175n.m1507a();
        String b = this.f2175n.m1508b();
        boolean z = C0839h.m1574d(a) || C0839h.m1575e(a);
        boolean z2 = b != null && b.length() > 0;
        return z && z2;
    }

    public final void m2319a() {
        m2325b();
    }

    public final void m2320a(int i, Object obj) {
        C1107b e = C1107b.m2383e();
        if (this.f2164b == C0806a.STATE_LOGGING_IN) {
            switch (i) {
                case KEYRecord.OWNER_USER /*0*/:
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    e.m2420a("currentUser", this.f2175n.m1507a());
                    if (PayPal.getInstance().getPayType() == 3) {
                        e.m2426j();
                    } else {
                        e.m2425i();
                    }
                default:
            }
        }
    }

    protected final void m2321a(Context context) {
        View a;
        PayPal instance = PayPal.getInstance();
        super.m1637a(context);
        this.f2164b = C0806a.STATE_NORMAL;
        View a2 = C0835d.m1555a(context, -1, -2);
        a2.setOrientation(1);
        a2.setPadding(5, 5, 5, 15);
        a2.addView(C0845o.m1619b(C0844a.HELVETICA_16_BOLD, context));
        this.f2169g = new C1278b(context, this);
        this.f2169g.m1633a((C0852a) this);
        a2.addView(this.f2169g);
        if (instance.getPayType() == 3) {
            this.f2169g.m2582a(false, true);
        } else if (!instance.canShowCart()) {
            this.f2169g.m2582a(false, false);
        }
        addView(a2);
        this.f2172j = new LinearLayout(context);
        this.f2172j.setOrientation(1);
        this.f2172j.setLayoutParams(new LayoutParams(-1, -1));
        this.f2172j.setBackgroundDrawable(C0835d.m1553a());
        this.f2172j.setPadding(10, 5, 10, 5);
        this.f2172j.addView(new C0854h(C0839h.m1568a("ANDROID_login"), context));
        this.f2170h = new C0857i(context, C0856a.RED_ALERT);
        this.f2170h.m1635a("Placeholder");
        this.f2170h.setVisibility(8);
        this.f2170h.setPadding(0, 5, 0, 5);
        this.f2172j.addView(this.f2170h);
        this.f2171i = new C0857i(context, C0856a.BLUE_ALERT);
        this.f2171i.m1635a(C0839h.m1568a("ANDROID_not_you_message"));
        this.f2171i.setVisibility(8);
        this.f2171i.setPadding(0, 5, 0, 5);
        this.f2172j.addView(this.f2171i);
        this.f2175n = new C0816e(context);
        this.f2175n.m1509c().addTextChangedListener(this);
        this.f2175n.m1510d().addTextChangedListener(this);
        this.f2172j.addView(this.f2175n);
        LinearLayout a3 = C0835d.m1555a(context, -1, -2);
        a3.setOrientation(0);
        a3.setGravity(16);
        a3.setPadding(5, 5, 5, 0);
        this.f2176o = new C0847a(context);
        this.f2176o.setChecked(instance.getIsRememberMe());
        this.f2176o.setOnClickListener(this);
        if (instance.getAuthSetting() == 1) {
            a3.addView(this.f2176o);
        }
        this.f2177p = new WebView(context);
        this.f2177p.setWebViewClient(new C0807b());
        this.f2177p.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.f2177p.setBackgroundColor(0);
        this.f2177p.loadData("<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><head><style type=\"text/css\">b {color:#1B3664; font-family:Helvetica; font-size:12;}a {color:#686868; font-family:Helvetica; font-size:12;}</style></head><body><b>" + C0839h.m1568a("ANDROID_checkbox_opt_in") + "</b>" + "  " + "<a href=\"About.Quick.Pay\">" + C0839h.m1568a("ANDROID_checkbox_whats_this") + "</a>" + "</body>" + "</html>", "text/html", "utf-8");
        if (instance.getAuthSetting() == 1) {
            a3.addView(this.f2177p);
        }
        a2 = C0835d.m1555a(context, -1, -2);
        a2.setPadding(5, 5, 5, 5);
        a2.setOrientation(0);
        a2.setGravity(1);
        if (instance.getPayType() == 3 || instance.getShippingEnabled() || instance.isPersonalPayment()) {
            this.f2166d = null;
            this.f2167e = new Button(context);
            this.f2167e.setLayoutParams(new LinearLayout.LayoutParams(-1, C0835d.m1557b()));
            this.f2167e.setId(184424834);
            this.f2167e.setText(C0839h.m1568a("ANDROID_login"));
            this.f2167e.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
            this.f2167e.setBackgroundDrawable(C0836e.m1559a());
            this.f2167e.setOnClickListener(this);
            this.f2167e.setEnabled(false);
            a2.addView(this.f2167e);
        } else {
            a = C0835d.m1555a(context, -1, -2);
            a.setOrientation(1);
            a.setPadding(5, 0, 5, 5);
            View a4 = C0845o.m1618a(C0844a.HELVETICA_10_NORMAL, context);
            a4.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
            a4.setText(C0839h.m1568a("ANDROID_review_text"));
            a4.setGravity(3);
            a.addView(a4);
            this.f2172j.addView(a);
            this.f2166d = new Button(context);
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, C0835d.m1557b(), 0.5f);
            layoutParams.setMargins(0, 0, 5, 0);
            this.f2166d.setLayoutParams(layoutParams);
            if (instance.getTextType() == 1) {
                this.f2166d.setText(C0839h.m1568a("ANDROID_donate"));
            } else {
                this.f2166d.setText(C0839h.m1568a("ANDROID_pay"));
            }
            this.f2166d.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
            this.f2166d.setBackgroundDrawable(C0836e.m1559a());
            this.f2166d.setOnClickListener(this);
            this.f2166d.setEnabled(false);
            a2.addView(this.f2166d);
            this.f2167e = new Button(context);
            layoutParams = new LinearLayout.LayoutParams(-1, C0835d.m1557b(), 0.5f);
            layoutParams.setMargins(5, 0, 0, 0);
            this.f2167e.setLayoutParams(layoutParams);
            this.f2167e.setId(184424834);
            this.f2167e.setText(C0839h.m1568a("ANDROID_review"));
            this.f2167e.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
            this.f2167e.setBackgroundDrawable(C0836e.m1561b());
            this.f2167e.setOnClickListener(this);
            this.f2167e.setEnabled(false);
            a2.addView(this.f2167e);
        }
        this.f2172j.addView(a2);
        a2 = C0835d.m1555a(context, -1, -2);
        a2.setPadding(5, 5, 5, 5);
        a2.setOrientation(0);
        a2.setGravity(1);
        this.f2165c = new Button(context);
        this.f2165c.setLayoutParams(new LinearLayout.LayoutParams(-1, C0835d.m1557b()));
        this.f2165c.setText(C0839h.m1568a("ANDROID_cancel"));
        this.f2165c.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f2165c.setBackgroundDrawable(C0836e.m1561b());
        this.f2165c.setOnClickListener(this);
        a2.addView(this.f2165c);
        this.f2172j.addView(a2);
        this.f2168f = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
        this.f2168f.setLayoutParams(new LayoutParams(-1, -2));
        this.f2168f.setPadding(10, 10, 10, 10);
        this.f2168f.setTextColor(-16776961);
        this.f2168f.setGravity(17);
        this.f2168f.setOnClickListener(this);
        CharSequence spannableString = new SpannableString(C0839h.m1568a("ANDROID_help"));
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        this.f2168f.setText(spannableString);
        this.f2168f.setFocusable(true);
        this.f2172j.addView(this.f2168f);
        this.f2172j.invalidate();
        addView(this.f2172j);
        this.f2173k = new RelativeLayout(context);
        this.f2173k.setLayoutParams(new LayoutParams(-1, -1));
        this.f2173k.setBackgroundDrawable(C0835d.m1553a());
        a = C0835d.m1555a(context, -1, -2);
        ViewGroup.LayoutParams layoutParams2 = new LayoutParams(a.getLayoutParams());
        layoutParams2.addRule(13);
        a.setLayoutParams(layoutParams2);
        a.setOrientation(1);
        a.setGravity(1);
        if (f2163l == null) {
            f2163l = new C0850e(context);
        } else {
            ((LinearLayout) f2163l.getParent()).removeAllViews();
        }
        this.f2174m = C0845o.m1618a(C0844a.HELVETICA_16_NORMAL, context);
        this.f2174m.setGravity(1);
        this.f2174m.setTextColor(-13408615);
        this.f2174m.setText(C0839h.m1568a("ANDROID_logging_in_message"));
        a.addView(f2163l);
        a.addView(this.f2174m);
        this.f2173k.addView(a);
        this.f2173k.setVisibility(8);
        addView(this.f2173k);
        if (instance.getIsRememberMe()) {
            m2322a(C0806a.STATE_LOGGING_IN);
            this.f2175n.m1510d().setText(XmlPullParser.NO_NAMESPACE);
            C1107b.m2383e().m2420a("delegate", (Object) this);
            C1107b.m2383e().m2420a("quickPay", (Object) "false");
            C1107b.m2383e().m2417a(10);
        }
        if (f2162a) {
            m2322a(C0806a.STATE_ERROR);
        }
    }

    public final void m2322a(C0806a c0806a) {
        this.f2164b = c0806a;
        C08051.m1498b();
    }

    public final void m2323a(C0853g c0853g, int i) {
    }

    public final void m2324a(String str, Object obj) {
        this.f2179r.put(str, obj);
    }

    public final void afterTextChanged(Editable editable) {
        if (this.f2175n.m1509c().getText().length() <= 0 || this.f2175n.m1510d().getText().length() <= 0) {
            if (this.f2167e != null) {
                this.f2167e.setEnabled(false);
            }
            if (this.f2166d != null) {
                this.f2166d.setEnabled(false);
            }
        } else {
            if (this.f2167e != null) {
                this.f2167e.setEnabled(true);
            }
            if (this.f2166d != null) {
                this.f2166d.setEnabled(true);
            }
        }
        m2325b();
    }

    public final void m2325b() {
        if (this.f2164b == C0806a.STATE_LOGGING_IN || this.f2164b == C0806a.STATE_LOGGING_OUT) {
            this.f2169g.m2582a(false, true);
            this.f2172j.setVisibility(8);
            this.f2173k.setVisibility(0);
            f2163l.m1624a();
        } else if (this.f2164b == C0806a.STATE_NORMAL || this.f2164b == C0806a.STATE_ERROR) {
            if (PayPal.getInstance().canShowCart()) {
                this.f2169g.m2582a(true, false);
            }
            f2163l.m1625b();
            this.f2173k.setVisibility(8);
            this.f2172j.setVisibility(0);
            if (this.f2164b != C0806a.STATE_ERROR) {
                return;
            }
            if (f2162a) {
                this.f2171i.setVisibility(0);
                this.f2170h.setVisibility(8);
                return;
            }
            this.f2171i.setVisibility(8);
            this.f2170h.setVisibility(0);
            this.f2170h.m1635a(this.f2178q);
        }
    }

    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public final C0806a m2326c() {
        return this.f2164b;
    }

    public final void m2327d(String str) {
        f2162a = false;
        this.f2178q = str;
        if (this.f2164b == C0806a.STATE_LOGGING_IN) {
            m2322a(C0806a.STATE_ERROR);
        } else if (this.f2164b == C0806a.STATE_LOGGING_OUT) {
            m2322a(C0806a.STATE_ERROR);
        }
    }

    public final void m2328l() {
        C1107b.m2383e().m2420a("usernameOrPhone", this.f2179r.get("usernameOrPhone"));
        C1107b.m2383e().m2420a("passwordOrPin", this.f2179r.get("passwordOrPin"));
        C1107b.m2383e().m2420a("delegate", (Object) this);
        C1107b.m2383e().m2417a(0);
    }

    public final void onClick(View view) {
        C1107b e = C1107b.m2383e();
        PayPal instance = PayPal.getInstance();
        Context instance2 = PayPalActivity.getInstance();
        if (view == this.f2176o) {
            instance.setIsRememberMe(this.f2176o.isChecked());
        } else if (view == this.f2177p) {
            C08051.m1497a(1);
        } else if (view == this.f2166d) {
            if (m2318e()) {
                m2322a(C0806a.STATE_NORMAL);
                this.f2175n.m1511e();
                if (instance.getServer() == 2) {
                    instance2.paymentSucceeded("27892", (String) e.m2421c("PaymentExecStatus"), true);
                    return;
                }
                e.m2420a("quickPay", (Object) "true");
                m2317d();
            }
        } else if (view == this.f2167e) {
            if (m2318e()) {
                m2322a(C0806a.STATE_NORMAL);
                this.f2175n.m1511e();
                if (instance.getServer() == 2) {
                    if (instance.getPayType() == 3) {
                        PayPalPreapproval preapproval = instance.getPreapproval();
                        preapproval.setStartDate("2011-07-06T23:59:49.000-07:00");
                        preapproval.setEndDate("2011-08-07T23:59:49.000-07:00");
                        preapproval.setPinRequired(true);
                    }
                    C1099a.f2141a = new Hashtable();
                    Vector vector = new Vector();
                    Vector vector2 = new Vector();
                    C0823c c0823c = new C0823c();
                    c0823c.m1519a(Contact.RELATION_ASK);
                    c0823c.f1518a = new C0821a();
                    c0823c.f1518a.m1516b("USD");
                    c0823c.f1518a.m1514a("2.00");
                    c0823c.f1521d = new Vector();
                    C0831k c0831k = new C0831k();
                    c0831k.f1556a = new C0821a();
                    c0831k.f1556a.m1516b("USD");
                    c0831k.f1556a.m1514a("2.00");
                    c0831k.f1557b = new C0827g();
                    c0831k.f1557b.m1524a("2093");
                    c0831k.f1557b.m1526b("BANK_INSTANT");
                    c0823c.f1521d.add(c0831k);
                    vector.add(c0823c);
                    c0823c = new C0823c();
                    c0823c.m1519a(Contact.RELATION_FRIEND);
                    c0823c.f1518a = new C0821a();
                    c0823c.f1518a.m1516b("USD");
                    c0823c.f1518a.m1514a("2.00");
                    c0823c.f1521d = new Vector();
                    c0831k = new C0831k();
                    c0831k.f1556a = new C0821a();
                    c0831k.f1556a.m1516b("USD");
                    c0831k.f1556a.m1514a("2.00");
                    c0831k.f1557b = new C0827g();
                    c0831k.f1557b.m1524a("9853");
                    c0831k.f1557b.m1526b("CREDITCARD");
                    c0823c.f1521d.add(c0831k);
                    vector.add(c0823c);
                    c0823c = new C0823c();
                    c0823c.m1519a(Contact.RELATION_BACKNAME);
                    c0823c.f1518a = new C0821a();
                    c0823c.f1518a.m1516b("USD");
                    c0823c.f1518a.m1514a("2.00");
                    c0823c.f1521d = new Vector();
                    c0831k = new C0831k();
                    c0831k.f1556a = new C0821a();
                    c0831k.f1556a.m1516b("USD");
                    c0831k.f1556a.m1514a("2.00");
                    c0831k.f1557b = new C0827g();
                    c0831k.f1557b.m1524a("9691");
                    c0831k.f1557b.m1526b("CREDITCARD");
                    c0823c.f1521d.add(c0831k);
                    vector.add(c0823c);
                    C1099a.f2141a.put("FundingPlanId", Contact.RELATION_ASK);
                    C1099a.f2141a.put("FundingPlans", vector);
                    C0828h c0828h = new C0828h();
                    c0828h.m1529a("Trenton");
                    c0828h.m1534c("123 Home St");
                    c0828h.m1536d("Apt B");
                    c0828h.m1538e("08601");
                    c0828h.m1540f("NJ");
                    c0828h.m1542g(Contact.RELATION_FRIEND);
                    c0828h.m1532b("US");
                    vector2.add(c0828h);
                    c0828h = new C0828h();
                    c0828h.m1529a("Hamlin");
                    c0828h.m1534c("3012 Church Rd");
                    c0828h.m1536d(XmlPullParser.NO_NAMESPACE);
                    c0828h.m1538e("14464");
                    c0828h.m1540f("NY");
                    c0828h.m1542g(Contact.RELATION_BACKNAME);
                    c0828h.m1532b("US");
                    vector2.add(c0828h);
                    C1099a.f2141a.put("ShippingAddressId", Contact.RELATION_FRIEND);
                    C1099a.f2141a.put("AvailableAddresses", vector2);
                    PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity.LOGIN_SUCCESS));
                    return;
                }
                e.m2420a("quickPay", (Object) "false");
                m2317d();
            }
        } else if (view == this.f2165c) {
            new C0817f(instance2).show();
        } else if (view == this.f2168f) {
            C08051.m1497a(2);
        }
    }

    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
