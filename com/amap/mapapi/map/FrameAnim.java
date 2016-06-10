package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/* renamed from: com.amap.mapapi.map.s */
class FrameAnim extends AnimBase {
    private Bitmap[] f1914e;
    private Rect f1915f;
    private int f1916g;
    private ag f1917h;

    public FrameAnim(int i, int i2, ag agVar, Bitmap[] bitmapArr) {
        super(i, i2);
        this.f1914e = bitmapArr;
        this.f1915f = new Rect(0, 0, this.f1914e[0].getWidth(), this.f1914e[0].getHeight());
        this.f1916g = 0;
        this.f1917h = agVar;
    }

    public int m2002h() {
        return this.f1914e[0].getWidth();
    }

    public int m2003i() {
        return this.f1914e[0].getHeight();
    }

    public void m2000a(Canvas canvas, int i, int i2) {
        int width = this.f1915f.width() / 2;
        int height = this.f1915f.height() / 2;
        this.f1915f.set(i - width, i2 - height, width + i, height + i2);
        this.f1916g++;
        if (this.f1916g >= this.f1914e.length) {
            this.f1916g = 0;
        }
        canvas.drawBitmap(this.f1914e[this.f1916g], (float) this.f1915f.left, (float) this.f1915f.top, null);
    }

    protected void m1999a() {
        this.f1916g++;
        if (this.f1916g >= this.f1914e.length) {
            this.f1916g = 0;
        }
        this.f1917h.f596d.m716b(this.f1915f.left, this.f1915f.top, this.f1915f.right, this.f1915f.bottom);
    }

    protected void m2001b() {
    }
}
