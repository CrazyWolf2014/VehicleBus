package com.paypal.android.MEP.p021b;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout.LayoutParams;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPreapproval;
import com.paypal.android.MEP.PayPalReceiverDetails;
import com.paypal.android.MEP.p020a.C1102d;
import com.paypal.android.MEP.p020a.C1103e;
import com.paypal.android.MEP.p020a.C1106h;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0841l;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p024b.C0851f;
import com.paypal.android.p024b.C0858j;
import com.paypal.android.p024b.C1110c;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.achartengine.renderer.DefaultRenderer;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.MEP.b.b */
public final class C1278b extends C1110c implements OnTouchListener {
    private C0851f f2386e;
    private boolean f2387f;
    private GradientDrawable f2388g;
    private GradientDrawable f2389h;

    public C1278b(Context context, C0858j c0858j) {
        super(context);
        PayPal instance = PayPal.getInstance();
        PayPalAdvancedPayment payment = instance.getPayment();
        PayPalPreapproval preapproval = instance.getPreapproval();
        boolean z = c0858j instanceof C1102d;
        Object obj = ((c0858j instanceof C1106h) || (c0858j instanceof C1103e)) ? 1 : null;
        m1632a(new LayoutParams(-1, -2), 0);
        m1632a(new LayoutParams(-1, -2), 1);
        this.f2388g = C0835d.m1554a(-1, -1510918, -7829368);
        this.f2389h = C0835d.m1554a(-1, 14209956, -7829368);
        setBackgroundDrawable(this.f2388g);
        setPadding(5, 5, 5, 0);
        m2435a(C0836e.m1558a(14218, 463));
        m2437b(C0836e.m1558a(38432, 430));
        setOnTouchListener(this);
        View a;
        if (instance.getPayType() != 3) {
            int i;
            String description;
            boolean z2 = payment.getTotalShipping().compareTo(BigDecimal.ZERO) > 0 || payment.getTotalTax().compareTo(BigDecimal.ZERO) > 0 || instance.getDynamicAmountCalculationEnabled() || payment.hasPrimaryReceiever() || payment.getReceivers().size() > 1;
            this.f2387f = z2;
            if (!(((PayPalReceiverDetails) payment.getReceivers().get(0)).getInvoiceData() == null || ((PayPalReceiverDetails) payment.getReceivers().get(0)).getInvoiceData().getInvoiceItems() == null)) {
                for (i = 0; i < ((PayPalReceiverDetails) payment.getReceivers().get(0)).getInvoiceData().getInvoiceItems().size(); i++) {
                    if (((PayPalInvoiceItem) ((PayPalReceiverDetails) payment.getReceivers().get(0)).getInvoiceData().getInvoiceItems().get(i)).isValid()) {
                        this.f2387f = true;
                    }
                }
            }
            this.a.setGravity(16);
            View a2 = obj != null ? C0836e.m1560a(context, "system-icon-confirmation.png") : (instance.isPersonalPayment() || instance.getTextType() == 1) ? C0836e.m1560a(context, "shopping-list-enabled.png") : C0836e.m1560a(context, "shopping-cart-enabled.png");
            this.a.addView(a2);
            this.f2386e = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_BOLD, 0.5f, 0.5f);
            if (obj != null) {
                if (instance.isPersonalPayment() || instance.getTextType() == 1) {
                    this.f2386e.m1627a(C0839h.m1568a("ANDROID_total_paid") + ":");
                } else {
                    this.f2386e.m1627a(C0839h.m1568a("ANDROID_receipt") + ":");
                }
            } else if (this.f2387f) {
                this.f2386e.m1627a(C0839h.m1568a("ANDROID_my_total") + ":");
            } else {
                description = ((PayPalReceiverDetails) payment.getReceivers().get(0)).getDescription();
                if (description == null || description.length() <= 0) {
                    this.f2386e.m1627a(C0839h.m1568a("ANDROID_my_total") + ":");
                } else {
                    this.f2386e.m1627a(description + ":");
                }
            }
            this.f2386e.m1626a((int) DefaultRenderer.BACKGROUND_COLOR);
            if (payment.hasPrimaryReceiever()) {
                this.f2386e.m1629b(C0841l.m1579a(payment.getPrimaryReceiver().getTotal(), payment.getCurrencyType()));
            } else {
                this.f2386e.m1629b(C0841l.m1579a(payment.getTotal(), payment.getCurrencyType()));
            }
            this.f2386e.m1628b(-13408768);
            this.f2386e.setPadding(5, 0, 5, 0);
            this.a.addView(this.f2386e);
            this.a.addView(this.c);
            this.c.setVisibility(0);
            if (this.f2387f) {
                this.b.setOrientation(1);
                a2 = new View(context);
                a2.setLayoutParams(new LayoutParams(-1, 1));
                a2.setBackgroundColor(-7829368);
                this.b.addView(a2);
                if (payment.hasPrimaryReceiever()) {
                    this.b.addView(C1279c.m2584a(context, payment.getPrimaryReceiver(), payment.getCurrencyType()));
                } else {
                    ArrayList receivers = payment.getReceivers();
                    if (receivers.size() == 1) {
                        this.b.addView(C1279c.m2584a(context, (PayPalReceiverDetails) receivers.get(0), payment.getCurrencyType()));
                    } else {
                        for (i = 0; i < receivers.size(); i++) {
                            this.b.addView(new C1279c(context, (PayPalReceiverDetails) receivers.get(i), payment.getCurrencyType()));
                        }
                    }
                }
                a2 = new View(context);
                a2.setLayoutParams(new LayoutParams(-1, 1));
                a2.setBackgroundColor(-7829368);
                this.b.addView(a2);
                a = C0845o.m1618a(C0844a.HELVETICA_14_BOLD, context);
                a.setGravity(5);
                description = payment.hasPrimaryReceiever() ? C0841l.m1579a(payment.getPrimaryReceiver().getTotal(), payment.getCurrencyType()) : C0841l.m1578a(payment.getTotal().toString(), payment.getCurrencyType());
                if (description.contains(payment.getCurrencyType())) {
                    a.setText(C0839h.m1568a("ANDROID_total") + ": " + description);
                } else {
                    a.setText(C0839h.m1568a("ANDROID_total") + " (" + payment.getCurrencyType() + "): " + description);
                }
                this.b.addView(a);
                a2 = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                a2.setLayoutParams(new LayoutParams(-1, -2));
                a2.setGravity(5);
                a2.setText(C0839h.m1568a("ANDROID_shipping_tax_estimated_note"));
                if (instance.getDynamicAmountCalculationEnabled() && z) {
                    this.b.addView(a2);
                    return;
                }
                return;
            }
            setClickable(false);
            return;
        }
        this.f2387f = true;
        this.a.setGravity(16);
        this.a.addView(obj != null ? C0836e.m1560a(context, "system-icon-confirmation.png") : C0836e.m1560a(context, "shopping-list-enabled.png"));
        this.f2386e = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_BOLD, 0.5f, 0.5f);
        if (obj != null) {
            this.f2386e.m1627a(C0839h.m1568a("ANDROID_receipt") + ":");
            this.f2386e.m1629b(XmlPullParser.NO_NAMESPACE);
        } else {
            this.f2386e.m1627a(C0839h.m1568a("ANDROID_billing_summary"));
            this.f2386e.m1629b(XmlPullParser.NO_NAMESPACE);
        }
        this.f2386e.m1626a((int) DefaultRenderer.BACKGROUND_COLOR);
        this.f2386e.m1628b(-13408768);
        this.a.addView(this.f2386e);
        this.a.addView(this.c);
        this.c.setVisibility(0);
        PayPalPreapproval preapproval2 = instance.getPreapproval();
        if (!z) {
            this.b.setOrientation(1);
            a = new View(context);
            a.setLayoutParams(new LayoutParams(-1, 1));
            a.setBackgroundColor(-7829368);
            this.b.addView(a);
            a = new C0851f(context, C0844a.HELVETICA_12_BOLD, C0844a.HELVETICA_12_NORMAL);
            a.m1627a(C0839h.m1568a("ANDROID_name") + ":");
            a.m1629b(preapproval.getMerchantName());
            this.b.addView(a);
            if (preapproval2.getStartDate() != null && preapproval2.getStartDate().length() > 0) {
                a = new C0851f(context, C0844a.HELVETICA_12_BOLD, C0844a.HELVETICA_12_NORMAL);
                a.m1627a(C0839h.m1568a("ANDROID_start_date") + ":");
                a.m1629b(C0839h.m1569a(preapproval2.getStartDate(), 2));
                this.b.addView(a);
            }
            if (preapproval2.getEndDate() != null && preapproval2.getEndDate().length() > 0) {
                a = new C0851f(context, C0844a.HELVETICA_12_BOLD, C0844a.HELVETICA_12_NORMAL);
                a.m1627a(C0839h.m1568a("ANDROID_end_date") + ":");
                a.m1629b(C0839h.m1569a(preapproval2.getEndDate(), 2));
                this.b.addView(a);
            }
        }
    }

    public final void m2582a(boolean z, boolean z2) {
        if (z && this.f2387f) {
            setFocusable(true);
            setClickable(true);
            this.c.setVisibility(0);
            this.f2386e.m1626a((int) DefaultRenderer.BACKGROUND_COLOR);
            this.f2386e.m1628b(-13408768);
            return;
        }
        setFocusable(false);
        setClickable(false);
        this.b.setVisibility(8);
        this.c.setVisibility(8);
        if (z2) {
            this.f2386e.m1626a(-7829368);
            this.f2386e.m1628b(-7829368);
        }
    }

    public final void m2583b(boolean z) {
        if (z) {
            setBackgroundDrawable(this.f2389h);
        } else {
            setBackgroundDrawable(this.f2388g);
        }
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        if (isFocusable()) {
            switch (motionEvent.getAction()) {
                case KEYRecord.OWNER_USER /*0*/:
                    setBackgroundDrawable(this.f2389h);
                    break;
                default:
                    setBackgroundDrawable(this.f2388g);
                    break;
            }
        }
        return false;
    }
}
