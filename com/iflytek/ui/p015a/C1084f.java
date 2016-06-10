package com.iflytek.ui.p015a;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/* renamed from: com.iflytek.ui.a.f */
public class C1084f implements C0301i {
    private Drawable f2047a;
    private Rect f2048b;
    private int f2049c;

    public C1084f(Drawable drawable) {
        this.f2049c = 0;
        if (drawable == null) {
            throw new IllegalArgumentException("Arguments must be not null");
        }
        this.f2047a = drawable;
        this.f2048b = new Rect((-this.f2047a.getIntrinsicWidth()) / 2, (-this.f2047a.getIntrinsicHeight()) / 2, this.f2047a.getIntrinsicWidth() / 2, this.f2047a.getIntrinsicHeight() / 2);
    }

    public int m2185a() {
        return 100;
    }

    public void m2186a(Canvas canvas, Rect rect) {
        canvas.save();
        canvas.translate((float) (rect.width() / 2), (float) (rect.height() / 2));
        canvas.rotate((float) ((this.f2049c * 30) % 360));
        this.f2047a.draw(canvas);
        this.f2047a.setBounds(this.f2048b);
        canvas.restore();
    }

    public void m2187b() {
        this.f2049c++;
    }

    public void m2188c() {
    }

    public void m2189d() {
        this.f2049c = 0;
    }

    protected void finalize() throws Throwable {
        this.f2047a = null;
        this.f2048b = null;
        super.finalize();
    }
}
