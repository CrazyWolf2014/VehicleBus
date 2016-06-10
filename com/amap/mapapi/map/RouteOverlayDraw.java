package com.amap.mapapi.map;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import com.amap.mapapi.core.GeoPoint;

/* renamed from: com.amap.mapapi.map.p */
class RouteOverlayDraw extends ai {
    private Drawable f2363d;
    private RouteMessageHandler f2364e;
    private boolean f2365f;
    private boolean f2366g;
    private int f2367h;
    private int f2368i;
    private boolean f2369j;

    public RouteOverlayDraw(RouteOverlay routeOverlay, int i, GeoPoint geoPoint, Drawable drawable, RouteMessageHandler routeMessageHandler, boolean z) {
        super(routeOverlay, i, geoPoint);
        this.f2369j = false;
        this.f2363d = drawable;
        this.f2364e = routeMessageHandler;
        this.f2365f = false;
        this.f2366g = false;
        Rect bounds = this.f2363d.getBounds();
        this.f2367h = (int) (((double) bounds.width()) * 1.5d);
        this.f2368i = (int) (((double) bounds.height()) * 1.5d);
        this.f2369j = z;
    }

    public void m2538a(Canvas canvas, MapView mapView, boolean z) {
        if (this.f2363d != null && !z) {
            Point a = m779a(mapView, this.b);
            Overlay.m653a(canvas, this.f2363d, a.x, a.y);
        }
    }

    private boolean m2536a(MapView mapView, int i, int i2) {
        Point a = m779a(mapView, this.b);
        Rect bounds = this.f2363d.getBounds();
        if (!this.f2369j) {
            return bounds.contains(i - a.x, i2 - a.y);
        }
        int width = bounds.width() / 2;
        int height = bounds.height();
        if (Math.abs(i - a.x) * 2 >= width || a.y - i2 <= 0 || a.y - i2 >= height) {
            return false;
        }
        return true;
    }

    private boolean m2537b(MapView mapView, Point point) {
        Point a = m779a(mapView, this.b);
        return ((a.y - point.y) * (a.y - point.y)) + ((a.x - point.x) * (a.x - point.x)) > this.f2367h * this.f2368i;
    }

    public boolean m2540a(MotionEvent motionEvent, MapView mapView) {
        Point point = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
        if (motionEvent.getAction() == 0 && m2536a(mapView, point.x, point.y)) {
            this.f2365f = true;
            return true;
        } else if (motionEvent.getAction() == 2) {
            if (this.f2366g) {
                if (!m2537b(mapView, point)) {
                    return true;
                }
                this.f2364e.onDrag(mapView, this.c, this.a, m780a(mapView, point));
                return true;
            } else if (!this.f2365f) {
                return false;
            } else {
                if (!m2537b(mapView, point)) {
                    return true;
                }
                this.f2366g = true;
                this.f2364e.onDragBegin(mapView, this.c, this.a, m780a(mapView, point));
                return true;
            }
        } else if (motionEvent.getAction() != 1 || !this.f2365f) {
            return false;
        } else {
            this.f2365f = false;
            if (this.f2366g) {
                this.f2366g = false;
                this.f2364e.onDragEnd(mapView, this.c, this.a, m780a(mapView, point));
                return true;
            }
            this.f2364e.onRouteEvent(mapView, this.c, this.a, 4);
            return true;
        }
    }

    public void m2539a(GeoPoint geoPoint) {
        this.b = geoPoint;
    }
}
