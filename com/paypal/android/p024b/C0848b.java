package com.paypal.android.p024b;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.StateSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

/* renamed from: com.paypal.android.b.b */
public class C0848b extends Button implements OnFocusChangeListener {
    private int f1584a;
    private Drawable[] f1585b;

    public C0848b(Context context) {
        super(context);
        this.f1584a = 0;
        setOnFocusChangeListener(this);
        setLayoutParams(new LayoutParams(-2, -2));
    }

    public final int m1620a() {
        return this.f1584a;
    }

    public final void m1621a(int i) {
        if (this.f1584a != 2) {
            if (i < 0 || i >= 3) {
                throw new IllegalArgumentException("State " + i + " is outside the acceptable range 0-" + 2);
            }
            this.f1584a = i;
            if (this.f1585b != null) {
                Drawable drawable = this.f1585b[this.f1584a];
                if (drawable != null) {
                    setBackgroundColor(0);
                    setBackgroundDrawable(drawable);
                }
            }
        }
    }

    public final void m1622a(int i, Drawable drawable) {
        if (i < 0 || i >= 3) {
            throw new IllegalArgumentException("State " + i + " is outside the acceptable range 0-" + 2);
        }
        if (this.f1585b == null) {
            this.f1585b = new Drawable[3];
        }
        this.f1585b[i] = drawable;
        if (i == this.f1584a && drawable != null) {
            setBackgroundDrawable(drawable);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        if (!StateSet.stateSetMatches(new int[]{16842919}, drawableState)) {
            if (!StateSet.stateSetMatches(new int[]{16842908}, drawableState)) {
                m1621a(0);
                return;
            }
        }
        m1621a(1);
    }

    public void onFocusChange(View view, boolean z) {
    }
}
