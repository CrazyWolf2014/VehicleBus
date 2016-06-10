package com.amap.mapapi.map;

import com.amap.mapapi.core.GeoPoint;

/* compiled from: TransAnim */
class bc extends AnimBase {
    private GeoPoint f1874e;
    private GeoPoint f1875f;
    private int f1876g;
    private int f1877h;
    private int f1878i;
    private int f1879j;
    private int f1880k;
    private int f1881l;
    private int f1882m;
    private int f1883n;
    private bd f1884o;

    public bc(int i, int i2, GeoPoint geoPoint, GeoPoint geoPoint2, int i3, bd bdVar) {
        super(i, i2);
        this.f1874e = geoPoint;
        this.f1875f = geoPoint2;
        this.f1876g = (int) this.f1874e.m466c();
        this.f1877h = (int) this.f1874e.m467d();
        this.f1884o = bdVar;
        this.f1880k = (int) Math.abs(geoPoint2.m466c() - this.f1874e.m466c());
        this.f1881l = (int) Math.abs(geoPoint2.m467d() - this.f1874e.m467d());
        this.f1882m = 7;
        m1976a(i3);
    }

    private void m1976a(int i) {
        this.f1878i = this.f1880k / this.f1882m;
        this.f1879j = this.f1881l / this.f1882m;
    }

    protected void m1978b() {
        this.f1884o.m816b();
    }

    protected void m1977a() {
        int c = (int) this.f1875f.m466c();
        int d = (int) this.f1875f.m467d();
        if (m662f()) {
            this.f1883n++;
            this.f1878i += this.f1883n * (this.f1883n + 1);
            this.f1879j += this.f1883n * (this.f1883n + 1);
            this.f1876g = m1975a(this.f1876g, c, this.f1878i);
            this.f1877h = m1975a(this.f1877h, d, this.f1879j);
            this.f1884o.m815a(new GeoPoint((double) this.f1877h, (double) this.f1876g, false));
            return;
        }
        this.f1876g = c;
        this.f1877h = d;
        this.f1884o.m815a(new GeoPoint((double) this.f1877h, (double) this.f1876g, false));
    }

    private int m1975a(int i, int i2, int i3) {
        int i4;
        if (i2 > i) {
            i4 = i + i3;
            if (i4 >= i2) {
                this.f1883n = 0;
                return i2;
            }
        }
        i4 = i - i3;
        if (i4 <= i2) {
            this.f1883n = 0;
            return i2;
        }
        return i4;
    }
}
