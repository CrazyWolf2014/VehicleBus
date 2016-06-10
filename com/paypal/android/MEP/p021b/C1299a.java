package com.paypal.android.MEP.p021b;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.googlecode.leptonica.android.Skew;
import com.ifoer.mine.Contact;
import com.paypal.android.MEP.C0811a.C0801b;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.p020a.C1099a;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p022a.C1107b;
import com.paypal.android.p022a.p023a.C0821a;
import com.paypal.android.p022a.p023a.C0823c;
import com.paypal.android.p022a.p023a.C0826f;
import com.paypal.android.p022a.p023a.C0827g;
import com.paypal.android.p022a.p023a.C0828h;
import com.paypal.android.p022a.p023a.C0831k;
import com.paypal.android.p024b.C0850e;
import com.paypal.android.p024b.C0851f;
import com.paypal.android.p024b.C1280k;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.Vector;
import org.achartengine.renderer.DefaultRenderer;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.MEP.b.a */
public final class C1299a extends C1280k implements OnTouchListener, C0801b {
    private static C0850e f2418l;
    private static boolean f2419q;
    boolean f2420e;
    Vector<Hashtable> f2421f;
    private GradientDrawable f2422g;
    private GradientDrawable f2423h;
    private C0813a f2424i;
    private LinearLayout f2425j;
    private LinearLayout f2426k;
    private TextView f2427m;
    private C1099a f2428n;
    private Vector<String> f2429o;
    private Vector<String> f2430p;
    private int f2431r;
    private C0814b f2432s;
    private OnClickListener f2433t;
    private String f2434u;

    /* renamed from: com.paypal.android.MEP.b.a.1 */
    static /* synthetic */ class C08121 {
        static final /* synthetic */ int[] f1483a;

        static {
            f1483a = new int[C0813a.values().length];
            try {
                f1483a[C0813a.PAYMENT_DETAILS_FUNDING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1483a[C0813a.PAYMENT_DETAILS_FEES.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f1483a[C0813a.PAYMENT_DETAILS_SHIPPING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* renamed from: com.paypal.android.MEP.b.a.a */
    public enum C0813a {
        PAYMENT_DETAILS_FUNDING,
        PAYMENT_DETAILS_FEES,
        PAYMENT_DETAILS_SHIPPING
    }

    /* renamed from: com.paypal.android.MEP.b.a.b */
    public interface C0814b {
        void m1504a(C1299a c1299a, int i);
    }

    static {
        f2418l = null;
        f2419q = false;
    }

    public C1299a(Context context, C0813a c0813a, C1099a c1099a) {
        super(context);
        this.f2426k = null;
        this.f2427m = null;
        this.f2428n = null;
        this.f2429o = new Vector(3);
        this.f2430p = new Vector(3);
        this.f2420e = false;
        this.f2421f = null;
        this.f2431r = 0;
        this.f2432s = null;
        this.f2433t = new C0815d(this);
        this.f2434u = null;
        setOnTouchListener(this);
        this.f2428n = c1099a;
        this.f2424i = c0813a;
        setBackgroundColor(-16711681);
        m1632a(new LayoutParams(-1, -2), 0);
        m1632a(new LayoutParams(-1, -2), 1);
        this.f2422g = C0835d.m1554a(-1, -1510918, -3154193);
        this.f2423h = C0835d.m1554a(-1, -4336918, -3154193);
        setBackgroundDrawable(this.f2422g);
        setGravity(16);
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-10066330, -3487030});
        gradientDrawable.setCornerRadii(new float[]{0.0f, 0.0f, 0.0f, 0.0f, Skew.SWEEP_DELTA, Skew.SWEEP_DELTA, Skew.SWEEP_DELTA, Skew.SWEEP_DELTA});
        gradientDrawable.setStroke(2, -10066330);
        m2587c(gradientDrawable);
        m2435a(C0836e.m1558a(106169, 464));
        m2437b(C0836e.m1558a(113545, 430));
        this.a.setOrientation(1);
        View a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(0);
        a.setGravity(16);
        View a2 = C0845o.m1618a(C0844a.HELVETICA_14_BOLD, context);
        r0 = this.f2424i == C0813a.PAYMENT_DETAILS_FUNDING ? C0839h.m1568a("ANDROID_funding") + ":" : this.f2424i == C0813a.PAYMENT_DETAILS_FEES ? C0839h.m1568a("ANDROID_fee") + ":" : PayPal.getInstance().getTextType() == 1 ? C0839h.m1568a("ANDROID_mailing_address") + ":" : C0839h.m1568a("ANDROID_ship_to") + ":";
        a2.setText(r0);
        a2.setGravity(3);
        a.addView(a2);
        a.addView(this.c);
        this.c.setVisibility(0);
        this.a.addView(a);
        this.f2426k = C0835d.m1555a(context, -1, -2);
        this.f2426k.setOrientation(1);
        m2676a(context, this.f2426k);
        this.a.addView(this.f2426k);
        this.f2425j = C0835d.m1555a(context, -1, -2);
        this.f2425j.setOrientation(0);
        this.f2425j.setGravity(16);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(1);
        a.setGravity(1);
        this.f2427m = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
        this.f2427m.setLayoutParams(new LayoutParams(-1, -1));
        this.f2427m.setTextColor(-13408615);
        this.f2427m.setText(C0839h.m1568a("ANDROID_getting_information"));
        this.f2427m.setGravity(1);
        this.f2427m.setSingleLine(false);
        a.addView(this.f2427m);
        if (this.f2424i == C0813a.PAYMENT_DETAILS_FUNDING) {
            if (f2418l == null) {
                f2418l = new C0850e(context);
            } else {
                ((LinearLayout) f2418l.getParent()).removeAllViews();
            }
            f2418l.setGravity(1);
            this.f2425j.addView(f2418l);
            this.f2425j.addView(a);
            this.f2425j.setVisibility(8);
        }
        this.a.addView(this.f2425j);
        if (this.f2424i == C0813a.PAYMENT_DETAILS_FEES) {
            m2436a(false);
        }
        f2419q = false;
    }

    private Button m2674a(Context context, String str, int i) {
        Button button = new Button(context);
        button.setText(str);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, C0835d.m1557b());
        layoutParams.setMargins(0, 3, 0, 2);
        button.setLayoutParams(layoutParams);
        button.setGravity(17);
        Drawable stateListDrawable = new StateListDrawable();
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-328451, -4336918});
        Drawable gradientDrawable2 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-6702886, -11966331});
        Drawable gradientDrawable3 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-13605994, -16764058});
        Drawable gradientDrawable4 = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{2140780762, 2135517317});
        gradientDrawable.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable.setStroke(1, -6307088);
        gradientDrawable2.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable2.setStroke(1, -10650469);
        gradientDrawable3.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable3.setStroke(1, -16764058);
        gradientDrawable4.setCornerRadius(Skew.SWEEP_DELTA);
        gradientDrawable4.setStroke(1, 2136833179);
        stateListDrawable.addState(new int[]{16842910, -16842919, -16842908}, gradientDrawable);
        stateListDrawable.addState(new int[]{16842910, -16842919, 16842908}, gradientDrawable2);
        stateListDrawable.addState(new int[]{16842910, 16842919, -16842908}, gradientDrawable3);
        stateListDrawable.addState(new int[]{16842910, 16842919, 16842908}, gradientDrawable3);
        stateListDrawable.addState(new int[]{-16842910, -16842919, -16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, -16842919, 16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, 16842919, -16842908}, gradientDrawable4);
        stateListDrawable.addState(new int[]{-16842910, 16842919, 16842908}, gradientDrawable4);
        button.setBackgroundDrawable(stateListDrawable);
        button.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        button.setFocusable(true);
        button.setOnClickListener(this.f2433t);
        if (this.f2424i == C0813a.PAYMENT_DETAILS_FUNDING) {
            button.setId(2130706432 | i);
        } else if (this.f2424i == C0813a.PAYMENT_DETAILS_FEES) {
            button.setId(2113929216 | i);
        } else if (this.f2424i == C0813a.PAYMENT_DETAILS_SHIPPING) {
            button.setId(2097152000 | i);
        }
        return button;
    }

    private void m2676a(Context context, LinearLayout linearLayout) {
        Hashtable g = PayPal.getInstance().getServer() == 2 ? C1099a.f2141a : C1107b.m2383e().m2424g();
        Vector vector;
        String b;
        String b2;
        BigDecimal bigDecimal;
        if (this.f2424i == C0813a.PAYMENT_DETAILS_FUNDING) {
            C0823c c0823c;
            vector = (Vector) g.get("FundingPlans");
            if (vector == null || vector.size() == 0) {
                c0823c = (C0823c) g.get("DefaultFundingPlan");
            } else {
                C0823c c0823c2 = (C0823c) vector.elementAt(0);
                try {
                    c0823c = (C0823c) vector.elementAt(Integer.parseInt((String) g.get("FundingPlanId")));
                } catch (Exception e) {
                    c0823c = c0823c2;
                }
            }
            for (int i = 0; i < c0823c.f1521d.size(); i++) {
                View a;
                Object obj;
                C0831k c0831k = (C0831k) c0823c.f1521d.get(i);
                b = c0831k.f1556a.m1515b();
                BigDecimal a2 = c0831k.f1556a.m1513a();
                String c = c0831k.f1557b.m1527c();
                b2 = c0831k.f1557b.m1525b();
                if (b2 == null) {
                    b2 = XmlPullParser.NO_NAMESPACE;
                }
                View c0851f = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                Object obj2 = !PayPal.getInstance().getLanguage().contains("fr_") ? 1 : null;
                if (c.indexOf("BALANCE") != -1) {
                    if (obj2 != null) {
                        c0851f.m1627a(c0831k.f1557b.m1523a() + " (" + b + "):");
                    } else {
                        c0851f.m1627a(c0831k.f1557b.m1523a() + " (" + b + ") :");
                    }
                } else if (c.equals("BANK_DELAYED")) {
                    if (b2 == null || b2.length() <= 0) {
                        if (obj2 != null) {
                            c0851f.m1627a(c0831k.f1557b.m1523a() + " (" + C0839h.m1568a("ANDROID_echeck") + "):");
                        } else {
                            c0851f.m1627a(c0831k.f1557b.m1523a() + " (" + C0839h.m1568a("ANDROID_echeck") + ") :");
                        }
                    } else if (obj2 != null) {
                        c0851f.m1627a(c0831k.f1557b.m1523a() + " x" + b2 + " (" + C0839h.m1568a("ANDROID_echeck") + "):");
                    } else {
                        c0851f.m1627a(c0831k.f1557b.m1523a() + " x" + b2 + " (" + C0839h.m1568a("ANDROID_echeck") + ") :");
                    }
                } else if (c.equals("BANK_INSTANT")) {
                    if (b2 == null || b2.length() <= 0) {
                        c0851f.m1627a(c0831k.f1557b.m1523a() + ":");
                    } else {
                        c0851f.m1627a(c0831k.f1557b.m1523a() + " x" + b2 + ":");
                    }
                } else if (!c.equals("CREDITCARD") && !c.equals("DEBITCARD")) {
                    c0851f.m1627a(c0831k.f1557b.m1523a());
                } else if (b2 == null || b2.length() <= 0) {
                    c0851f.m1627a(c0831k.f1557b.m1523a() + ":");
                } else {
                    c0851f.m1627a(c0831k.f1557b.m1523a() + " x" + b2 + ":");
                }
                c0851f.m1629b(C0839h.m1570a(a2, b));
                linearLayout.addView(c0851f);
                if (c.equals("BANK_DELAYED")) {
                    a = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                    a.setLayoutParams(new LayoutParams(-1, -2, 0.5f));
                    a.setPadding(2, 2, 2, 2);
                    a.setBackgroundColor(0);
                    a.setText(C0839h.m1568a("ANDROID_echeck_note"));
                    linearLayout.addView(a);
                }
                bigDecimal = new BigDecimal(Contact.RELATION_ASK);
                if (!(c0823c.f1519b == null || c0823c.f1519b.m1513a() == null)) {
                    bigDecimal = c0823c.f1519b.m1513a();
                }
                BigDecimal total = PayPal.getInstance().getPayment().getTotal();
                if (b.equals(PayPal.getInstance().getPayment().getCurrencyType())) {
                    obj = a2.compareTo(total.add(bigDecimal)) > 0 ? 1 : null;
                } else {
                    total = c0823c.f1518a.m1513a().subtract(c0823c.f1520c.f1539b.m1513a());
                    BigDecimal a3 = c0823c.f1520c.f1538a.m1513a();
                    int i2 = 0;
                    while (i2 < c0823c.f1521d.size()) {
                        c0831k = (C0831k) c0823c.f1521d.elementAt(i2);
                        if (c0823c.f1518a.m1515b().equals(c0831k.f1556a.m1515b())) {
                            BigDecimal bigDecimal2 = a3;
                            a3 = total.subtract(c0831k.f1556a.m1513a());
                            bigDecimal = bigDecimal2;
                        } else {
                            bigDecimal = a3.subtract(c0831k.f1556a.m1513a());
                            a3 = total;
                        }
                        i2++;
                        total = a3;
                        a3 = bigDecimal;
                    }
                    obj = (total.compareTo(BigDecimal.ZERO) == 0 && a3.compareTo(BigDecimal.ZERO) == 0) ? null : 1;
                }
                if (obj != null) {
                    a = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                    a.setLayoutParams(new LayoutParams(-1, -2, 0.5f));
                    a.setPadding(2, 2, 2, 2);
                    a.setBackgroundColor(0);
                    a.setText(C0839h.m1568a("ANDROID_negative_balance"));
                    linearLayout.addView(a);
                }
            }
            C0826f c0826f = c0823c.f1520c;
            if (c0826f != null) {
                float parseFloat = Float.parseFloat(c0826f.m1521a());
                View c0851f2 = new C0851f(context, C0844a.HELVETICA_12_NORMAL, C0844a.HELVETICA_12_NORMAL);
                c0851f2.m1627a("1 " + c0826f.f1538a.m1515b() + " = " + parseFloat + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + c0826f.f1539b.m1515b());
                c0851f2.m1629b(XmlPullParser.NO_NAMESPACE);
                linearLayout.addView(c0851f2);
            }
            boolean z = true;
            if (vector != null && vector.size() > 0) {
                z = vector.size() > 1;
            }
            if (((C0831k) c0823c.f1521d.elementAt(0)).f1557b.m1527c().equals("BALANCE") && c0823c.f1521d.size() == 1) {
                z = false;
            }
            m2436a(z);
        } else if (this.f2424i == C0813a.PAYMENT_DETAILS_FEES) {
            try {
                vector = (Vector) g.get("FundingPlans");
                C0823c c0823c3 = (vector == null || vector.size() == 0) ? (C0823c) g.get("DefaultFundingPlan") : (C0823c) vector.elementAt(Integer.parseInt((String) g.get("FundingPlanId")));
                if (c0823c3 != null) {
                    C0821a c0821a = c0823c3.f1519b;
                    if (c0821a != null) {
                        bigDecimal = c0821a.m1513a();
                        String b3 = c0821a.m1515b();
                        View c0851f3 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                        if (bigDecimal.compareTo(BigDecimal.ZERO) > 0) {
                            c0851f3.m1627a(C0839h.m1568a("ANDROID_i_pay"));
                        } else {
                            c0851f3.m1627a(C0839h.m1568a("ANDROID_free"));
                        }
                        c0851f3.m1629b(C0839h.m1570a(bigDecimal, b3));
                        linearLayout.addView(c0851f3);
                        if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
                            m2436a(false);
                        }
                    } else {
                        View c0851f4 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                        c0851f4.m1627a(C0839h.m1568a("ANDROID_free"));
                        linearLayout.addView(c0851f4);
                    }
                    m2436a(false);
                }
            } catch (Exception e2) {
            }
        } else if (this.f2424i == C0813a.PAYMENT_DETAILS_SHIPPING) {
            vector = (Vector) g.get("AvailableAddresses");
            String str = (String) g.get("ShippingAddressId");
            if (vector != null && vector.size() > 0) {
                C0828h c0828h = null;
                for (int i3 = 0; i3 < vector.size(); i3++) {
                    c0828h = (C0828h) vector.elementAt(i3);
                    if (c0828h.m1543h().equals(str)) {
                        break;
                    }
                }
                if (c0828h != null) {
                    str = c0828h.m1528a();
                    b2 = c0828h.m1535d();
                    String e3 = c0828h.m1537e();
                    b = c0828h.m1531b();
                    String g2 = c0828h.m1541g();
                    String f = c0828h.m1539f();
                    View c0851f5 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                    StringBuffer stringBuffer = new StringBuffer();
                    if (str != null && str.length() > 0) {
                        stringBuffer.append(str + SpecilApiUtil.LINE_SEP);
                    }
                    if (b2 != null && b2.length() > 0) {
                        stringBuffer.append(b2);
                        if (e3 != null && e3.length() > 0) {
                            stringBuffer.append(", ");
                        }
                    }
                    if (e3 != null && e3.length() > 0) {
                        stringBuffer.append(e3);
                    }
                    if ((b2 != null && b2.length() > 0) || (e3 != null && e3.length() > 0)) {
                        stringBuffer.append(SpecilApiUtil.LINE_SEP);
                    }
                    if (b != null && b.length() > 0) {
                        stringBuffer.append(b);
                        if ((g2 != null && g2.length() > 0) || (f != null && f.length() > 0)) {
                            stringBuffer.append(", ");
                        }
                    }
                    if (g2 != null && g2.length() > 0) {
                        stringBuffer.append(g2);
                        if (f != null && f.length() > 0) {
                            stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                        }
                    }
                    if (f != null && f.length() > 0) {
                        stringBuffer.append(f);
                    }
                    c0851f5.m1627a(stringBuffer.toString());
                    m2436a(vector.size() > 1);
                    linearLayout.addView(c0851f5);
                }
            }
        }
    }

    private void m2679c(boolean z) {
        if (z) {
            this.f2426k.setVisibility(8);
            this.f2425j.setVisibility(0);
            f2418l.m1624a();
            return;
        }
        f2418l.m1625b();
        this.f2425j.setVisibility(8);
        this.f2426k.setVisibility(0);
    }

    private void m2680e() {
        this.f2421f = new Vector();
        Vector vector = (Vector) (PayPal.getInstance().getServer() == 2 ? C1099a.f2141a : C1107b.m2383e().m2424g()).get("FundingPlans");
        for (int i = 0; i < vector.size(); i++) {
            Object obj;
            C0823c c0823c = (C0823c) vector.get(i);
            C0831k c0831k = (C0831k) c0823c.f1521d.get(0);
            C0827g c0827g = c0831k.f1557b;
            String c = c0827g.m1527c();
            String a = c0827g.m1523a();
            if (c.equals("BALANCE")) {
                obj = a + "(" + c0831k.f1556a.m1515b() + ")";
            } else if ((c.equals("BANK_DELAYED") || c.equals("BANK_INSTANT") || c.equals("CREDITCARD") || c.equals("DEBITCARD")) && c0827g.m1525b() != null && c0827g.m1525b().length() > 0) {
                obj = a + " x" + c0827g.m1525b();
            } else {
                String str = a;
            }
            if (c.equals("BANK_DELAYED")) {
                obj = obj + " (" + C0839h.m1568a("ANDROID_echeck") + ")";
            }
            Hashtable hashtable = new Hashtable();
            hashtable.put("label", obj);
            hashtable.put("plan", c0823c);
            this.f2421f.add(hashtable);
        }
        C08051.m1498b();
    }

    public final void m2681a(int i) {
        this.f2431r = i;
        if (i == 1) {
            switch (C08121.f1483a[this.f2424i.ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    Vector vector = (Vector) (PayPal.getInstance().getServer() == 2 ? C1099a.f2141a : C1107b.m2383e().m2424g()).get("FundingPlans");
                    if (vector == null || vector.size() == 0) {
                        m2679c(true);
                        C1107b.m2383e().m2420a("delegate", (Object) this);
                        C1107b.m2383e().m2417a(5);
                        return;
                    }
                    m2680e();
                    super.m2434a(i);
                    return;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    int id;
                    m2588d();
                    String str = (String) ((Hashtable) ((Vector) C1107b.m2383e().m2424g().get("PricingDetails")).get(0)).get("FeeBearer");
                    View a = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, getContext());
                    a.setLayoutParams(new LayoutParams(-2, -2));
                    a.setBackgroundColor(0);
                    a.setText(C0839h.m1568a("ANDROID_choose_who_pays_the_fee") + ":");
                    a.setTextColor(-1);
                    m2585a(a);
                    if (str.compareTo("ApplyFeeToReceiver") == 0) {
                        a = m2674a(getContext(), C0839h.m1568a("ANDROID_i_pay"), 0);
                        id = a.getId();
                        m2585a(a);
                        a.setNextFocusUpId(getId());
                    } else {
                        a = m2674a(getContext(), C0839h.m1568a("NDROID_recipient_pays"), 1);
                        id = a.getId();
                        m2585a(a);
                        a.setNextFocusUpId(getId());
                    }
                    setNextFocusDownId(id);
                    if (this.f2432s != null) {
                        this.f2432s.m1504a(this, id);
                    }
                    super.m2434a(i);
                    return;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    if (PayPal.getInstance().getServer() != 2) {
                        if (((Vector) PayPalActivity._networkHandler.m2424g().get("AvailableAddresses")) == null) {
                            this.f2421f = null;
                        } else {
                            this.f2421f = new Vector();
                        }
                    } else if (((Vector) C1099a.f2141a.get("AvailableAddresses")) == null) {
                        this.f2421f = null;
                    } else {
                        this.f2421f = new Vector();
                    }
                    C08051.m1498b();
                    super.m2434a(i);
                    return;
                default:
                    return;
            }
        }
        m2588d();
        this.f2421f = null;
        super.m2434a(i);
    }

    public final void m2682a(int i, Object obj) {
        f2419q = false;
        switch (i) {
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                C1107b.m2383e().m2417a(6);
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                m2680e();
                if (((Vector) C1107b.m2383e().m2421c("FundingPlans")).size() == 1) {
                    m2436a(false);
                    PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
                }
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                try {
                    if (PayPal.getInstance().getServer() != 2) {
                        C1099a.f2141a = C1107b.m2383e().m2424g();
                    }
                    PayPalActivity.getInstance().sendBroadcast(new Intent(PayPalActivity.CREATE_PAYMENT_SUCCESS));
                } catch (Throwable th) {
                }
            default:
        }
    }

    public final void m2683a(C0814b c0814b) {
        this.f2432s = c0814b;
    }

    public final void m2684a(String str, Object obj) {
    }

    public final C0813a m2685b() {
        return this.f2424i;
    }

    public final void m2686b(boolean z) {
        if (z) {
            setBackgroundDrawable(this.f2423h);
        } else {
            setBackgroundDrawable(this.f2422g);
        }
    }

    public final void m2687c() {
        if (this.f2434u != null) {
            this.f2428n.m2308d(this.f2434u);
            this.f2434u = null;
            m2679c(false);
        }
        if (this.f2421f != null) {
            m2679c(false);
            super.m2434a(1);
            m2588d();
            View a;
            int i;
            int i2;
            switch (C08121.f1483a[this.f2424i.ordinal()]) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    a = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, getContext());
                    a.setLayoutParams(new LayoutParams(-2, -2));
                    a.setBackgroundColor(0);
                    a.setText(C0839h.m1568a("ANDROID_change_funding") + ":");
                    a.setTextColor(-1);
                    if (this.f2421f.size() > 0) {
                        m2585a(a);
                    }
                    int i3 = 0;
                    i = 0;
                    boolean z = false;
                    int i4 = 0;
                    while (i3 < this.f2421f.size()) {
                        boolean z2;
                        try {
                            Hashtable hashtable = (Hashtable) this.f2421f.get(i3);
                            C0823c c0823c = (C0823c) hashtable.get("plan");
                            if (!((String) (PayPal.getInstance().getServer() == 2 ? C1099a.f2141a : C1107b.m2383e().m2424g()).get("FundingPlanId")).equals(c0823c.m1518a())) {
                                View a2 = m2674a(getContext(), (String) hashtable.get("label"), i4);
                                int id = a2.getId();
                                this.f2430p.add(c0823c.m1518a());
                                m2585a(a2);
                                if (!z) {
                                    setNextFocusDownId(id);
                                    z = true;
                                }
                                i4++;
                                i = id;
                            }
                            i2 = i;
                            z2 = z;
                            i = i4;
                        } catch (Throwable th) {
                            i2 = i;
                            z2 = z;
                            i = i4;
                        }
                        i3++;
                        z = z2;
                        i4 = i;
                        i = i2;
                    }
                    if (!(i == 0 || this.f2432s == null)) {
                        this.f2432s.m1504a(this, i);
                    }
                    if (i4 == 0) {
                        a.setText(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                    }
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    Hashtable g = PayPal.getInstance().getServer() == 2 ? C1099a.f2141a : C1107b.m2383e().m2424g();
                    Vector vector = (Vector) g.get("AvailableAddresses");
                    String str = (String) g.get("ShippingAddressId");
                    if (vector != null && vector.size() > 1) {
                        a = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, getContext());
                        a.setLayoutParams(new LayoutParams(-2, -2));
                        a.setBackgroundColor(0);
                        if (PayPal.getInstance().getTextType() == 1) {
                            a.setText(C0839h.m1568a("ANDROID_change_mailing_address_to") + ":");
                        } else {
                            a.setText(C0839h.m1568a("ANDROID_change_shipping_to") + ":");
                        }
                        a.setTextColor(-1);
                        m2585a(a);
                        this.f2429o.removeAllElements();
                        int i5 = 0;
                        boolean z3 = false;
                        i = 0;
                        while (i5 < vector.size()) {
                            C0828h c0828h = (C0828h) vector.elementAt(i5);
                            try {
                                String d = c0828h.m1535d();
                                String e = c0828h.m1537e();
                                String b = c0828h.m1531b();
                                String g2 = c0828h.m1541g();
                                String f = c0828h.m1539f();
                                d = d + (d.length() > 0 ? SpecilApiUtil.LINE_SEP : XmlPullParser.NO_NAMESPACE);
                                if (e != null) {
                                    d = d + e + (e.length() > 0 ? SpecilApiUtil.LINE_SEP : XmlPullParser.NO_NAMESPACE);
                                }
                                d = d + b;
                                if (g2 != null) {
                                    d = d + ", " + g2;
                                }
                                if (f != null) {
                                    d = d + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + f;
                                }
                                if (str == null || !str.equals(c0828h.m1543h())) {
                                    boolean z4;
                                    View a3 = m2674a(getContext(), d, i);
                                    int id2 = a3.getId();
                                    this.f2429o.add(c0828h.m1543h());
                                    m2585a(a3);
                                    if (z3) {
                                        z4 = z3;
                                    } else {
                                        setNextFocusDownId(id2);
                                        z4 = true;
                                    }
                                    boolean z5 = z4;
                                    i2 = i + 1;
                                    z3 = z5;
                                    i5++;
                                    i = i2;
                                } else {
                                    i2 = i;
                                    i5++;
                                    i = i2;
                                }
                            } catch (Throwable th2) {
                                i2 = i;
                            }
                        }
                        if (this.f2421f.size() == 0) {
                            View view = new View(getContext());
                            view.setMinimumWidth(10);
                            view.setMinimumHeight(10);
                            m2585a(view);
                        }
                        if (i == 0) {
                            a.setText(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                        }
                    }
                default:
            }
        }
    }

    public final void m2688d(String str) {
        this.f2434u = str;
        f2419q = false;
        C08051.m1498b();
    }

    public final void m2689l() {
        switch (C08121.f1483a[this.f2424i.ordinal()]) {
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (this.f2420e) {
                    this.f2420e = false;
                    C1107b.m2383e().m2420a("delegate", (Object) this);
                }
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                C1107b.m2383e().m2420a("delegate", (Object) this);
                if (this.f2420e) {
                    this.f2420e = false;
                } else {
                    C1107b.m2383e().m2417a(7);
                }
            default:
        }
    }

    public final void onClick(View view) {
        if (this.f2431r == 0) {
            this.f2428n.m2307d();
        }
        super.onClick(view);
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case KEYRecord.OWNER_USER /*0*/:
                if (isClickable()) {
                    setBackgroundDrawable(this.f2423h);
                    break;
                }
                break;
            default:
                setBackgroundDrawable(this.f2422g);
                break;
        }
        return false;
    }
}
