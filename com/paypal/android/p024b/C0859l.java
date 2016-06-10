package com.paypal.android.p024b;

import android.view.animation.Animation;

/* renamed from: com.paypal.android.b.l */
public final class C0859l extends Thread {
    private C0858j f1605a;
    private Animation f1606b;

    public C0859l(C0858j c0858j, Animation animation) {
        this.f1605a = c0858j;
        this.f1606b = animation;
    }

    public final void run() {
        this.f1605a.startAnimation(this.f1606b);
    }
}
