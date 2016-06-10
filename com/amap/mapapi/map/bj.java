package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Point;

/* compiled from: WaterMarkOverlay */
class bj extends ImageOverlay {
    public bj(ag agVar, Bitmap bitmap) {
        super(agVar, bitmap);
    }

    protected Point m2641a() {
        return new Point(0, (this.a.f594b.m752d() - this.b.getHeight()) - 10);
    }

    public void m2642b() {
        if (this.b != null && !this.b.isRecycled()) {
            this.b.recycle();
            this.b = null;
        }
    }
}
