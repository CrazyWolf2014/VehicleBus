package com.paypal.android.p024b;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.p024b.C1111d.C0849a;

/* renamed from: com.paypal.android.b.c */
public class C1110c extends C0853g implements OnClickListener, C0849a {
    protected LinearLayout f2232a;
    protected LinearLayout f2233b;
    protected C1111d f2234c;

    public C1110c(Context context) {
        super(context);
        setOnClickListener(this);
        setOnFocusChangeListener(this);
        this.f2232a = new LinearLayout(context);
        this.f2232a.setLayoutParams(new LayoutParams(-1, -2));
        this.f2232a.setOrientation(0);
        this.f2232a.setGravity(5);
        this.f2232a.setPadding(5, 5, 5, 5);
        this.f2233b = new LinearLayout(context);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.gravity = 1;
        this.f2233b.setLayoutParams(layoutParams);
        this.f2233b.setPadding(5, 0, 5, 5);
        addView(this.f2232a);
        addView(this.f2233b);
        this.f2233b.setVisibility(8);
        this.f2234c = new C1111d(context);
        this.f2234c.setLayoutParams(new LayoutParams(-2, -2));
        this.f2234c.m2438a(this);
        this.f2234c.setGravity(16);
        this.f2234c.setVisibility(8);
        this.f2234c.setClickable(false);
        this.f2234c.setFocusable(false);
    }

    public final void m2433a() {
        int i = 1;
        if (this.d == 1) {
            i = 0;
        }
        m2434a(i);
    }

    public void m2434a(int i) {
        super.m1631a(i);
        if (i == 1) {
            this.f2233b.setVisibility(0);
            this.f2234c.m1621a(1);
            return;
        }
        this.f2233b.setVisibility(8);
        this.f2234c.m1621a(0);
    }

    public final void m2435a(Drawable drawable) {
        if (this.f2234c != null) {
            float pow = (float) Math.pow((double) PayPal.getInstance().getDensity(), 2.0d);
            int minimumWidth = (int) (((float) drawable.getMinimumWidth()) * pow);
            int minimumHeight = (int) (pow * ((float) drawable.getMinimumHeight()));
            this.f2234c.m1622a(1, drawable);
            this.f2234c.setLayoutParams(new LayoutParams(minimumWidth, minimumHeight));
        }
    }

    public final void m2436a(boolean z) {
        if (z) {
            setClickable(true);
            setFocusable(true);
            this.f2234c.setVisibility(0);
            return;
        }
        setClickable(false);
        setFocusable(false);
        this.f2234c.setVisibility(8);
        this.f2233b.setVisibility(8);
    }

    public final void m2437b(Drawable drawable) {
        if (this.f2234c != null) {
            float pow = (float) Math.pow((double) PayPal.getInstance().getDensity(), 2.0d);
            int minimumWidth = (int) (((float) drawable.getMinimumWidth()) * pow);
            int minimumHeight = (int) (pow * ((float) drawable.getMinimumHeight()));
            this.f2234c.m1622a(0, drawable);
            this.f2234c.setLayoutParams(new LayoutParams(minimumWidth, minimumHeight));
        }
    }

    public void onClick(View view) {
        this.f2234c.onClick(this.f2234c);
    }
}
