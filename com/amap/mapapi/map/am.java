package com.amap.mapapi.map;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView.C0092b;
import com.amap.mapapi.map.MapView.C0094e;
import com.amap.mapapi.map.MapView.LayoutParams;

/* compiled from: RouteInfo */
public class am extends InfoWindow implements C0092b {
    private RouteOverlay f1868i;
    private int f1869j;

    public /* bridge */ /* synthetic */ void m1939a() {
        super.m854a();
    }

    public /* bridge */ /* synthetic */ void m1942b() {
        super.m855b();
    }

    public /* bridge */ /* synthetic */ boolean onDown(MotionEvent motionEvent) {
        return super.onDown(motionEvent);
    }

    public /* bridge */ /* synthetic */ boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return super.onFling(motionEvent, motionEvent2, f, f2);
    }

    public /* bridge */ /* synthetic */ void onLongPress(MotionEvent motionEvent) {
        super.onLongPress(motionEvent);
    }

    public /* bridge */ /* synthetic */ boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return super.onScroll(motionEvent, motionEvent2, f, f2);
    }

    public /* bridge */ /* synthetic */ void onShowPress(MotionEvent motionEvent) {
        super.onShowPress(motionEvent);
    }

    public /* bridge */ /* synthetic */ boolean onSingleTapUp(MotionEvent motionEvent) {
        return super.onSingleTapUp(motionEvent);
    }

    public am(MapView mapView, View view, GeoPoint geoPoint, RouteOverlay routeOverlay, int i) {
        this(mapView, view, geoPoint, null, null, routeOverlay, i);
    }

    public am(MapView mapView, View view, GeoPoint geoPoint, Drawable drawable, LayoutParams layoutParams, RouteOverlay routeOverlay, int i) {
        super(mapView, view, geoPoint, drawable, layoutParams);
        this.f1868i = routeOverlay;
        this.f1869j = i;
    }

    public void m1941a(boolean z) {
        boolean z2;
        boolean z3 = true;
        super.m855b();
        this.d.mRouteCtrl.m604a(z, (C0092b) this);
        this.d.mRouteCtrl.m605b(this.f1869j < this.f1868i.getRoute().getStepCount());
        C0094e c0094e = this.d.mRouteCtrl;
        if (this.f1869j != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        c0094e.m603a(z2);
        c0094e = this.d.mRouteCtrl;
        if (this.d.getMaxZoomLevel() != this.d.getZoomLevel()) {
            z2 = true;
        } else {
            z2 = false;
        }
        c0094e.m607c(z2);
        C0094e c0094e2 = this.d.mRouteCtrl;
        if (this.d.getMinZoomLevel() == this.d.getZoomLevel()) {
            z3 = false;
        }
        c0094e2.m608d(z3);
    }

    public void m1943c() {
        super.m856c();
        this.d.mRouteCtrl.m604a(false, (C0092b) this);
    }

    public void m1940a(int i) {
        this.f1868i.f1849b.onRouteEvent(this.d, this.f1868i, this.f1869j, i);
    }
}
