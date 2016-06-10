package com.iflytek.ui.p015a;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* renamed from: com.iflytek.ui.a.e */
public class C1083e implements C0301i {
    private Drawable[] f2042a;
    private Drawable f2043b;
    private Rect f2044c;
    private int f2045d;
    private int f2046e;

    public C1083e(Drawable[] drawableArr, int i) {
        this.f2045d = 0;
        this.f2046e = 0;
        if (drawableArr == null) {
            throw new IllegalArgumentException("Arguments must be not null");
        }
        this.f2042a = drawableArr;
        this.f2046e = i;
        Drawable drawable = this.f2042a[0];
        this.f2045d = 0;
        this.f2043b = drawableArr[0];
        this.f2044c = new Rect((-drawable.getIntrinsicWidth()) / 2, (-drawable.getIntrinsicHeight()) / 2, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
    }

    public int m2180a() {
        return this.f2046e;
    }

    public void m2181a(Canvas canvas, Rect rect) {
        canvas.save();
        canvas.translate((float) (rect.width() / 2), (float) (rect.height() / 2));
        this.f2043b.draw(canvas);
        canvas.restore();
    }

    public void m2182b() {
        this.f2045d = (this.f2045d + 1) % this.f2042a.length;
        this.f2043b = this.f2042a[this.f2045d];
        this.f2043b.setBounds(this.f2044c);
    }

    public void m2183c() {
    }

    public void m2184d() {
        this.f2045d = 0;
    }

    protected void finalize() throws Throwable {
        this.f2042a = null;
        this.f2043b = null;
        this.f2044c = null;
        super.finalize();
    }
}
