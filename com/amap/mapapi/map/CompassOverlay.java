package com.amap.mapapi.map;

import android.graphics.Point;
import com.amap.mapapi.core.ConfigableConst;

/* renamed from: com.amap.mapapi.map.m */
class CompassOverlay extends ImageOverlay {
    private ComassMaker f2408c;
    private float f2409d;

    public CompassOverlay(ag agVar) {
        super(agVar, null);
        this.f2408c = new ComassMaker(ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ecompassback.ordinal()), ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ecommpasspoint.ordinal()));
        this.f2409d = 0.0f;
        m2644d();
    }

    protected Point m2645a() {
        return new Point(15, 45);
    }

    public boolean m2646a(float f) {
        boolean b = m2643b(f);
        if (b) {
            this.f2409d = f;
            m2644d();
        }
        return b;
    }

    private void m2644d() {
        this.b = this.f2408c.m1992a(this.f2409d);
    }

    private boolean m2643b(float f) {
        return Math.abs(f - this.f2409d) > 3.0f;
    }

    public void m2647b() {
        if (this.b != null && !this.b.isRecycled()) {
            this.b.recycle();
            this.b = null;
        }
    }
}
