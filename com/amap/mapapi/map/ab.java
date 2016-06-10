package com.amap.mapapi.map;

import android.graphics.Point;
import android.graphics.PointF;
import com.amap.mapapi.core.ConfigableConst;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.GeoPoint.EnumMapProjection;
import com.amap.mapapi.map.ag.Mediator;
import com.amap.mapapi.map.ar.Tile;
import com.mapabc.minimap.map.vmap.VMapProjection;
import java.util.ArrayList;
import org.xbill.DNS.KEYRecord;

/* compiled from: MapProjection */
class ab {
    double f534a;
    int f535b;
    double f536c;
    double f537d;
    public int f538e;
    public int f539f;
    public int f540g;
    public double[] f541h;
    public ac f542i;
    public GeoPoint f543j;
    public Point f544k;
    public MapProjection f545l;
    Mediator f546m;
    private double f547n;
    private double f548o;
    private double f549p;

    /* renamed from: com.amap.mapapi.map.ab.a */
    static class MapProjection {
        float f530a;
        float f531b;
        float f532c;
        float f533d;

        MapProjection() {
        }
    }

    public ab(Mediator mediator) {
        this.f547n = 116.39716d;
        this.f548o = 39.91669d;
        this.f534a = 156543.0339d;
        this.f535b = 0;
        this.f536c = -2.003750834E7d;
        this.f537d = 2.003750834E7d;
        this.f538e = 4;
        this.f539f = 18;
        this.f540g = 10;
        this.f541h = null;
        this.f542i = null;
        this.f543j = null;
        this.f544k = null;
        this.f545l = null;
        this.f546m = null;
        this.f549p = 0.01745329251994329d;
        this.f546m = mediator;
    }

    public void m684a(ac acVar) {
        this.f542i = acVar;
        m681a();
    }

    public void m681a() {
        GeoPoint geoPoint;
        if (this.f542i != null) {
            if (this.f542i.f550a > 0.0d) {
                this.f547n = this.f542i.f550a;
            }
            if (this.f542i.f551b > 0.0d) {
                this.f548o = this.f542i.f551b;
            }
            ConfigableConst.f338h = this.f542i.f552c;
            if (this.f542i.f553d > 0.0d) {
                this.f534a = this.f542i.f553d;
            }
            this.f535b = this.f542i.f554e;
            this.f536c = this.f542i.f555f;
            this.f537d = this.f542i.f556g;
            if (this.f542i.f557h >= 0) {
                this.f538e = this.f542i.f557h;
            }
            if (this.f542i.f558i >= 0) {
                this.f539f = this.f542i.f558i;
            }
            if (this.f542i.f559j >= 0) {
                this.f540g = this.f542i.f559j;
            }
        }
        this.f541h = new double[(this.f539f + 1)];
        for (int i = 0; i <= this.f539f; i++) {
            this.f541h[i] = this.f534a / ((double) m674a(2, i));
        }
        if (ConfigableConst.f338h == EnumMapProjection.projection_900913) {
            geoPoint = new GeoPoint(this.f548o, this.f547n, true);
            CoreUtil.f346a = true;
        } else {
            geoPoint = new GeoPoint(this.f548o, this.f547n, false);
            CoreUtil.f346a = false;
        }
        this.f543j = m679a(geoPoint);
        this.f544k = new Point(this.f546m.m750c() / 2, this.f546m.m752d() / 2);
        this.f545l = new MapProjection();
        if (ConfigableConst.f338h == EnumMapProjection.projection_900913) {
            this.f545l.f530a = -2.0037508E7f;
            this.f545l.f531b = 2.0037508E7f;
            this.f545l.f532c = 2.0037508E7f;
            this.f545l.f533d = -2.0037508E7f;
        }
    }

    public void m682a(Point point) {
        this.f544k = point;
    }

    private int m674a(int i, int i2) {
        int i3 = 1;
        for (int i4 = 0; i4 < i2; i4++) {
            i3 *= i;
        }
        return i3;
    }

    public GeoPoint m679a(GeoPoint geoPoint) {
        if (geoPoint == null) {
            return null;
        }
        if (ConfigableConst.f338h == EnumMapProjection.projection_custBeijing54) {
            return geoPoint.m468e();
        }
        if (ConfigableConst.f338h != EnumMapProjection.projection_900913) {
            return null;
        }
        return new GeoPoint(((Math.log(Math.tan((((((double) geoPoint.getLatitudeE6()) / 1000000.0d) + 90.0d) * 3.141592653589793d) / 360.0d)) / 0.017453292519943295d) * 2.003750834E7d) / VMapProjection.MaxLongitude, ((((double) geoPoint.getLongitudeE6()) / 1000000.0d) * 2.003750834E7d) / VMapProjection.MaxLongitude, false);
    }

    PointF m677a(GeoPoint geoPoint, GeoPoint geoPoint2, Point point, double d) {
        PointF pointF = new PointF();
        pointF.x = (float) (((geoPoint.m466c() - geoPoint2.m466c()) / d) + ((double) point.x));
        pointF.y = (float) (((double) point.y) - ((geoPoint.m467d() - geoPoint2.m467d()) / d));
        return pointF;
    }

    public GeoPoint m687b(GeoPoint geoPoint) {
        if (ConfigableConst.f338h == EnumMapProjection.projection_custBeijing54) {
            return new GeoPoint(geoPoint.m467d(), geoPoint.m466c(), (long) geoPoint.m467d(), (long) geoPoint.m466c());
        }
        if (ConfigableConst.f338h != EnumMapProjection.projection_900913) {
            return null;
        }
        return new GeoPoint((int) (((double) ((float) (57.29577951308232d * ((2.0d * Math.atan(Math.exp((((double) ((float) ((geoPoint.m467d() * VMapProjection.MaxLongitude) / 2.003750834E7d))) * 3.141592653589793d) / VMapProjection.MaxLongitude))) - 1.5707963267948966d)))) * 1000000.0d), (int) (((double) ((float) ((geoPoint.m466c() * VMapProjection.MaxLongitude) / 2.003750834E7d))) * 1000000.0d));
    }

    public GeoPoint m678a(PointF pointF, GeoPoint geoPoint, Point point, double d, MapProjection mapProjection) {
        double c = (((double) (pointF.x - ((float) point.x))) * d) + geoPoint.m466c();
        double d2 = geoPoint.m467d() - (((double) (pointF.y - ((float) point.y))) * d);
        if (ConfigableConst.f338h == EnumMapProjection.projection_900913) {
            while (c < ((double) mapProjection.f530a)) {
                c += (double) (mapProjection.f531b - mapProjection.f530a);
            }
            while (c > ((double) mapProjection.f531b)) {
                c -= (double) (mapProjection.f531b - mapProjection.f530a);
            }
            while (d2 < ((double) mapProjection.f533d)) {
                d2 += (double) (mapProjection.f532c - mapProjection.f533d);
            }
            while (d2 > ((double) mapProjection.f532c)) {
                d2 -= (double) (mapProjection.f532c - mapProjection.f533d);
            }
        }
        return m687b(new GeoPoint(d2, c, false));
    }

    GeoPoint m686b(PointF pointF, GeoPoint geoPoint, Point point, double d, MapProjection mapProjection) {
        double c = (((double) (pointF.x - ((float) point.x))) * d) + geoPoint.m466c();
        double d2 = geoPoint.m467d() - (((double) (pointF.y - ((float) point.y))) * d);
        if (ConfigableConst.f338h == EnumMapProjection.projection_900913) {
            while (c < ((double) mapProjection.f530a)) {
                c += (double) (mapProjection.f531b - mapProjection.f530a);
            }
            while (c > ((double) mapProjection.f531b)) {
                c -= (double) (mapProjection.f531b - mapProjection.f530a);
            }
            while (d2 < ((double) mapProjection.f533d)) {
                d2 += (double) (mapProjection.f532c - mapProjection.f533d);
            }
            while (d2 > ((double) mapProjection.f532c)) {
                d2 -= (double) (mapProjection.f532c - mapProjection.f533d);
            }
        }
        return new GeoPoint(d2, c, false);
    }

    public PointF m685b(GeoPoint geoPoint, GeoPoint geoPoint2, Point point, double d) {
        return m677a(m679a(geoPoint), geoPoint2, point, d);
    }

    public Tile m688b() {
        double d = this.f541h[this.f540g];
        int i = 0;
        int c = (int) ((this.f543j.m466c() - this.f536c) / (d * 256.0d));
        if (this.f535b == 0) {
            i = (int) ((this.f537d - this.f543j.m467d()) / (d * 256.0d));
        } else if (this.f535b == 1) {
            i = (int) ((this.f543j.m467d() - this.f537d) / (d * 256.0d));
        }
        return new Tile(c, i, this.f540g, -1);
    }

    public ArrayList<Tile> m680a(GeoPoint geoPoint, int i, int i2, int i3) {
        int i4;
        double d = this.f541h[this.f540g];
        int c = (int) ((geoPoint.m466c() - this.f536c) / (256.0d * d));
        double d2 = this.f536c + (((double) (c * KEYRecord.OWNER_ZONE)) * d);
        double d3 = 0.0d;
        int d4;
        if (this.f535b == 0) {
            d4 = (int) ((this.f537d - geoPoint.m467d()) / (256.0d * d));
            d3 = this.f537d - (((double) (d4 * KEYRecord.OWNER_ZONE)) * d);
            i4 = d4;
        } else if (this.f535b == 1) {
            d4 = (int) ((geoPoint.m467d() - this.f537d) / (256.0d * d));
            d3 = ((double) ((d4 + 1) * KEYRecord.OWNER_ZONE)) * d;
            i4 = d4;
        } else {
            i4 = 0;
        }
        GeoPoint geoPoint2 = new GeoPoint(d3, d2, false);
        PointF a = m677a(r0, geoPoint, this.f544k, d);
        Tile tile = new Tile(c, i4, this.f540g, -1);
        tile.f634f = a;
        ArrayList<Tile> arrayList = new ArrayList();
        arrayList.add(tile);
        int i5 = 1;
        while (true) {
            int i6;
            Object obj;
            Object obj2 = null;
            for (i6 = c - i5; i6 <= c + i5; i6++) {
                int i7 = i4 + i5;
                PointF a2 = m676a(i6, i7, c, i4, a, i2, i3);
                if (a2 != null) {
                    if (obj2 == null) {
                        obj = 1;
                    } else {
                        obj = obj2;
                    }
                    Tile tile2 = new Tile(i6, i7, this.f540g, -1);
                    tile2.f634f = a2;
                    arrayList.add(tile2);
                    obj2 = obj;
                }
                i7 = i4 - i5;
                PointF a3 = m676a(i6, i7, c, i4, a, i2, i3);
                if (a3 != null) {
                    if (obj2 == null) {
                        obj2 = 1;
                    }
                    Tile tile3 = new Tile(i6, i7, this.f540g, -1);
                    tile3.f634f = a3;
                    arrayList.add(tile3);
                }
            }
            for (i7 = (i4 + i5) - 1; i7 > i4 - i5; i7--) {
                i6 = c + i5;
                a2 = m676a(i6, i7, c, i4, a, i2, i3);
                if (a2 != null) {
                    if (obj2 == null) {
                        obj = 1;
                    } else {
                        obj = obj2;
                    }
                    tile2 = new Tile(i6, i7, this.f540g, -1);
                    tile2.f634f = a2;
                    arrayList.add(tile2);
                    obj2 = obj;
                }
                i6 = c - i5;
                a3 = m676a(i6, i7, c, i4, a, i2, i3);
                if (a3 != null) {
                    if (obj2 == null) {
                        obj2 = 1;
                    }
                    tile3 = new Tile(i6, i7, this.f540g, -1);
                    tile3.f634f = a3;
                    arrayList.add(tile3);
                }
            }
            if (obj2 == null) {
                return arrayList;
            }
            i5++;
        }
    }

    PointF m676a(int i, int i2, int i3, int i4, PointF pointF, int i5, int i6) {
        PointF pointF2 = new PointF();
        pointF2.x = ((float) ((i - i3) * KEYRecord.OWNER_ZONE)) + pointF.x;
        if (this.f535b == 0) {
            pointF2.y = ((float) ((i2 - i4) * KEYRecord.OWNER_ZONE)) + pointF.y;
        } else if (this.f535b == 1) {
            pointF2.y = pointF.y - ((float) ((i2 - i4) * KEYRecord.OWNER_ZONE));
        }
        if (pointF2.x + 256.0f <= 0.0f || pointF2.x >= ((float) i5) || pointF2.y + 256.0f <= 0.0f || pointF2.y >= ((float) i6)) {
            return null;
        }
        return pointF2;
    }

    public void m683a(PointF pointF, PointF pointF2, int i) {
        double d = this.f541h[i];
        GeoPoint b = m686b(pointF, this.f543j, this.f544k, d, this.f545l);
        GeoPoint b2 = m686b(pointF2, this.f543j, this.f544k, d, this.f545l);
        double d2 = b2.m467d() - b.m467d();
        double c = this.f543j.m466c() + (b2.m466c() - b.m466c());
        double d3 = this.f543j.m467d() + d2;
        if (ConfigableConst.f338h == EnumMapProjection.projection_900913) {
            while (c < ((double) this.f545l.f530a)) {
                c += (double) (this.f545l.f531b - this.f545l.f530a);
            }
            while (c > ((double) this.f545l.f531b)) {
                c -= (double) (this.f545l.f531b - this.f545l.f530a);
            }
            while (d3 < ((double) this.f545l.f533d)) {
                d3 += (double) (this.f545l.f532c - this.f545l.f533d);
            }
            while (d3 > ((double) this.f545l.f532c)) {
                d3 -= (double) (this.f545l.f532c - this.f545l.f533d);
            }
        }
        this.f543j.m465b(d3);
        this.f543j.m463a(c);
    }

    public float m675a(GeoPoint geoPoint, GeoPoint geoPoint2) {
        double a = CoreUtil.m481a(geoPoint.m462a());
        double a2 = CoreUtil.m481a(geoPoint.m464b());
        double a3 = CoreUtil.m481a(geoPoint2.m462a());
        a *= this.f549p;
        a2 *= this.f549p;
        a3 *= this.f549p;
        double a4 = CoreUtil.m481a(geoPoint2.m464b()) * this.f549p;
        double sin = Math.sin(a);
        double sin2 = Math.sin(a2);
        a = Math.cos(a);
        a2 = Math.cos(a2);
        double sin3 = Math.sin(a3);
        double sin4 = Math.sin(a4);
        a3 = Math.cos(a3);
        a4 = Math.cos(a4);
        r17 = new double[3];
        double[] dArr = new double[]{a * a2, a2 * sin, sin2};
        dArr[0] = a4 * a3;
        dArr[1] = a4 * sin3;
        dArr[2] = sin4;
        return (float) (Math.asin(Math.sqrt((((r17[0] - dArr[0]) * (r17[0] - dArr[0])) + ((r17[1] - dArr[1]) * (r17[1] - dArr[1]))) + ((r17[2] - dArr[2]) * (r17[2] - dArr[2]))) / 2.0d) * 1.27420015798544E7d);
    }
}
