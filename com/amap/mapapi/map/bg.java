package com.amap.mapapi.map;

import android.content.Context;
import com.amap.mapapi.core.GeoPoint;
import com.mapabc.minimap.map.vmap.VMapProjection;

/* compiled from: VectorMeshServer */
class bg extends ad implements bi {
    public bg(ag agVar, Context context) {
        super(agVar, context);
        this.e.f594b.m745a((bi) this);
    }

    public void m1979a(boolean z, boolean z2) {
        GeoPoint f = this.e.f594b.m754f();
        bf LatLongToPixels = VMapProjection.LatLongToPixels(((double) f.getLatitudeE6()) / 1000000.0d, ((double) f.getLongitudeE6()) / 1000000.0d, 20);
        this.e.f594b.m755g().centerX = LatLongToPixels.f665a;
        this.e.f594b.m755g().centerY = LatLongToPixels.f666b;
        this.e.f594b.m755g().mapLevel = this.e.f594b.m753e();
        this.e.f594b.m755g().invalidate();
    }
}
