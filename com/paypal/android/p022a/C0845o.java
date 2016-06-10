package com.paypal.android.p022a;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalReceiverDetails;
import org.achartengine.renderer.DefaultRenderer;

/* renamed from: com.paypal.android.a.o */
public final class C0845o {

    /* renamed from: com.paypal.android.a.o.a */
    public enum C0844a {
        HELVETICA_16_BOLD,
        HELVETICA_16_NORMAL,
        HELVETICA_14_BOLD,
        HELVETICA_14_NORMAL,
        HELVETICA_12_BOLD,
        HELVETICA_12_NORMAL,
        HELVETICA_10_BOLD,
        HELVETICA_10_NORMAL
    }

    public static TextView m1618a(C0844a c0844a, Context context) {
        Rect rect = new Rect(5, 2, 5, 3);
        Rect rect2 = new Rect(0, 0, 0, 0);
        TextView textView = new TextView(context);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2, 0.5f);
        layoutParams.setMargins(rect2.left, rect2.top, rect2.right, rect2.bottom);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(0);
        textView.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        textView.setGravity(3);
        textView.setPadding(rect.left, rect.top, rect.right, rect.bottom);
        if (c0844a == C0844a.HELVETICA_16_BOLD || c0844a == C0844a.HELVETICA_14_BOLD || c0844a == C0844a.HELVETICA_12_BOLD || c0844a == C0844a.HELVETICA_10_BOLD) {
            textView.setTypeface(Typeface.create("Helvetica", 1));
        } else if (c0844a != C0844a.HELVETICA_16_NORMAL && c0844a != C0844a.HELVETICA_14_NORMAL && c0844a != C0844a.HELVETICA_12_NORMAL && c0844a != C0844a.HELVETICA_10_NORMAL) {
            return null;
        } else {
            textView.setTypeface(Typeface.create("Helvetica", 0));
        }
        if (c0844a == C0844a.HELVETICA_16_BOLD || c0844a == C0844a.HELVETICA_16_NORMAL) {
            textView.setTextSize(16.0f);
        } else if (c0844a == C0844a.HELVETICA_14_BOLD || c0844a == C0844a.HELVETICA_14_NORMAL) {
            textView.setTextSize(14.0f);
        } else if (c0844a == C0844a.HELVETICA_12_BOLD || c0844a == C0844a.HELVETICA_12_NORMAL) {
            textView.setTextSize(12.0f);
        } else if (c0844a != C0844a.HELVETICA_10_BOLD && c0844a != C0844a.HELVETICA_10_NORMAL) {
            return null;
        } else {
            textView.setTextSize(10.0f);
        }
        return textView;
    }

    public static TextView m1619b(C0844a c0844a, Context context) {
        int i = 0;
        TextView a = C0845o.m1618a(c0844a, context);
        PayPal instance = PayPal.getInstance();
        PayPalAdvancedPayment payment = instance.getPayment();
        if (instance.getPayType() != 3) {
            CharSequence merchantName;
            if (instance.getPayType() != 0) {
                if (instance.getPayType() != 2) {
                    merchantName = PayPal.getInstance().getPayment().getMerchantName();
                    if (merchantName == null || merchantName.length() <= 0) {
                        for (int i2 = 0; i2 < payment.getReceivers().size(); i2++) {
                            merchantName = ((PayPalReceiverDetails) payment.getReceivers().get(i2)).getMerchantName();
                            if (merchantName != null && merchantName.length() > 0) {
                                a.setText(merchantName);
                                break;
                            }
                        }
                    } else {
                        a.setText(merchantName);
                    }
                    if (a.getText() == null || a.getText().length() <= 0) {
                        while (i < payment.getReceivers().size()) {
                            String recipient = ((PayPalReceiverDetails) payment.getReceivers().get(i)).getRecipient();
                            if (recipient != null && recipient.length() > 0) {
                                a.setText(C0839h.m1568a("ANDROID_send_to") + ": " + recipient);
                                break;
                            }
                            i++;
                        }
                    }
                } else {
                    merchantName = PayPal.getInstance().getPayment().getMerchantName();
                    if (merchantName == null || merchantName.length() <= 0) {
                        merchantName = payment.getPrimaryReceiver().getMerchantName();
                        if (merchantName == null || merchantName.length() <= 0) {
                            a.setText(C0839h.m1568a("ANDROID_send_to") + ": " + payment.getPrimaryReceiver().getRecipient());
                        } else {
                            a.setText(merchantName);
                        }
                    } else {
                        a.setText(merchantName);
                    }
                }
            } else if (instance.isPersonalPayment()) {
                a.setText(C0839h.m1568a("ANDROID_send_to") + ": " + ((PayPalReceiverDetails) payment.getReceivers().get(0)).getRecipient());
            } else {
                merchantName = PayPal.getInstance().getPayment().getMerchantName();
                if (merchantName == null || merchantName.length() <= 0) {
                    merchantName = ((PayPalReceiverDetails) payment.getReceivers().get(0)).getMerchantName();
                    if (merchantName == null || merchantName.length() <= 0) {
                        a.setText(C0839h.m1568a("ANDROID_send_to") + ": " + ((PayPalReceiverDetails) payment.getReceivers().get(0)).getRecipient());
                    } else {
                        a.setText(merchantName);
                    }
                } else {
                    a.setText(merchantName);
                }
            }
        } else {
            a.setText(instance.getPreapproval().getMerchantName());
        }
        a.setGravity(17);
        a.setPadding(5, 2, 5, 3);
        return a;
    }
}
