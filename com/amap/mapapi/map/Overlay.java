package com.amap.mapapi.map;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.amap.mapapi.core.GeoPoint;

public class Overlay {
    protected static final float SHADOW_X_SKEW = -0.9f;
    protected static final float SHADOW_Y_SCALE = 0.5f;

    public interface Snappable {
        boolean onSnapToItem(int i, int i2, Point point, MapView mapView);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent, MapView mapView) {
        return false;
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent, MapView mapView) {
        return false;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        return false;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        return false;
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
    }

    public boolean draw(Canvas canvas, MapView mapView, boolean z, long j) {
        draw(canvas, mapView, z);
        return false;
    }

    protected static void drawAt(Canvas canvas, Drawable drawable, int i, int i2, boolean z) {
        if (z) {
            Drawable a = new ao().m1945a(drawable);
            ao.m1944a(a, drawable);
            drawable = a;
        }
        m653a(canvas, drawable, i, i2);
    }

    static void m653a(Canvas canvas, Drawable drawable, int i, int i2) {
        Rect bounds = drawable.getBounds();
        drawable.setBounds(bounds.left + i, bounds.top + i2, bounds.right + i, bounds.bottom + i2);
        drawable.draw(canvas);
        drawable.setBounds(bounds.left - i, bounds.top - i2, bounds.right - i, bounds.bottom - i2);
    }
}
