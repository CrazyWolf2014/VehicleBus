package com.paypal.android.p024b;

import android.content.Context;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/* renamed from: com.paypal.android.b.g */
public class C0853g extends LinearLayout implements OnFocusChangeListener {
    private LayoutParams[] f1589a;
    private C0852a f1590b;
    private int f1591c;
    protected int f1592d;

    /* renamed from: com.paypal.android.b.g.a */
    public interface C0852a {
        void m1630a(C0853g c0853g, int i);
    }

    public C0853g(Context context) {
        super(context);
        setOnFocusChangeListener(this);
        setFocusable(true);
        this.f1592d = 0;
        this.f1591c = 2;
        setOrientation(1);
    }

    public void m1631a(int i) {
        if (i != this.f1592d) {
            if (i < 0 || i >= this.f1591c) {
                throw new IllegalArgumentException("State " + i + " is outside the acceptable range 0-" + (this.f1591c - 1));
            }
            this.f1592d = i;
            setLayoutParams(this.f1589a[this.f1592d]);
            if (this.f1590b != null) {
                this.f1590b.m1630a(this, i);
            }
        }
    }

    public final void m1632a(LayoutParams layoutParams, int i) {
        if (this.f1589a == null) {
            this.f1589a = new LayoutParams[this.f1591c];
        }
        this.f1589a[i] = layoutParams;
        if (i == this.f1592d) {
            setLayoutParams(layoutParams);
        }
    }

    public final void m1633a(C0852a c0852a) {
        this.f1590b = c0852a;
    }

    public void m1634b(boolean z) {
    }

    public void onFocusChange(View view, boolean z) {
        m1634b(z);
    }
}
