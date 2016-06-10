package com.paypal.android.p024b;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/* renamed from: com.paypal.android.b.k */
public class C1280k extends C1110c {
    private LinearLayout f2390e;

    public C1280k(Context context) {
        super(context);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
        layoutParams.gravity = 1;
        this.b.setLayoutParams(layoutParams);
        this.b.setOrientation(1);
        this.b.setGravity(17);
        this.b.setPadding(10, 0, 10, 0);
        this.f2390e = new LinearLayout(context);
        this.f2390e.setLayoutParams(layoutParams);
        this.f2390e.setOrientation(1);
        this.f2390e.setGravity(17);
        this.f2390e.setPadding(5, 0, 5, 5);
        this.b.addView(this.f2390e);
        this.c.setVisibility(0);
    }

    public final void m2585a(View view) {
        this.f2390e.addView(view);
    }

    public void m2586c() {
    }

    public final void m2587c(Drawable drawable) {
        this.f2390e.setBackgroundDrawable(drawable);
    }

    public final void m2588d() {
        this.f2390e.removeAllViews();
    }

    public void setBackgroundDrawable(Drawable drawable) {
        this.a.setBackgroundDrawable(drawable);
    }
}
