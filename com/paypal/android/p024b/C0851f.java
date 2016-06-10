package com.paypal.android.p024b;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.paypal.android.p022a.C0845o;
import com.paypal.android.p022a.C0845o.C0844a;

/* renamed from: com.paypal.android.b.f */
public final class C0851f extends LinearLayout {
    private TextView f1587a;
    private TextView f1588b;

    public C0851f(Context context, C0844a c0844a, C0844a c0844a2) {
        super(context);
        setLayoutParams(new LayoutParams(-1, -2, 0.5f));
        setBackgroundColor(0);
        this.f1587a = C0845o.m1618a(c0844a, context);
        this.f1587a.setLayoutParams(new LayoutParams(-1, -2, 0.4f));
        this.f1587a.setPadding(2, 2, 2, 2);
        this.f1587a.setBackgroundColor(0);
        this.f1587a.setVisibility(8);
        this.f1588b = C0845o.m1618a(c0844a2, context);
        this.f1588b.setGravity(5);
        this.f1588b.setLayoutParams(new LayoutParams(-1, -2, 0.6f));
        this.f1588b.setPadding(2, 2, 2, 2);
        this.f1588b.setBackgroundColor(0);
        this.f1588b.setVisibility(8);
        addView(this.f1587a);
        addView(this.f1588b);
    }

    public C0851f(Context context, C0844a c0844a, C0844a c0844a2, float f, float f2) {
        super(context);
        setLayoutParams(new LayoutParams(-1, -2, 0.5f));
        setBackgroundColor(0);
        this.f1587a = C0845o.m1618a(c0844a, context);
        this.f1587a.setLayoutParams(new LayoutParams(-1, -2, 0.5f));
        this.f1587a.setPadding(2, 2, 2, 2);
        this.f1587a.setBackgroundColor(0);
        this.f1587a.setVisibility(8);
        this.f1588b = C0845o.m1618a(c0844a2, context);
        this.f1588b.setGravity(5);
        this.f1588b.setLayoutParams(new LayoutParams(-1, -2, 0.5f));
        this.f1588b.setPadding(2, 2, 2, 2);
        this.f1588b.setBackgroundColor(0);
        this.f1588b.setVisibility(8);
        addView(this.f1587a);
        addView(this.f1588b);
    }

    public final void m1626a(int i) {
        this.f1587a.setTextColor(i);
    }

    public final void m1627a(String str) {
        this.f1587a.setVisibility(8);
        if (str != null && str.length() > 0) {
            this.f1587a.setText(str);
            this.f1587a.setVisibility(0);
        }
    }

    public final void m1628b(int i) {
        this.f1588b.setTextColor(i);
    }

    public final void m1629b(String str) {
        this.f1588b.setVisibility(8);
        if (str != null && str.length() > 0) {
            this.f1588b.setText(str);
            this.f1588b.setVisibility(0);
        }
    }
}
