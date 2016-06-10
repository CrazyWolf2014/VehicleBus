package com.paypal.android.p024b;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.paypal.android.p022a.C0835d;
import com.paypal.android.p022a.C0836e;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.paypal.android.b.h */
public final class C0854h extends LinearLayout {
    public C0854h(String str, Context context) {
        super(context);
        setOrientation(1);
        setPadding(0, 0, 0, 0);
        setLayoutParams(new LayoutParams(-1, -2));
        setGravity(80);
        View a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(0);
        a.setPadding(0, 0, 0, 0);
        a.setGravity(80);
        View a2 = C0845o.m1618a(C0844a.HELVETICA_14_BOLD, context);
        a2.setLayoutParams(new LayoutParams(-1, -2, 1.0f));
        a2.setPadding(5, 5, 0, 0);
        a2.setGravity(83);
        a2.setBackgroundColor(0);
        a2.setTextColor(-3637184);
        a2.setText(str);
        a.addView(a2);
        a.addView(C0836e.m1560a(context, "paypal_logo_22.png"));
        a2 = C0835d.m1555a(context, -2, -2);
        a2.setPadding(5, 5, 5, 5);
        a2.addView(C0836e.m1560a(context, "lock-icon.png"));
        a.addView(a2);
        addView(a);
        a = C0835d.m1555a(context, -1, -2);
        a.setOrientation(0);
        a.setPadding(5, 5, 5, 10);
        a.setGravity(48);
        a2 = new ImageView(context);
        a2.setLayoutParams(new LayoutParams(-1, 2));
        Drawable gradientDrawable = new GradientDrawable(Orientation.LEFT_RIGHT, new int[]{-6042131, -14993820, -6042131});
        gradientDrawable.setCornerRadius(0.0f);
        gradientDrawable.setStroke(0, 0);
        gradientDrawable.setAlpha(Flags.FLAG8);
        a2.setBackgroundDrawable(gradientDrawable);
        a.addView(a2);
        addView(a);
    }
}
