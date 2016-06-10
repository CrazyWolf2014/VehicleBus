package com.paypal.android.MEP.p021b;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalReceiverDetails;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0841l;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import com.paypal.android.p024b.C0851f;
import com.paypal.android.p024b.C1110c;
import java.math.BigDecimal;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.MEP.b.c */
public final class C1279c extends C1110c implements OnTouchListener {
    public C1279c(Context context, PayPalReceiverDetails payPalReceiverDetails, String str) {
        BigDecimal subtotal;
        int i = 0;
        super(context);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.setMargins(3, 3, 3, 3);
        m1632a(layoutParams, 0);
        m1632a(layoutParams, 1);
        setBackgroundColor(0);
        m2435a(C0836e.m1558a(124846, 2921));
        m2437b(C0836e.m1558a(61183, 2927));
        setOnTouchListener(this);
        this.a.setPadding(5, 0, 5, 0);
        this.a.setBackgroundColor(-2763307);
        this.a.setGravity(16);
        this.a.addView(this.c);
        this.c.setVisibility(0);
        View c0851f = new C0851f(context, C0844a.HELVETICA_14_BOLD, C0844a.HELVETICA_14_BOLD);
        c0851f.setPadding(3, 0, 0, 0);
        String merchantName = payPalReceiverDetails.getMerchantName();
        if (merchantName == null || merchantName.length() == 0) {
            merchantName = payPalReceiverDetails.getRecipient();
        }
        String a = C0841l.m1579a(payPalReceiverDetails.getTotal(), str);
        c0851f.m1627a(merchantName);
        c0851f.m1629b(a);
        this.a.addView(c0851f);
        this.b.setPadding(10, 0, 0, 0);
        this.b.setOrientation(1);
        if (payPalReceiverDetails.getInvoiceData() == null || payPalReceiverDetails.getInvoiceData().getInvoiceItems() == null || payPalReceiverDetails.getInvoiceData().getInvoiceItems().size() <= 0) {
            subtotal = payPalReceiverDetails.getSubtotal();
            if (subtotal != null) {
                View c0851f2 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                c0851f2.m1627a(C0839h.m1568a("ANDROID_total"));
                c0851f2.m1629b(C0841l.m1579a(subtotal, str));
                this.b.addView(c0851f2);
            }
        } else {
            while (i < payPalReceiverDetails.getInvoiceData().getInvoiceItems().size()) {
                PayPalInvoiceItem payPalInvoiceItem = (PayPalInvoiceItem) payPalReceiverDetails.getInvoiceData().getInvoiceItems().get(i);
                String name = payPalInvoiceItem.getName();
                a = payPalInvoiceItem.getID();
                BigDecimal totalPrice = payPalInvoiceItem.getTotalPrice();
                BigDecimal unitPrice = payPalInvoiceItem.getUnitPrice();
                int quantity = payPalInvoiceItem.getQuantity();
                if (payPalInvoiceItem.isValid()) {
                    View c0851f3;
                    if (name != null && name.length() > 0) {
                        c0851f = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                        c0851f.m1627a(C0839h.m1568a("ANDROID_item") + ": " + payPalInvoiceItem.getName());
                        if (totalPrice == null || totalPrice.toString().length() <= 0) {
                            c0851f.m1629b(XmlPullParser.NO_NAMESPACE);
                        } else {
                            c0851f.m1629b(C0841l.m1579a(totalPrice, str));
                        }
                        this.b.addView(c0851f);
                    } else if (totalPrice != null && totalPrice.compareTo(BigDecimal.ZERO) > 0) {
                        c0851f3 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                        c0851f3.m1627a(C0839h.m1568a("ANDROID_item") + ": " + C0839h.m1568a("ANDROID_item") + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (i + 1));
                        c0851f3.m1629b(totalPrice.toString());
                        this.b.addView(c0851f3);
                    }
                    if (a != null && a.length() > 0) {
                        c0851f3 = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                        c0851f3.setText(C0839h.m1568a("ANDROID_item_num") + ": " + a);
                        this.b.addView(c0851f3);
                    }
                    if (unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) > 0) {
                        c0851f3 = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                        c0851f3.setText(C0839h.m1568a("ANDROID_item_price") + ": " + C0841l.m1579a(unitPrice, str));
                        this.b.addView(c0851f3);
                    }
                    if (quantity > 0) {
                        c0851f3 = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                        c0851f3.setText(C0839h.m1568a("ANDROID_quantity") + ": " + quantity);
                        this.b.addView(c0851f3);
                    }
                    if (i != payPalReceiverDetails.getInvoiceData().getInvoiceItems().size() - 1) {
                        c0851f3 = C0835d.m1555a(context, 5, 5);
                        c0851f3.setVisibility(4);
                        this.b.addView(c0851f3);
                    }
                }
                i++;
            }
        }
        subtotal = payPalReceiverDetails.getInvoiceData() != null ? payPalReceiverDetails.getInvoiceData().getTax() : null;
        if (subtotal != null && subtotal.compareTo(BigDecimal.ZERO) > 0) {
            c0851f2 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
            c0851f2.m1627a(C0839h.m1568a("ANDROID_tax"));
            c0851f2.m1629b(C0841l.m1579a(subtotal, str));
            this.b.addView(c0851f2);
        }
        subtotal = payPalReceiverDetails.getInvoiceData() != null ? payPalReceiverDetails.getInvoiceData().getShipping() : null;
        if (subtotal != null && subtotal.compareTo(BigDecimal.ZERO) > 0) {
            c0851f2 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
            c0851f2.m1627a(C0839h.m1568a("ANDROID_shipping"));
            c0851f2.m1629b(C0841l.m1579a(subtotal, str));
            this.b.addView(c0851f2);
        }
    }

    public static LinearLayout m2584a(Context context, PayPalReceiverDetails payPalReceiverDetails, String str) {
        BigDecimal subtotal;
        LinearLayout linearLayout = new LinearLayout(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.gravity = 1;
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setPadding(5, 0, 5, 5);
        linearLayout.setPadding(10, 0, 0, 0);
        linearLayout.setOrientation(1);
        if (payPalReceiverDetails.getInvoiceData() == null || payPalReceiverDetails.getInvoiceData().getInvoiceItems() == null || payPalReceiverDetails.getInvoiceData().getInvoiceItems().size() <= 0) {
            subtotal = payPalReceiverDetails.getSubtotal();
            if (subtotal != null) {
                View c0851f = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                c0851f.m1627a(C0839h.m1568a("ANDROID_total"));
                c0851f.m1629b(C0841l.m1579a(subtotal, str));
                linearLayout.addView(c0851f);
            }
        } else {
            for (int i = 0; i < payPalReceiverDetails.getInvoiceData().getInvoiceItems().size(); i++) {
                PayPalInvoiceItem payPalInvoiceItem = (PayPalInvoiceItem) payPalReceiverDetails.getInvoiceData().getInvoiceItems().get(i);
                String name = payPalInvoiceItem.getName();
                String id = payPalInvoiceItem.getID();
                BigDecimal totalPrice = payPalInvoiceItem.getTotalPrice();
                BigDecimal unitPrice = payPalInvoiceItem.getUnitPrice();
                int quantity = payPalInvoiceItem.getQuantity();
                if (payPalInvoiceItem.isValid()) {
                    View c0851f2;
                    if (name != null && name.length() > 0) {
                        View c0851f3 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                        c0851f3.m1627a(C0839h.m1568a("ANDROID_item") + ": " + payPalInvoiceItem.getName());
                        if (totalPrice == null || totalPrice.toString().length() <= 0) {
                            c0851f3.m1629b(XmlPullParser.NO_NAMESPACE);
                        } else {
                            c0851f3.m1629b(C0841l.m1579a(totalPrice, str));
                        }
                        linearLayout.addView(c0851f3);
                    } else if (totalPrice != null && totalPrice.compareTo(BigDecimal.ZERO) > 0) {
                        c0851f2 = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
                        c0851f2.m1627a(C0839h.m1568a("ANDROID_item") + ": " + C0839h.m1568a("ANDROID_item") + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + (i + 1));
                        c0851f2.m1629b(totalPrice.toString());
                        linearLayout.addView(c0851f2);
                    }
                    if (id != null && id.length() > 0) {
                        c0851f2 = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                        c0851f2.setText(C0839h.m1568a("ANDROID_item_num") + ": " + id);
                        linearLayout.addView(c0851f2);
                    }
                    if (unitPrice != null && unitPrice.compareTo(BigDecimal.ZERO) > 0) {
                        c0851f2 = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                        c0851f2.setText(C0839h.m1568a("ANDROID_item_price") + ": " + C0841l.m1579a(unitPrice, str));
                        linearLayout.addView(c0851f2);
                    }
                    if (quantity > 0) {
                        c0851f2 = C0845o.m1618a(C0844a.HELVETICA_12_NORMAL, context);
                        c0851f2.setText(C0839h.m1568a("ANDROID_quantity") + ": " + quantity);
                        linearLayout.addView(c0851f2);
                    }
                    if (i != payPalReceiverDetails.getInvoiceData().getInvoiceItems().size() - 1) {
                        c0851f2 = C0835d.m1555a(context, 5, 5);
                        c0851f2.setVisibility(4);
                        linearLayout.addView(c0851f2);
                    }
                }
            }
        }
        subtotal = payPalReceiverDetails.getInvoiceData() != null ? payPalReceiverDetails.getInvoiceData().getTax() : null;
        if (subtotal != null && subtotal.compareTo(BigDecimal.ZERO) > 0) {
            c0851f = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
            c0851f.m1627a(C0839h.m1568a("ANDROID_tax"));
            c0851f.m1629b(C0841l.m1579a(subtotal, str));
            linearLayout.addView(c0851f);
        }
        subtotal = payPalReceiverDetails.getInvoiceData() != null ? payPalReceiverDetails.getInvoiceData().getShipping() : null;
        if (subtotal != null && subtotal.compareTo(BigDecimal.ZERO) > 0) {
            c0851f = new C0851f(context, C0844a.HELVETICA_14_NORMAL, C0844a.HELVETICA_14_NORMAL);
            c0851f.m1627a(C0839h.m1568a("ANDROID_shipping"));
            c0851f.m1629b(C0841l.m1579a(subtotal, str));
            linearLayout.addView(c0851f);
        }
        return linearLayout;
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
