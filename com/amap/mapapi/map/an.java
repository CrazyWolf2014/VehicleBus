package com.amap.mapapi.map;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import com.amap.mapapi.core.GeoPoint;

/* compiled from: RouteOverlayDraw */
class an {
    protected RouteOverlay f624c;

    public an(RouteOverlay routeOverlay) {
        this.f624c = routeOverlay;
    }

    public void m781a(Canvas canvas, MapView mapView, boolean z) {
    }

    protected Point m779a(MapView mapView, GeoPoint geoPoint) {
        return mapView.getProjection().toPixels(geoPoint, null);
    }

    protected GeoPoint m780a(MapView mapView, Point point) {
        return mapView.getProjection().fromPixels(point.x, point.y);
    }

    public void m782a(MapView mapView) {
    }

    public void m784b(MapView mapView) {
    }

    public boolean m783a(MotionEvent motionEvent, MapView mapView) {
        return false;
    }
}
