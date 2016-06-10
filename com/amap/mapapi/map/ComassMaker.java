package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/* renamed from: com.amap.mapapi.map.l */
class ComassMaker implements BitmapMaker {
    BitmapDrawer f1905a;
    private Drawable f1906b;
    private Drawable f1907c;
    private int f1908d;
    private int f1909e;
    private float f1910f;

    public ComassMaker(Bitmap bitmap, Bitmap bitmap2) {
        this.f1910f = 0.0f;
        this.f1905a = new BitmapDrawer(Config.ARGB_4444);
        this.f1908d = bitmap.getWidth();
        this.f1909e = bitmap.getHeight();
        this.f1906b = m1991a(bitmap);
        this.f1907c = m1991a(bitmap2);
    }

    private Drawable m1991a(Bitmap bitmap) {
        Drawable bitmapDrawable = new BitmapDrawable(bitmap);
        bitmapDrawable.setBounds(0, 0, this.f1908d, this.f1909e);
        return bitmapDrawable;
    }

    public Bitmap m1992a(float f) {
        this.f1910f = f;
        this.f1905a.m823a(this.f1908d, this.f1909e);
        this.f1905a.m825a((BitmapMaker) this);
        return this.f1905a.m826b();
    }

    public void m1993a(Canvas canvas) {
        this.f1906b.draw(canvas);
        canvas.rotate(-this.f1910f, (float) (this.f1908d / 2), (float) (this.f1909e / 2));
        this.f1907c.draw(canvas);
    }
}
