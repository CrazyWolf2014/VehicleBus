package com.amap.mapapi.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.amap.mapapi.core.ConfigableConst;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.GeoPoint.EnumMapProjection;
import com.amap.mapapi.core.IDRefPool;
import com.amap.mapapi.core.MapServerUrl;
import com.amap.mapapi.core.PublicResManager;
import com.amap.mapapi.core.RouteResource;
import com.amap.mapapi.core.SyncList;
import com.amap.mapapi.map.MapView.LayoutParams;
import com.amap.mapapi.map.MapView.ReticleDrawMode;
import com.amap.mapapi.map.ar.Tile;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: Mediator */
class ag {
    public Mediator f593a;
    public Mediator f594b;
    public Mediator f595c;
    public Mediator f596d;
    public Mediator f597e;
    public ab f598f;

    /* renamed from: com.amap.mapapi.map.ag.a */
    public class Mediator {
        public SyncList<LayerPropertys> f564a;
        public SyncList<Overlay> f565b;
        long f566c;
        BitmapDrawer f567d;
        public boolean f568e;
        public boolean f569f;
        String f570g;
        String f571h;
        String f572i;
        String f573j;
        final /* synthetic */ ag f574k;
        private boolean f575l;
        private IDRefPool<FixPosOverlay> f576m;
        private ReticleDrawMode f577n;
        private int f578o;
        private int f579p;
        private boolean f580q;

        /* renamed from: com.amap.mapapi.map.ag.a.1 */
        class Mediator extends be {
            final /* synthetic */ Mediator f1858a;

            Mediator(Mediator mediator) {
                this.f1858a = mediator;
            }

            public String m1932a(int i, int i2, int i3) {
                return MapServerUrl.m503a().m505b() + "/mapabc/maptile?v=w2.61&zoom=" + (17 - i3) + "&x=" + i + "&y=" + i2;
            }
        }

        public Mediator(ag agVar, MapActivity mapActivity) {
            this.f574k = agVar;
            this.f575l = false;
            this.f564a = null;
            this.f565b = new SyncList();
            this.f576m = new IDRefPool();
            this.f577n = ReticleDrawMode.DRAW_RETICLE_NEVER;
            this.f568e = false;
            this.f569f = false;
            this.f578o = 0;
            this.f579p = 0;
            this.f570g = "GridMap";
            this.f571h = "SatelliteMap";
            this.f572i = "GridTmc";
            this.f573j = "SateliteTmc";
            this.f580q = false;
            if (mapActivity != null) {
                m700f();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                mapActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int i = (displayMetrics.widthPixels / KEYRecord.OWNER_ZONE) + 2;
                int i2 = (displayMetrics.heightPixels / KEYRecord.OWNER_ZONE) + 2;
                this.f578o = i2 + (i + (i * i2));
                this.f579p = (this.f578o / 8) + 1;
                if (this.f579p == 0) {
                    this.f579p = 1;
                } else if (this.f579p > 5) {
                    this.f579p = 5;
                }
                m692a((Context) mapActivity);
            }
        }

        private void m692a(Context context) {
            if (this.f564a == null) {
                this.f564a = new SyncList();
            }
            LayerPropertys layerPropertys = new LayerPropertys();
            layerPropertys.f1927j = new Mediator(this);
            layerPropertys.f1918a = this.f570g;
            layerPropertys.f1922e = true;
            layerPropertys.f1921d = true;
            layerPropertys.f1923f = true;
            layerPropertys.f1924g = true;
            layerPropertys.f1919b = 18;
            layerPropertys.f1920c = 4;
            m713a(layerPropertys, context);
        }

        boolean m714a(String str, boolean z) {
            if (str.equals(XmlPullParser.NO_NAMESPACE)) {
                return false;
            }
            int size = this.f564a.size();
            for (int i = 0; i < size; i++) {
                LayerPropertys layerPropertys = (LayerPropertys) this.f564a.get(i);
                if (layerPropertys != null && layerPropertys.f1918a.equals(str)) {
                    layerPropertys.f1923f = z;
                    if (!layerPropertys.f1922e) {
                        return true;
                    }
                    if (z) {
                        if (layerPropertys.f1919b > layerPropertys.f1920c) {
                            this.f574k.f594b.m748b(layerPropertys.f1919b);
                            this.f574k.f594b.m751c(layerPropertys.f1920c);
                        }
                        m696b(str);
                        this.f574k.f594b.m746a(false, false);
                        return true;
                    }
                }
            }
            return false;
        }

        private void m696b(String str) {
            if (!str.equals(XmlPullParser.NO_NAMESPACE)) {
                int size = this.f564a.size();
                for (int i = 0; i < size; i++) {
                    LayerPropertys layerPropertys = (LayerPropertys) this.f564a.get(i);
                    if (layerPropertys != null && !layerPropertys.f1918a.equals(str) && layerPropertys.f1922e && layerPropertys.f1923f) {
                        layerPropertys.f1923f = false;
                    }
                }
            }
        }

        private boolean m698c(String str) {
            if (this.f564a == null) {
                return false;
            }
            int size = this.f564a.size();
            for (int i = 0; i < size; i++) {
                LayerPropertys layerPropertys = (LayerPropertys) this.f564a.get(i);
                if (layerPropertys != null && layerPropertys.f1918a.equals(str)) {
                    return true;
                }
            }
            return false;
        }

        boolean m713a(LayerPropertys layerPropertys, Context context) {
            if (layerPropertys == null || layerPropertys.f1918a.equals(XmlPullParser.NO_NAMESPACE) || m698c(layerPropertys.f1918a)) {
                return false;
            }
            boolean add;
            layerPropertys.f1932o = new SyncList();
            layerPropertys.f1930m = new BitmapManager(this.f578o, this.f579p, layerPropertys.f1925h, layerPropertys.f1926i);
            layerPropertys.f1931n = new CachManager(context, this.f574k.f594b.f591c.f488e, layerPropertys);
            layerPropertys.f1931n.m846a(layerPropertys.f1930m);
            int size = this.f564a.size();
            if (!layerPropertys.f1922e || size == 0) {
                add = this.f564a.add(layerPropertys);
            } else {
                for (int i = size - 1; i >= 0; i--) {
                    LayerPropertys layerPropertys2 = (LayerPropertys) this.f564a.get(i);
                    if (layerPropertys2 != null && layerPropertys2.f1922e) {
                        this.f564a.add(i, layerPropertys);
                        add = false;
                        break;
                    }
                }
                add = false;
            }
            m699e();
            if (layerPropertys.f1923f) {
                m714a(layerPropertys.f1918a, true);
            }
            return add;
        }

        private void m699e() {
            int size = this.f564a.size();
            for (int i = 0; i < size; i++) {
                LayerPropertys layerPropertys = (LayerPropertys) this.f564a.get(i);
                if (layerPropertys != null) {
                    layerPropertys.f1928k = i;
                }
            }
        }

        LayerPropertys m702a(String str) {
            if (str.equals(XmlPullParser.NO_NAMESPACE) || this.f564a == null || this.f564a.size() == 0) {
                return null;
            }
            int size = this.f564a.size();
            for (int i = 0; i < size; i++) {
                LayerPropertys layerPropertys = (LayerPropertys) this.f564a.get(i);
                if (layerPropertys != null && layerPropertys.f1918a.equals(str)) {
                    return layerPropertys;
                }
            }
            return null;
        }

        private void m700f() {
            String str = Build.MODEL;
            if (str == null) {
                return;
            }
            if (str.indexOf("OMAP_SS") > -1 || str.indexOf("omap_ss") > -1 || str.indexOf("MT810") > -1 || str.indexOf("MT720") > -1 || str.indexOf("GT-I9008") > -1) {
                ConfigableConst.f345o = true;
            }
        }

        public void m707a(ReticleDrawMode reticleDrawMode) {
            this.f577n = reticleDrawMode;
        }

        public void m703a() {
            int i;
            int size = this.f565b.size();
            for (i = 0; i < size; i++) {
                Overlay overlay = (Overlay) this.f565b.remove(0);
                if (overlay instanceof MyLocationOverlay) {
                    MyLocationOverlay myLocationOverlay = (MyLocationOverlay) overlay;
                    if (myLocationOverlay != null) {
                        myLocationOverlay.m1920a();
                    }
                }
            }
            size = this.f576m.size();
            for (i = 0; i < size; i++) {
                FixPosOverlay fixPosOverlay = (FixPosOverlay) this.f576m.remove(0);
                if (fixPosOverlay != null) {
                    fixPosOverlay.m1998b();
                }
            }
        }

        public void m715b() {
            int size = this.f574k.f596d.f564a.size();
            for (int i = 0; i < size; i++) {
                LayerPropertys layerPropertys = (LayerPropertys) this.f574k.f596d.f564a.get(0);
                if (layerPropertys != null) {
                    layerPropertys.m2006a();
                    this.f574k.f596d.f564a.remove(0);
                }
            }
            this.f574k.f596d.f564a = null;
        }

        public List<Overlay> m719c() {
            return this.f565b;
        }

        public boolean m712a(GeoPoint geoPoint) {
            SyncList syncList;
            Tile b = this.f574k.f598f.m688b();
            int size = this.f564a.size();
            for (int i = 0; i < size; i++) {
                if (((LayerPropertys) this.f564a.get(i)).f1922e) {
                    syncList = ((LayerPropertys) this.f564a.get(i)).f1932o;
                    break;
                }
            }
            syncList = null;
            if (syncList == null) {
                return false;
            }
            boolean z;
            Iterator it = syncList.iterator();
            while (it.hasNext()) {
                Tile tile = (Tile) it.next();
                if (tile.f630b == b.f630b && tile.f631c == b.f631c && tile.f632d == b.f632d) {
                    if (tile.f635g >= 0) {
                        z = true;
                        return z;
                    }
                    z = false;
                    return z;
                }
            }
            z = false;
            return z;
        }

        public void m704a(int i, int i2, int i3, int i4) {
            if (m694a(this.f566c)) {
                m716b(i, i2, i3, i4);
            } else {
                this.f580q = true;
            }
        }

        public void m721d() {
            if (this.f574k.f594b != null && this.f574k.f594b.f591c != null) {
                this.f574k.f594b.f591c.postInvalidate();
            }
        }

        public void m716b(int i, int i2, int i3, int i4) {
            this.f574k.f594b.f591c.postInvalidate(i, i2, i3, i4);
            this.f566c = System.currentTimeMillis();
            this.f580q = false;
        }

        private boolean m694a(long j) {
            return System.currentTimeMillis() - j > 300;
        }

        public void m709a(boolean z) {
            this.f575l = z;
        }

        public void m708a(FixPosOverlay fixPosOverlay, boolean z) {
            if (z) {
                this.f576m.m1873a(fixPosOverlay);
            } else {
                this.f576m.m1874b(fixPosOverlay);
            }
        }

        public void m705a(Canvas canvas, Matrix matrix, float f, float f2) {
            if (this.f575l) {
                canvas.save();
                canvas.translate(f, f2);
                canvas.concat(matrix);
                m693a(canvas);
                canvas.restore();
                if (!(this.f568e || this.f569f)) {
                    m709a(false);
                    this.f574k.f594b.f591c.m648b().m1912b(new Matrix());
                    this.f574k.f594b.f591c.m648b().m1911b(1.0f);
                    this.f574k.f594b.f591c.m648b().m1914c();
                }
            } else {
                m693a(canvas);
            }
            this.f574k.f594b.m756h();
            m695b(canvas);
        }

        private void m693a(Canvas canvas) {
            if (!this.f574k.f594b.f591c.VMapMode) {
                int size = this.f564a.size();
                for (int i = 0; i < size; i++) {
                    LayerPropertys layerPropertys = (LayerPropertys) this.f564a.get(i);
                    if (layerPropertys != null && layerPropertys.f1923f) {
                        layerPropertys.m2007a(canvas);
                    }
                }
            } else if (this.f574k.f594b.f591c.isInited) {
                this.f574k.f594b.f591c.paintVectorMap(canvas);
            }
        }

        private void m695b(Canvas canvas) {
            long a = CoreUtil.m483a();
            Iterator it = this.f565b.iterator();
            RouteOverlay routeOverlay = null;
            MyLocationOverlay myLocationOverlay = null;
            while (it.hasNext()) {
                Overlay overlay = (Overlay) it.next();
                if (overlay != null) {
                    if (overlay instanceof RouteOverlay) {
                        routeOverlay = (RouteOverlay) overlay;
                    } else {
                        MyLocationOverlay myLocationOverlay2;
                        if (overlay instanceof MyLocationOverlay) {
                            myLocationOverlay2 = (MyLocationOverlay) overlay;
                        } else {
                            overlay.draw(canvas, this.f574k.f594b.f591c, false, a);
                            myLocationOverlay2 = myLocationOverlay;
                        }
                        myLocationOverlay = myLocationOverlay2;
                    }
                }
            }
            if (this.f577n == ReticleDrawMode.DRAW_RETICLE_OVER) {
                m697c(canvas);
            }
            if (myLocationOverlay != null) {
                myLocationOverlay.draw(canvas, this.f574k.f594b.f591c, false, a);
            }
            if (routeOverlay != null) {
                routeOverlay.draw(canvas, this.f574k.f594b.f591c, false, a);
            }
            Iterator it2 = this.f576m.iterator();
            while (it2.hasNext()) {
                ((FixPosOverlay) it2.next()).draw(canvas, this.f574k.f594b.f591c, false, a);
            }
        }

        public PointF m701a(Tile tile) {
            if (tile == null) {
                return null;
            }
            return tile.f634f;
        }

        public void m706a(Canvas canvas, MapView mapView, Tile tile) {
            PointF a = m701a(tile);
            Rect rect = new Rect((int) a.x, (int) a.y, (int) (a.x + 256.0f), (int) (a.y + 256.0f));
            Paint paint = new Paint();
            paint.setStyle(Style.STROKE);
            paint.setColor(-7829368);
            canvas.drawRect(rect, paint);
        }

        private void m697c(Canvas canvas) {
            int size = this.f564a.size();
            for (int i = 0; i < size; i++) {
                LayerPropertys layerPropertys = (LayerPropertys) this.f564a.get(i);
                if (layerPropertys != null && layerPropertys.f1923f && layerPropertys.f1922e) {
                    int size2 = layerPropertys.f1932o.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        m706a(canvas, this.f574k.f594b.f591c, (Tile) layerPropertys.f1932o.get(i2));
                    }
                }
            }
        }

        public boolean m710a(int i, KeyEvent keyEvent) {
            boolean z = false;
            Iterator it = this.f565b.iterator();
            while (it.hasNext()) {
                z = ((Overlay) it.next()).onKeyDown(i, keyEvent, this.f574k.f594b.f591c);
                if (z) {
                    break;
                }
            }
            return z;
        }

        public boolean m717b(int i, KeyEvent keyEvent) {
            boolean z = false;
            Iterator it = this.f565b.iterator();
            while (it.hasNext()) {
                z = ((Overlay) it.next()).onKeyUp(i, keyEvent, this.f574k.f594b.f591c);
                if (z) {
                    break;
                }
            }
            return z;
        }

        public boolean m711a(MotionEvent motionEvent) {
            boolean z = false;
            Iterator it = this.f565b.iterator();
            while (it.hasNext()) {
                z = ((Overlay) it.next()).onTrackballEvent(motionEvent, this.f574k.f594b.f591c);
                if (z) {
                    break;
                }
            }
            return z;
        }

        public boolean m718b(MotionEvent motionEvent) {
            Iterator it = this.f565b.iterator();
            boolean z = false;
            while (it.hasNext()) {
                boolean onTouchEvent;
                Overlay overlay = (Overlay) it.next();
                if (overlay != null) {
                    onTouchEvent = overlay.onTouchEvent(motionEvent, this.f574k.f594b.f591c);
                } else {
                    onTouchEvent = z;
                }
                if (onTouchEvent) {
                    return onTouchEvent;
                }
                z = onTouchEvent;
            }
            return z;
        }

        protected boolean m720c(MotionEvent motionEvent) {
            GeoPoint fromPixels = this.f574k.f593a.fromPixels((int) motionEvent.getX(), (int) motionEvent.getY());
            for (int size = this.f565b.size() - 1; size >= 0; size--) {
                Overlay overlay = (Overlay) this.f565b.get(size);
                if (overlay != null) {
                    if (!(overlay instanceof ItemizedOverlay)) {
                        if (overlay.onTap(fromPixels, this.f574k.f594b.f591c)) {
                            break;
                        }
                    } else if (((ItemizedOverlay) overlay).onTap(fromPixels, this.f574k.f594b.f591c)) {
                        break;
                    }
                }
            }
            return false;
        }
    }

    /* renamed from: com.amap.mapapi.map.ag.b */
    public class Mediator {
        int f581a;
        final /* synthetic */ ag f582b;

        public Mediator(ag agVar) {
            this.f582b = agVar;
            this.f581a = 0;
            m726e();
        }

        public void m722a() {
            for (Entry value : this.f582b.f597e.f588f.entrySet()) {
                ad adVar = (ad) value.getValue();
                if (adVar != null) {
                    adVar.a_();
                }
            }
            this.f582b.f597e.m731e();
        }

        public void m723b() {
            this.f582b.f594b.f589a = false;
            for (Entry value : this.f582b.f597e.f588f.entrySet()) {
                ad adVar = (ad) value.getValue();
                if (adVar != null) {
                    adVar.m689a();
                }
            }
            if (this.f582b.f596d != null && this.f582b.f596d.f567d != null) {
                this.f582b.f596d.f567d.m822a();
            }
        }

        public void m724c() {
            for (Entry value : this.f582b.f597e.f588f.entrySet()) {
                ad adVar = (ad) value.getValue();
                if (adVar != null) {
                    adVar.m690e();
                }
            }
        }

        public void m725d() {
            for (Entry value : this.f582b.f597e.f588f.entrySet()) {
                ad adVar = (ad) value.getValue();
                if (adVar != null) {
                    adVar.m691f();
                }
            }
        }

        public void m726e() {
            for (Entry value : this.f582b.f597e.f588f.entrySet()) {
                ad adVar = (ad) value.getValue();
                if (adVar != null) {
                    adVar.b_();
                }
            }
            this.f582b.f597e.m730d();
        }
    }

    /* renamed from: com.amap.mapapi.map.ag.c */
    public class Mediator {
        final /* synthetic */ ag f583a;
        private String f584b;
        private String f585c;
        private final MapActivity f586d;
        private Proxy f587e;
        private Map<Integer, ad> f588f;

        public Mediator(ag agVar, ag agVar2, MapActivity mapActivity, String str) {
            this.f583a = agVar;
            this.f584b = XmlPullParser.NO_NAMESPACE;
            this.f585c = "android";
            this.f588f = new HashMap();
            this.f586d = mapActivity;
            if (str != null) {
                this.f585c = str;
            }
            if (ConfigableConst.f335e == 2) {
                this.f585c = "androidh";
            } else if (ConfigableConst.f335e == 1) {
                this.f585c = "androidl";
            } else {
                this.f585c = "android";
            }
            this.f584b = CoreUtil.m485a(this.f586d);
            if (agVar2.f594b.f591c.VMapMode) {
                this.f588f.put(Integer.valueOf(1), new bg(agVar2, mapActivity));
                this.f588f.put(Integer.valueOf(2), new CompassServer(agVar2, mapActivity));
                return;
            }
            this.f588f.put(Integer.valueOf(0), new au(agVar2, mapActivity));
            this.f588f.put(Integer.valueOf(2), new CompassServer(agVar2, mapActivity));
        }

        public String m733a() {
            return this.f584b;
        }

        public String m735b() {
            return this.f585c;
        }

        public Proxy m736c() {
            return this.f587e;
        }

        public ad m732a(int i) {
            return (ad) this.f588f.get(Integer.valueOf(i));
        }

        public void m734a(ad adVar, int i) {
            this.f588f.put(Integer.valueOf(i), adVar);
        }

        private void m730d() {
            this.f587e = CoreUtil.m491b(this.f586d);
        }

        private void m731e() {
            this.f587e = null;
        }
    }

    /* renamed from: com.amap.mapapi.map.ag.d */
    public class Mediator {
        public boolean f589a;
        final /* synthetic */ ag f590b;
        private MapView f591c;
        private ArrayList<bi> f592d;

        public Mediator(ag agVar, MapView mapView) {
            this.f590b = agVar;
            this.f589a = true;
            this.f591c = mapView;
            this.f592d = new ArrayList();
        }

        public void m742a(int i) {
            if (this.f591c.VMapMode) {
                if (i != this.f591c.mapLevel) {
                    this.f591c.mapLevel = i;
                }
            } else if (i != this.f590b.f598f.f540g) {
                this.f590b.f598f.f540g = i;
            }
            m746a(false, false);
        }

        public void m743a(int i, int i2) {
            if (i != ConfigableConst.f339i || i2 != ConfigableConst.f340j) {
                ConfigableConst.f339i = i;
                ConfigableConst.f340j = i2;
                m746a(true, false);
            }
        }

        public void m744a(GeoPoint geoPoint) {
            if (geoPoint != null) {
                if (ConfigableConst.f344n) {
                    this.f590b.f598f.f543j = this.f590b.f598f.m679a(geoPoint);
                }
                m746a(false, false);
            }
        }

        public void m749b(GeoPoint geoPoint) {
            GeoPoint f = this.f590b.f594b.m754f();
            if (geoPoint != null && !geoPoint.equals(f)) {
                if (ConfigableConst.f344n) {
                    this.f590b.f598f.f543j = this.f590b.f598f.m679a(geoPoint);
                }
                m746a(false, true);
            }
        }

        public int m741a() {
            return this.f590b.f598f.f539f;
        }

        public void m748b(int i) {
            if (i > 0) {
                ab abVar;
                if (this.f591c.VMapMode) {
                    abVar = this.f590b.f598f;
                    ConfigableConst.f333c = i;
                    abVar.f539f = i;
                    return;
                }
                abVar = this.f590b.f598f;
                ConfigableConst.f331a = i;
                abVar.f539f = i;
            }
        }

        public int m747b() {
            return this.f590b.f598f.f538e;
        }

        public void m751c(int i) {
            if (i > 0) {
                ab abVar;
                if (this.f591c.VMapMode) {
                    abVar = this.f590b.f598f;
                    ConfigableConst.f334d = i;
                    abVar.f538e = i;
                    return;
                }
                abVar = this.f590b.f598f;
                ConfigableConst.f332b = i;
                abVar.f538e = i;
            }
        }

        public int m750c() {
            return ConfigableConst.f339i;
        }

        public int m752d() {
            return ConfigableConst.f340j;
        }

        public int m753e() {
            if (this.f591c.VMapMode) {
                return this.f591c.mapLevel;
            }
            return this.f590b.f598f.f540g;
        }

        public GeoPoint m754f() {
            return this.f590b.f598f.m687b(this.f590b.f598f.f543j);
        }

        public void m745a(bi biVar) {
            this.f592d.add(biVar);
        }

        public void m746a(boolean z, boolean z2) {
            Iterator it = this.f592d.iterator();
            while (it.hasNext()) {
                ((bi) it.next()).m818a(z, z2);
            }
        }

        public MapView m755g() {
            return this.f591c;
        }

        public void m756h() {
            int childCount = this.f591c.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = this.f591c.getChildAt(i);
                if (childAt != null && (childAt.getLayoutParams() instanceof LayoutParams)) {
                    LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                    if (layoutParams.mode == 0) {
                        m739a(childAt, layoutParams);
                    } else {
                        m740b(childAt, layoutParams);
                    }
                }
            }
            this.f590b.f594b.f591c.m649c();
        }

        private void m739a(View view, LayoutParams layoutParams) {
            if (layoutParams.point != null) {
                Point toPixels = this.f590b.f593a.toPixels(layoutParams.point, null);
                toPixels.x += layoutParams.f447x;
                toPixels.y += layoutParams.f448y;
                m738a(view, layoutParams.width, layoutParams.height, toPixels.x, toPixels.y, layoutParams.alignment);
            }
        }

        private void m740b(View view, LayoutParams layoutParams) {
            m738a(view, layoutParams.width, layoutParams.height, layoutParams.f447x, layoutParams.f448y, layoutParams.alignment);
        }

        private void m738a(View view, int i, int i2, int i3, int i4, int i5) {
            if (view instanceof ListView) {
                View view2 = (View) view.getParent();
                if (view2 != null) {
                    i = view2.getWidth();
                    i2 = view2.getHeight();
                }
            }
            view.measure(i, i2);
            if (i == -2) {
                i = view.getMeasuredWidth();
            } else if (i == -1) {
                i = this.f591c.getMeasuredWidth();
            }
            if (i2 == -2) {
                i2 = view.getMeasuredHeight();
            } else if (i2 == -1) {
                i2 = this.f591c.getMeasuredHeight();
            }
            int i6 = i5 & 7;
            int i7 = i5 & Opcodes.IREM;
            if (i6 == 5) {
                i3 -= i;
            } else if (i6 == 1) {
                i3 -= i / 2;
            }
            if (i7 == 80) {
                i4 -= i2;
            } else if (i7 == 16) {
                i4 -= i2 / 2;
            }
            view.layout(i3, i4, i3 + i, i4 + i2);
        }
    }

    /* renamed from: com.amap.mapapi.map.ag.e */
    public class Mediator implements Projection {
        final /* synthetic */ ag f1859a;
        private int f1860b;
        private HashMap<Float, Float> f1861c;

        public Mediator(ag agVar) {
            this.f1859a = agVar;
            this.f1860b = 0;
            this.f1861c = new HashMap();
        }

        public Point toPixels(GeoPoint geoPoint, Point point) {
            int i;
            int e = this.f1859a.f594b.m753e();
            PointF b = this.f1859a.f598f.m685b(geoPoint, this.f1859a.f598f.f543j, this.f1859a.f598f.f544k, this.f1859a.f598f.f541h[e]);
            ah a = this.f1859a.f594b.f591c.m648b().m1900a();
            Point point2 = this.f1859a.f594b.f591c.m642a().f598f.f544k;
            float f;
            float f2;
            if (!a.f615m) {
                f = ((float) point2.x) + (bk.f1885h * ((float) (((int) b.x) - point2.x)));
                f2 = (((float) (((int) b.y) - point2.y)) * bk.f1885h) + ((float) point2.y);
                e = (int) f;
                i = (int) f2;
                if (((double) f) >= ((double) e) + 0.5d) {
                    e++;
                }
                if (((double) f2) >= ((double) i) + 0.5d) {
                    i++;
                }
            } else if (a.f614l) {
                f2 = ((ah.f599j * (((float) ((int) b.x)) - a.f609f.x)) + a.f609f.x) + (a.f610g.x - a.f609f.x);
                f = (((((float) ((int) b.y)) - a.f609f.y) * ah.f599j) + a.f609f.y) + (a.f610g.y - a.f609f.y);
                e = (int) f2;
                i = (int) f;
                if (((double) f2) >= ((double) e) + 0.5d) {
                    e++;
                }
                if (((double) f) >= ((double) i) + 0.5d) {
                    i++;
                }
            } else {
                e = (int) b.x;
                i = (int) b.y;
            }
            point2 = new Point(e, i);
            if (point != null) {
                point.x = point2.x;
                point.y = point2.y;
            }
            return point2;
        }

        public GeoPoint fromPixels(int i, int i2) {
            int e = this.f1859a.f594b.m753e();
            return this.f1859a.f598f.m678a(new PointF((float) i, (float) i2), this.f1859a.f598f.f543j, this.f1859a.f598f.f544k, this.f1859a.f598f.f541h[e], this.f1859a.f598f.f545l);
        }

        public float metersToEquatorPixels(float f) {
            int e = this.f1859a.f594b.m753e();
            if (this.f1861c.size() > 30 || e != this.f1860b) {
                this.f1860b = e;
                this.f1861c.clear();
            }
            if (!this.f1861c.containsKey(Float.valueOf(f))) {
                float a = this.f1859a.f598f.m675a(fromPixels(0, 0), fromPixels(0, 10));
                if (a <= 0.0f) {
                    return 0.0f;
                }
                this.f1861c.put(Float.valueOf(f), Float.valueOf(10.0f * (f / a)));
            }
            return ((Float) this.f1861c.get(Float.valueOf(f))).floatValue();
        }

        public int m1934a() {
            return m1933a(false);
        }

        public int m1935b() {
            return m1933a(true);
        }

        private int m1933a(boolean z) {
            int c = this.f1859a.f594b.m750c();
            GeoPoint fromPixels = fromPixels(0, this.f1859a.f594b.m752d());
            GeoPoint fromPixels2 = fromPixels(c, 0);
            return z ? Math.abs(fromPixels.getLongitudeE6() - fromPixels2.getLongitudeE6()) : Math.abs(fromPixels.getLatitudeE6() - fromPixels2.getLatitudeE6());
        }
    }

    public ag(MapActivity mapActivity, MapView mapView, String str) {
        this.f598f = null;
        ConfigableConst.f338h = EnumMapProjection.projection_900913;
        this.f594b = new Mediator(this, mapView);
        this.f598f = new ab(this.f594b);
        if (mapView.VMapMode) {
            this.f594b.m748b(ConfigableConst.f333c);
            this.f594b.m751c(ConfigableConst.f334d);
            this.f594b.m742a(this.f594b.m753e());
        }
        this.f598f.m681a();
        if (ConfigableConst.f337g == null) {
            ConfigableConst.f337g = new PublicResManager(mapActivity.getApplicationContext());
            ConfigableConst.f337g.m538b();
        }
        this.f597e = new Mediator(this, this, mapActivity, str);
        this.f596d = new Mediator(this, mapActivity);
        this.f593a = new Mediator(this);
        this.f595c = new Mediator(this);
        m757a(this);
        this.f594b.m746a(false, false);
    }

    public void m758a() {
        this.f596d.m703a();
        this.f596d.m715b();
        if (ConfigableConst.f336f == 1) {
            RouteResource.m540a();
            ConfigableConst.f337g.m536a();
            ConfigableConst.f337g = null;
        }
        this.f593a = null;
        this.f594b = null;
        this.f595c = null;
        this.f596d = null;
        this.f597e = null;
    }

    private void m757a(ag agVar) {
        if (Math.random() > 0.1d) {
            this.f596d.m708a(new bj(agVar, ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.ewatermark.ordinal())), true);
        }
    }
}
