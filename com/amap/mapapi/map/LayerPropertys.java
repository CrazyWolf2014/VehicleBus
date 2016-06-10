package com.amap.mapapi.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import com.amap.mapapi.core.SyncList;
import com.amap.mapapi.map.ar.Tile;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.map.x */
public class LayerPropertys extends LayerPropertys {
    public String f1918a;
    public int f1919b;
    public int f1920c;
    public boolean f1921d;
    public boolean f1922e;
    public boolean f1923f;
    public boolean f1924g;
    public boolean f1925h;
    public long f1926i;
    public be f1927j;
    public int f1928k;
    public String f1929l;
    BitmapManager f1930m;
    CachManager f1931n;
    SyncList<Tile> f1932o;

    public LayerPropertys() {
        this.f1918a = XmlPullParser.NO_NAMESPACE;
        this.f1919b = 18;
        this.f1920c = 4;
        this.f1921d = true;
        this.f1922e = true;
        this.f1923f = false;
        this.f1924g = false;
        this.f1925h = false;
        this.f1926i = 0;
        this.f1927j = null;
        this.f1928k = -1;
        this.f1929l = XmlPullParser.NO_NAMESPACE;
        this.f1930m = null;
        this.f1931n = null;
        this.f1932o = null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LayerPropertys)) {
            return false;
        }
        return this.f1918a.equals(((LayerPropertys) obj).f1918a);
    }

    public int hashCode() {
        return this.f1928k;
    }

    public String toString() {
        return this.f1918a;
    }

    protected void m2007a(Canvas canvas) {
        if (this.f1932o != null) {
            Iterator it = this.f1932o.iterator();
            while (it.hasNext()) {
                Bitmap a;
                Tile tile = (Tile) it.next();
                if (tile.f635g >= 0) {
                    a = this.f1930m.m833a(tile.f635g);
                } else if (this.f1922e) {
                    a = ar.m797c();
                }
                PointF pointF = tile.f634f;
                if (!(a == null || pointF == null)) {
                    canvas.drawBitmap(a, pointF.x, pointF.y, null);
                }
            }
        }
    }

    protected void m2006a() {
        this.f1931n.m846a(null);
        this.f1930m.m835c();
        this.f1932o.clear();
    }
}
