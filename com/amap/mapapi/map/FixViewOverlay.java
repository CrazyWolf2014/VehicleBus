package com.amap.mapapi.map;

import android.graphics.Canvas;
import android.graphics.Rect;

/* renamed from: com.amap.mapapi.map.r */
class FixViewOverlay extends FixPosOverlay {
    private Rect f2370a;

    public FixViewOverlay(Rect rect) {
        this.f2370a = rect;
    }

    public boolean draw(Canvas canvas, MapView mapView, boolean z, long j) {
        return false;
    }

    public void m2541b() {
    }
}
