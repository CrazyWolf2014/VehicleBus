package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import com.amap.mapapi.core.ConfigableConst;

/* renamed from: com.amap.mapapi.map.t */
abstract class ImageOverlay extends FixPosOverlay {
    protected ag f2371a;
    protected Bitmap f2372b;

    protected abstract Point m2542a();

    public ImageOverlay(ag agVar, Bitmap bitmap) {
        this.f2371a = agVar;
        this.f2372b = bitmap;
    }

    public Rect m2543c() {
        Point a = m2542a();
        if (this.f2372b == null) {
            this.f2372b = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ewatermark.ordinal());
        }
        return new Rect(a.x, a.y, a.x + this.f2372b.getWidth(), a.y + this.f2372b.getHeight());
    }

    public boolean draw(Canvas canvas, MapView mapView, boolean z, long j) {
        if (this.f2372b != null && this.f2372b.isRecycled()) {
            this.f2372b = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ewatermark.ordinal());
        }
        if (this.f2372b == null) {
            this.f2372b = ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ewatermark.ordinal());
        }
        canvas.drawBitmap(this.f2372b, (float) m2543c().left, (float) m2543c().top, null);
        return true;
    }
}
