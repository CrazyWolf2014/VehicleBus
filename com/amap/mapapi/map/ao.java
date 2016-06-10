package com.amap.mapapi.map;

import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/* compiled from: ShadowMaker */
class ao implements BitmapMaker {
    private BitmapDrawer f1870a;
    private Drawable f1871b;

    ao() {
        this.f1870a = new BitmapDrawer(Config.ARGB_4444);
        this.f1871b = null;
    }

    public Drawable m1945a(Drawable drawable) {
        this.f1871b = drawable;
        this.f1870a.m823a(this.f1871b.getIntrinsicWidth(), this.f1871b.getIntrinsicHeight());
        this.f1870a.m825a((BitmapMaker) this);
        this.f1871b = null;
        return new BitmapDrawable(this.f1870a.m826b());
    }

    public void m1946a(Canvas canvas) {
        this.f1871b.setColorFilter(2130706432, Mode.SRC_IN);
        canvas.skew(-0.9f, 0.0f);
        canvas.scale(1.0f, 0.5f);
        this.f1871b.draw(canvas);
        this.f1871b.clearColorFilter();
    }

    public static void m1944a(Drawable drawable, Drawable drawable2) {
        Rect bounds = drawable2.getBounds();
        int height = (int) (((float) bounds.height()) * 0.5f);
        int width = (int) (((double) (((float) bounds.width()) * 0.9f)) * 0.5d);
        drawable.setBounds(bounds.left + width, bounds.top + height, width + bounds.right, bounds.bottom + height);
    }
}
