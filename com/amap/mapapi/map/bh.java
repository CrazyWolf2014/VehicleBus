package com.amap.mapapi.map;

import android.graphics.drawable.Drawable;
import android.view.View;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView.LayoutParams;

/* compiled from: RouteOverlayDraw */
class bh extends ai {
    private LayoutParams f2361d;
    private View f2362e;

    public bh(RouteOverlay routeOverlay, int i, GeoPoint geoPoint, View view, Drawable drawable, LayoutParams layoutParams) {
        super(routeOverlay, i, geoPoint);
        this.f2362e = view;
        this.f2362e.setBackgroundDrawable(drawable);
        this.f2361d = layoutParams;
    }

    public void m2534a(MapView mapView) {
        mapView.addView(this.f2362e, this.f2361d);
    }

    public void m2535b(MapView mapView) {
        mapView.removeView(this.f2362e);
    }
}
