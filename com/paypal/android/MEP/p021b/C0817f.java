package com.paypal.android.MEP.p021b;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalAdvancedPayment;
import com.paypal.android.MEP.PayPalReceiverDetails;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import org.achartengine.renderer.DefaultRenderer;

/* renamed from: com.paypal.android.MEP.b.f */
public final class C0817f extends Dialog implements OnClickListener {
    private Button f1501a;
    private Button f1502b;

    public C0817f(Context context) {
        CharSequence merchantName;
        super(context);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        requestWindowFeature(1);
        View a = C0835d.m1555a(context, -1, -1);
        a.setOrientation(1);
        a.setGravity(1);
        a.setPadding(10, 5, 10, 5);
        Drawable gradientDrawable = new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{-1510918, -4336918});
        gradientDrawable.setCornerRadius(3.0f);
        gradientDrawable.setStroke(0, 0);
        a.setBackgroundDrawable(gradientDrawable);
        View a2 = C0845o.m1618a(C0844a.HELVETICA_16_BOLD, context);
        a2.setText(C0839h.m1568a("ANDROID_cancel_transaction"));
        a2.setGravity(17);
        a2.setPadding(0, 0, 0, 10);
        a.addView(a2);
        String a3 = C0839h.m1568a("ANDROID_go_back_to_merchant");
        PayPal instance = PayPal.getInstance();
        PayPalAdvancedPayment payment = instance.getPayment();
        if (instance.getPayType() == 3) {
            merchantName = instance.getPreapproval().getMerchantName();
        } else if (instance.getPayType() == 0) {
            if (instance.isPersonalPayment()) {
                merchantName = ((PayPalReceiverDetails) payment.getReceivers().get(0)).getRecipient();
            } else {
                merchantName = ((PayPalReceiverDetails) payment.getReceivers().get(0)).getMerchantName();
                if (merchantName == null || merchantName.length() <= 0) {
                    merchantName = ((PayPalReceiverDetails) payment.getReceivers().get(0)).getRecipient();
                }
            }
        } else if (instance.getPayType() == 2) {
            merchantName = payment.getPrimaryReceiver().getMerchantName();
            if (merchantName == null || merchantName.length() <= 0) {
                merchantName = payment.getPrimaryReceiver().getRecipient();
            }
        } else {
            merchantName = C0839h.m1568a("ANDROID_the_merchant");
        }
        merchantName = a3.replace("%m", merchantName);
        View a4 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a4.setText(merchantName);
        a4.setGravity(17);
        a.addView(a4);
        a2 = C0845o.m1618a(C0844a.HELVETICA_14_NORMAL, context);
        a2.setText(C0839h.m1568a("ANDROID_lose_all_information"));
        a2.setGravity(17);
        a.addView(a2);
        a2 = C0835d.m1555a(context, -1, -2);
        a2.setOrientation(0);
        a2.setGravity(16);
        a2.setPadding(0, 10, 0, 10);
        a4 = C0835d.m1556a(context, -1, -2, 0.5f);
        a4.setOrientation(1);
        a4.setGravity(1);
        a4.setPadding(0, 0, 3, 0);
        this.f1501a = new Button(context);
        this.f1501a.setText(C0839h.m1568a("ANDROID_ok"));
        this.f1501a.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f1501a.setLayoutParams(new LayoutParams(-1, C0835d.m1557b()));
        this.f1501a.setGravity(17);
        this.f1501a.setBackgroundDrawable(C0836e.m1559a());
        this.f1501a.setOnClickListener(this);
        a4.addView(this.f1501a);
        a2.addView(a4);
        a4 = C0835d.m1556a(context, -1, -2, 0.5f);
        a4.setOrientation(1);
        a4.setGravity(1);
        a4.setPadding(3, 0, 0, 0);
        this.f1502b = new Button(context);
        this.f1502b.setText(C0839h.m1568a("ANDROID_cancel"));
        this.f1502b.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        this.f1502b.setLayoutParams(new LayoutParams(-1, C0835d.m1557b()));
        this.f1502b.setGravity(17);
        this.f1502b.setBackgroundDrawable(C0836e.m1561b());
        this.f1502b.setOnClickListener(this);
        a4.addView(this.f1502b);
        a2.addView(a4);
        a.addView(a2);
        setContentView(a);
    }

    public final void onClick(View view) {
        if (view == this.f1501a) {
            dismiss();
            PayPalActivity.getInstance().paymentCanceled();
        } else if (view == this.f1502b) {
            dismiss();
        }
    }
}
