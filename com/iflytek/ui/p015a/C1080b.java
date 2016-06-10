package com.iflytek.ui.p015a;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* renamed from: com.iflytek.ui.a.b */
public class C1080b implements C0301i {
    private Drawable[] f2025a;
    private Drawable f2026b;
    private Rect f2027c;
    private int f2028d;

    public C1080b(Drawable[] drawableArr) {
        this.f2028d = 0;
        if (drawableArr == null || drawableArr.length < 1) {
            throw new IllegalArgumentException("Arguments must be not null");
        }
        this.f2025a = drawableArr;
        this.f2027c = new Rect((-this.f2025a[0].getIntrinsicWidth()) / 2, (-this.f2025a[0].getIntrinsicHeight()) / 2, this.f2025a[0].getIntrinsicWidth() / 2, this.f2025a[0].getIntrinsicHeight() / 2);
        m2161a(0);
    }

    public int m2160a() {
        return 100;
    }

    public void m2161a(int i) {
        if (i < 0) {
            i = 0;
        } else if (i < this.f2028d) {
            i--;
        } else if (i > this.f2025a.length - 1) {
            i = this.f2025a.length - 1;
        }
        this.f2028d = i;
        if (this.f2028d < 0) {
            this.f2028d = 0;
        }
        this.f2026b = this.f2025a[this.f2028d];
        this.f2026b.setBounds(this.f2027c);
    }

    public void m2162a(Canvas canvas, Rect rect) {
        canvas.save();
        canvas.translate((float) (rect.width() / 2), (float) (rect.height() / 2));
        this.f2026b.draw(canvas);
        canvas.restore();
    }

    public void m2163b() {
    }

    public void m2164c() {
    }

    public void m2165d() {
    }

    protected void finalize() throws Throwable {
        this.f2025a = null;
        this.f2026b = null;
        this.f2027c = null;
        super.finalize();
    }
}
