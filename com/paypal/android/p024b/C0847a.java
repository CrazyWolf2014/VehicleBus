package com.paypal.android.p024b;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.CheckBox;
import android.widget.LinearLayout.LayoutParams;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.p022a.C0836e;

/* renamed from: com.paypal.android.b.a */
public final class C0847a extends CheckBox {
    private StateListDrawable f1583a;

    public C0847a(Context context) {
        super(context);
        Drawable a = C0836e.m1558a(92148, 842);
        Drawable a2 = C0836e.m1558a(162620, 1154);
        Drawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setVisible(false, false);
        this.f1583a = new StateListDrawable();
        this.f1583a.addState(new int[]{-16842912}, a);
        this.f1583a.addState(new int[]{16842912}, a2);
        setLayoutParams(new LayoutParams((int) (((double) a.getIntrinsicWidth()) * Math.pow((double) PayPal.getInstance().getDensity(), 2.0d)), (int) (((double) a.getIntrinsicHeight()) * Math.pow((double) PayPal.getInstance().getDensity(), 2.0d))));
        setBackgroundDrawable(this.f1583a);
        setButtonDrawable(gradientDrawable);
    }
}
