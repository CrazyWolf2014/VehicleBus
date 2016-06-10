package com.amap.mapapi.route;

import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.route.Route.FromAndTo;

/* renamed from: com.amap.mapapi.route.f */
public class RouteParam {
    public FromAndTo f745a;
    public int f746b;
    public String f747c;

    public void m895a(String str) {
        this.f747c = str;
    }

    public RouteParam(FromAndTo fromAndTo, int i) {
        this.f745a = fromAndTo;
        this.f746b = i;
    }

    public double m894a() {
        return CoreUtil.m481a(this.f745a.mFrom.m462a());
    }

    public double m896b() {
        return CoreUtil.m481a(this.f745a.mTo.m462a());
    }

    public double m897c() {
        return CoreUtil.m481a(this.f745a.mFrom.m464b());
    }

    public double m898d() {
        return CoreUtil.m481a(this.f745a.mTo.m464b());
    }
}
