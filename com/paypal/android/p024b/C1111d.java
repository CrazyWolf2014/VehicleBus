package com.paypal.android.p024b;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

/* renamed from: com.paypal.android.b.d */
public final class C1111d extends C0848b implements OnClickListener {
    private C0849a f2235a;

    /* renamed from: com.paypal.android.b.d.a */
    public interface C0849a {
        void m1623a();
    }

    public C1111d(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public final void m2438a(C0849a c0849a) {
        this.f2235a = c0849a;
    }

    protected final void drawableStateChanged() {
    }

    public final void onClick(View view) {
        if (view == this) {
            m1621a(m1620a() == 0 ? 1 : 0);
            if (this.f2235a != null) {
                this.f2235a.m1623a();
            }
        }
    }
}
