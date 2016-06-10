package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

/* renamed from: com.amap.mapapi.map.g */
class BitmapDrawer {
    protected Bitmap f669a;
    protected Canvas f670b;
    protected Config f671c;

    public BitmapDrawer(Config config) {
        this.f669a = null;
        this.f670b = null;
        this.f671c = config;
    }

    public void m824a(Bitmap bitmap) {
        this.f669a = bitmap;
        this.f670b = new Canvas(this.f669a);
    }

    public void m823a(int i, int i2) {
        m822a();
        this.f669a = Bitmap.createBitmap(i, i2, this.f671c);
        this.f670b = new Canvas(this.f669a);
    }

    public void m822a() {
        if (this.f669a != null) {
            this.f669a.recycle();
        }
        this.f669a = null;
        this.f670b = null;
    }

    public void m825a(BitmapMaker bitmapMaker) {
        this.f670b.save(1);
        bitmapMaker.m827a(this.f670b);
        this.f670b.restore();
    }

    public Bitmap m826b() {
        return this.f669a;
    }
}
