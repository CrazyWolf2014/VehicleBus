package com.amap.mapapi.map;

import android.graphics.Matrix;
import android.graphics.Point;
import android.view.animation.Animation.AnimationListener;
import com.amap.mapapi.core.GeoPoint;
import org.codehaus.jackson.smile.SmileConstants;

/* compiled from: ZoomCtlAnim */
class bk extends AnimBase {
    static float f1885h;
    public MapView f1886e;
    public float f1887f;
    public float f1888g;
    public int f1889i;
    public boolean f1890j;
    private AnimationListener f1891k;
    private float f1892l;
    private float f1893m;
    private float f1894n;
    private boolean f1895o;
    private boolean f1896p;

    static {
        f1885h = 1.0f;
    }

    public bk(MapView mapView, AnimationListener animationListener) {
        super(SmileConstants.TOKEN_PREFIX_SHORT_UNICODE, 40);
        this.f1896p = false;
        this.f1889i = -1;
        this.f1890j = false;
        this.f1886e = mapView;
        this.f1891k = animationListener;
    }

    protected void m1980a() {
        if (this.f1895o) {
            f1885h += this.f1894n;
        } else {
            f1885h -= this.f1894n;
        }
        Matrix matrix = new Matrix();
        matrix.setScale(f1885h, f1885h, this.f1887f, this.f1888g);
        this.f1886e.m648b().m1911b(f1885h);
        this.f1886e.m648b().m1912b(matrix);
    }

    protected void m1983b() {
        if (!this.f1896p) {
            Point point;
            GeoPoint fromPixels;
            this.f1886e.m642a().f596d.f568e = false;
            if (this.f1890j) {
                point = new Point((int) this.f1887f, (int) this.f1888g);
                fromPixels = this.f1886e.getProjection().fromPixels((int) this.f1887f, (int) this.f1888g);
                this.f1886e.m642a().f598f.f543j = this.f1886e.m642a().f598f.m679a(fromPixels);
                this.f1886e.m642a().f598f.m682a(point);
                this.f1886e.m642a().f594b.m746a(false, false);
            }
            this.f1886e.getController().setZoom(this.f1889i);
            this.f1891k.onAnimationEnd(null);
            if (this.f1890j) {
                point = new Point(this.f1886e.m642a().f594b.m750c() / 2, this.f1886e.m642a().f594b.m752d() / 2);
                fromPixels = this.f1886e.getProjection().fromPixels(this.f1886e.m642a().f594b.m750c() / 2, this.f1886e.m642a().f594b.m752d() / 2);
                this.f1886e.m642a().f598f.f543j = this.f1886e.m642a().f598f.m679a(fromPixels);
                this.f1886e.m642a().f598f.m682a(point);
                this.f1886e.m642a().f594b.m746a(false, false);
            }
            this.f1886e.m643a(0);
            f1885h = 1.0f;
            ah.f599j = 1.0f;
        }
    }

    public void m1981a(float f, int i, boolean z, float f2, float f3) {
        this.f1895o = z;
        this.f1887f = f2;
        this.f1888g = f3;
        this.f1892l = f;
        f1885h = this.f1892l;
        if (this.f1895o) {
            this.f1894n = (this.f1892l * ((float) this.d)) / ((float) this.c);
            this.f1893m = this.f1892l * 2.0f;
            return;
        }
        this.f1894n = ((this.f1892l * 0.5f) * ((float) this.d)) / ((float) this.c);
        this.f1893m = this.f1892l * 0.5f;
    }

    public void m1982a(int i, boolean z, float f, float f2) {
        this.f1886e.f486c[0] = this.f1886e.f486c[1];
        this.f1886e.f486c[1] = i;
        if (this.f1886e.f486c[0] != this.f1886e.f486c[1]) {
            this.f1886e.getZoomMgr().m622d();
            if (m662f()) {
                this.f1896p = true;
                m660d();
                m1981a(this.f1893m, i, z, f, f2);
                this.f1886e.m642a().f596d.m709a(true);
                this.f1886e.m642a().f596d.f568e = true;
                this.f1891k.onAnimationStart(null);
                super.m659c();
                this.f1896p = false;
                return;
            }
            this.c = SmileConstants.TOKEN_PREFIX_SHORT_UNICODE;
            m1981a(this.f1886e.m648b().m1909b(), i, z, f, f2);
            this.f1886e.m642a().f596d.m709a(true);
            this.f1886e.m642a().f596d.f568e = true;
            this.f1891k.onAnimationStart(null);
            super.m659c();
        }
    }
}
