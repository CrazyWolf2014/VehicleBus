package com.amap.mapapi.map;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import com.amap.mapapi.core.ConfigableConst;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.ZoomButtonsController.OnZoomListener;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.util.MyHttpException;
import com.mapabc.minimap.map.vmap.VMapProjection;
import java.util.LinkedList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Protocol;

public final class MapController implements OnKeyListener {
    private int f441a;
    private int f442b;
    private ag f443c;
    private boolean f444d;
    private C0090b f445e;
    private C1033a f446f;

    /* renamed from: com.amap.mapapi.map.MapController.b */
    class C0090b implements AnimationListener {
        final /* synthetic */ MapController f437a;
        private LinkedList<Animation> f438b;
        private boolean f439c;
        private bk f440d;

        C0090b(MapController mapController) {
            this.f437a = mapController;
            this.f438b = new LinkedList();
            this.f439c = false;
            this.f440d = null;
        }

        public void m586a() {
            this.f438b.clear();
        }

        public void m587a(int i, int i2, int i3, boolean z, boolean z2) {
            if (z) {
                m585b(i3, i, i2, z2);
            } else {
                m584a(i3, i, i2, z2);
            }
        }

        private void m584a(int i, int i2, int i3, boolean z) {
            if (this.f440d == null) {
                this.f440d = new bk(this.f437a.f443c.f594b.m755g(), this);
            }
            this.f440d.f1890j = z;
            this.f440d.f1889i = i;
            this.f440d.m1982a(i, false, (float) i2, (float) i3);
            this.f439c = true;
        }

        private void m585b(int i, int i2, int i3, boolean z) {
            if (this.f440d == null) {
                this.f440d = new bk(this.f437a.f443c.f594b.m755g(), this);
            }
            this.f440d.f1889i = i;
            this.f440d.f1890j = z;
            if (this.f440d.f1890j) {
                Point point = new Point(i2, i3);
                GeoPoint fromPixels = this.f437a.f443c.f594b.m755g().getProjection().fromPixels(i2, i3);
                this.f437a.f443c.f598f.f543j = this.f437a.f443c.f598f.m679a(fromPixels);
                this.f437a.f443c.f598f.m682a(point);
            }
            this.f440d.m1982a(i, true, (float) i2, (float) i3);
            this.f439c = true;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            MapView g = this.f437a.f443c.f594b.m755g();
            if (this.f438b.size() == 0) {
                this.f439c = false;
                g.getZoomMgr().m617a(true);
                this.f437a.f443c.f596d.m721d();
                return;
            }
            g.m648b().startAnimation((Animation) this.f438b.remove());
        }
    }

    /* renamed from: com.amap.mapapi.map.MapController.a */
    class C1033a implements bd {
        final /* synthetic */ MapController f1807a;
        private bc f1808b;
        private Message f1809c;
        private Runnable f1810d;

        C1033a(MapController mapController) {
            this.f1807a = mapController;
            this.f1808b = null;
            this.f1809c = null;
            this.f1810d = null;
        }

        public void m1888a(GeoPoint geoPoint, Message message, Runnable runnable) {
            m1886a();
            this.f1808b = m1884b(geoPoint);
            this.f1809c = message;
            this.f1810d = runnable;
            this.f1808b.m659c();
        }

        private bc m1884b(GeoPoint geoPoint) {
            return new bc(MyHttpException.ERROR_SERVER, 10, this.f1807a.f443c.f594b.m754f(), geoPoint, this.f1807a.f443c.f594b.m753e(), this);
        }

        private void m1885c() {
            this.f1808b = null;
            this.f1809c = null;
            this.f1810d = null;
        }

        public void m1886a() {
            if (this.f1808b != null) {
                this.f1808b.m660d();
            }
        }

        public void m1887a(GeoPoint geoPoint) {
            if (geoPoint != null) {
                if (geoPoint.m464b() == Long.MIN_VALUE || geoPoint.m462a() == Long.MIN_VALUE) {
                    this.f1807a.setCenter(this.f1807a.f443c.f598f.m687b(geoPoint));
                    return;
                }
                this.f1807a.setCenter(geoPoint);
            }
        }

        public void m1889b() {
            if (this.f1809c != null) {
                this.f1809c.getTarget().sendMessage(this.f1809c);
            }
            if (this.f1810d != null) {
                this.f1810d.run();
            }
            m1885c();
        }
    }

    MapController(ag agVar) {
        this.f441a = 0;
        this.f442b = 0;
        this.f443c = agVar;
        this.f444d = false;
        this.f445e = new C0090b(this);
        this.f446f = new C1033a(this);
    }

    public int getReqLatSpan() {
        return this.f441a;
    }

    public int getReqLngSpan() {
        return this.f442b;
    }

    public void setReqLatSpan(int i) {
        this.f441a = i;
    }

    public void setReqLngSpan(int i) {
        this.f442b = i;
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0) {
            return false;
        }
        boolean z = true;
        switch (i) {
            case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                scrollBy(0, -10);
                break;
            case FileOptions.JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER /*20*/:
                scrollBy(0, 10);
                break;
            case WelcomeActivity.GPIO_IOCSDATAHIGH /*21*/:
                scrollBy(-10, 0);
                break;
            case Protocol.XNS_IDP /*22*/:
                scrollBy(10, 0);
                break;
            default:
                z = false;
                break;
        }
        return z;
    }

    public void setCenter(GeoPoint geoPoint) {
        this.f443c.f594b.m744a(geoPoint);
        if (this.f443c.f594b.m755g().VMapMode) {
            bf LatLongToPixels = VMapProjection.LatLongToPixels(((double) geoPoint.getLatitudeE6()) / 1000000.0d, ((double) geoPoint.getLongitudeE6()) / 1000000.0d, 20);
            this.f443c.f594b.m755g().centerX = LatLongToPixels.f665a;
            this.f443c.f594b.m755g().centerY = LatLongToPixels.f666b;
        }
    }

    public int setZoom(int i) {
        int i2;
        MapView g = this.f443c.f594b.m755g();
        int b = g.m647b(i);
        if (this.f443c.f594b.m755g().VMapMode) {
            i2 = g.mapLevel;
            this.f443c.f594b.m742a(b);
        } else {
            i2 = this.f443c.f598f.f540g;
            this.f443c.f594b.m742a(b);
        }
        if (g.f487d != null) {
            OnZoomListener onZoomListener = g.getZoomButtonsController().getOnZoomListener();
            if (i2 < b && onZoomListener != null) {
                onZoomListener.onZoom(true);
            }
            if (i2 > b && onZoomListener != null) {
                onZoomListener.onZoom(false);
            }
        }
        return b;
    }

    public void zoomToSpan(int i, int i2) {
        if (i > 0 && i2 > 0) {
            int b = this.f443c.f594b.m747b();
            int a = this.f443c.f594b.m741a();
            int e = this.f443c.f594b.m753e();
            int b2 = this.f443c.f593a.m1935b();
            int a2 = this.f443c.f593a.m1934a();
            if (b2 == 0 && a2 == 0) {
                this.f441a = i;
                this.f442b = i2;
                return;
            }
            float max = Math.max(((float) i) / ((float) a2), ((float) i2) / ((float) b2));
            if (max > 1.0f) {
                a = e - m588a(max);
                if (a > b) {
                    b = a;
                }
            } else if (((double) max) < 0.5d) {
                b = (m588a(1.0f / max) + e) - 1;
                if (b >= a) {
                    b = a;
                }
            } else {
                b = e;
            }
            setZoom(b);
        }
    }

    public void setFitView(List<GeoPoint> list) {
        if (list != null && list.size() >= 2) {
            int i = Integer.MAX_VALUE;
            int i2 = Integer.MIN_VALUE;
            int i3 = Integer.MAX_VALUE;
            int i4 = Integer.MIN_VALUE;
            for (int i5 = 0; i5 < list.size(); i5++) {
                GeoPoint geoPoint = (GeoPoint) list.get(i5);
                int longitudeE6 = geoPoint.getLongitudeE6();
                int latitudeE6 = geoPoint.getLatitudeE6();
                if (longitudeE6 < i3) {
                    i3 = longitudeE6;
                }
                if (latitudeE6 < i) {
                    i = latitudeE6;
                }
                if (longitudeE6 > i2) {
                    i2 = longitudeE6;
                }
                if (latitudeE6 > i4) {
                    i4 = latitudeE6;
                }
            }
            setCenter(new GeoPoint((i + i4) / 2, (i3 + i2) / 2));
            zoomToSpan(i4 - i, i2 - i3);
        }
    }

    public static float calculateDistance(GeoPoint geoPoint, GeoPoint geoPoint2) {
        double a = CoreUtil.m481a(geoPoint.m462a());
        double a2 = CoreUtil.m481a(geoPoint.m464b());
        a *= 0.01745329251994329d;
        a2 *= 0.01745329251994329d;
        double a3 = CoreUtil.m481a(geoPoint2.m462a()) * 0.01745329251994329d;
        double a4 = CoreUtil.m481a(geoPoint2.m464b()) * 0.01745329251994329d;
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

    private int m588a(float f) {
        int i = 1;
        int i2 = 0;
        int i3 = 1;
        while (((float) i3) <= f) {
            i3 *= 2;
            i2 = i;
            i++;
        }
        return i2;
    }

    public boolean zoomIn() {
        return m593a(1);
    }

    boolean m593a(int i) {
        return m591a(this.f443c.f594b.m750c() / 2, this.f443c.f594b.m752d() / 2, true, false, i);
    }

    boolean m594b(int i) {
        return m591a(this.f443c.f594b.m750c() / 2, this.f443c.f594b.m752d() / 2, false, false, i);
    }

    public boolean zoomOut() {
        return m594b(1);
    }

    public boolean zoomInFixing(int i, int i2) {
        return m590a(i, i2, true, true);
    }

    public boolean zoomOutFixing(int i, int i2) {
        return m590a(i, i2, false, true);
    }

    public void animateTo(GeoPoint geoPoint) {
        this.f446f.m1888a(geoPoint, null, null);
    }

    public void animateTo(GeoPoint geoPoint, Message message) {
        this.f446f.m1888a(geoPoint, message, null);
    }

    public void animateTo(GeoPoint geoPoint, Runnable runnable) {
        this.f446f.m1888a(geoPoint, null, runnable);
    }

    public void stopAnimation(boolean z) {
        this.f445e.m586a();
        this.f446f.m1886a();
    }

    public void scrollBy(int i, int i2) {
        if (this.f444d) {
            this.f444d = false;
        } else if (i != 0 || i2 != 0) {
            if (ConfigableConst.f344n) {
                this.f443c.f598f.m683a(new PointF(0.0f, 0.0f), new PointF((float) i, (float) i2), this.f443c.f594b.m753e());
            }
            this.f443c.f594b.m746a(false, false);
        }
    }

    public void stopPanning() {
        this.f444d = true;
    }

    public void zoomAnimationAtLevel(int i, int i2, int i3, boolean z, boolean z2) {
        this.f445e.m587a(i, i2, i3, z, z2);
    }

    private boolean m591a(int i, int i2, boolean z, boolean z2, int i3) {
        int b = this.f443c.f594b.m755g().m647b(z ? this.f443c.f594b.m753e() + i3 : this.f443c.f594b.m753e() - i3);
        if (b == this.f443c.f594b.m753e()) {
            return false;
        }
        zoomAnimationAtLevel(i, i2, b, z, z2);
        return true;
    }

    private boolean m590a(int i, int i2, boolean z, boolean z2) {
        return m591a(i, i2, z, z2, 1);
    }

    boolean m592a() {
        return this.f445e.f440d.m662f();
    }
}
