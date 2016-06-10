package com.amap.mapapi.map;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;

/* compiled from: TrafSegment */
class ax {
    private int f659a;
    private int f660b;
    private int f661c;
    private int f662d;
    private List<PointF> f663e;

    public ax() {
        this.f663e = new ArrayList();
    }

    public List<PointF> m808a() {
        return this.f663e;
    }

    public void m810a(List<PointF> list) {
        this.f663e = list;
    }

    public void m809a(int i) {
        this.f659a = i;
    }

    public void m812b(int i) {
        this.f660b = i;
    }

    public int m811b() {
        return this.f661c;
    }

    public void m813c(int i) {
        this.f661c = i;
    }

    public void m814d(int i) {
        this.f662d = i;
    }
}
