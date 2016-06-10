package com.amap.mapapi.map;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.RouteResource;
import com.amap.mapapi.map.MapView.LayoutParams;
import com.amap.mapapi.route.Route;
import com.amap.mapapi.route.Route.FromAndTo;
import java.util.ArrayList;
import java.util.List;

public class RouteOverlay extends Overlay {
    public static final int OnDetail = 1;
    public static final int OnIconClick = 4;
    public static final int OnNext = 3;
    public static final int OnOverview = 0;
    public static final int OnPrev = 2;
    List<an> f1848a;
    C1037a f1849b;
    private am f1850c;
    private boolean f1851d;
    private boolean f1852e;
    private List<RouteMessageHandler> f1853f;
    private MapView f1854g;
    private int f1855h;
    private boolean f1856i;
    private boolean f1857j;
    protected MapActivity mContext;
    protected Route mRoute;

    /* renamed from: com.amap.mapapi.map.RouteOverlay.a */
    class C1037a implements RouteMessageHandler {
        final /* synthetic */ RouteOverlay f1847a;

        C1037a(RouteOverlay routeOverlay) {
            this.f1847a = routeOverlay;
        }

        public boolean onRouteEvent(MapView mapView, RouteOverlay routeOverlay, int i, int i2) {
            boolean z = false;
            for (RouteMessageHandler onRouteEvent : this.f1847a.f1853f) {
                z = onRouteEvent.onRouteEvent(mapView, routeOverlay, i, i2);
                if (z) {
                    break;
                }
            }
            if (!z) {
                this.f1847a.takeDefaultAction(mapView, i, i2);
            }
            return z;
        }

        private boolean m1921a(MapView mapView, RouteOverlay routeOverlay, int i, GeoPoint geoPoint) {
            if (!this.f1847a.f1852e) {
                return false;
            }
            this.f1847a.closePopupWindow();
            this.f1847a.m1923a(i).m2539a(geoPoint);
            mapView.invalidate();
            return true;
        }

        public void onDragBegin(MapView mapView, RouteOverlay routeOverlay, int i, GeoPoint geoPoint) {
            if (m1921a(mapView, routeOverlay, i, geoPoint)) {
                for (RouteMessageHandler onDragBegin : this.f1847a.f1853f) {
                    onDragBegin.onDragBegin(mapView, routeOverlay, i, geoPoint);
                }
            }
        }

        public void onDragEnd(MapView mapView, RouteOverlay routeOverlay, int i, GeoPoint geoPoint) {
            if (m1921a(mapView, routeOverlay, i, geoPoint)) {
                for (RouteMessageHandler onDragEnd : this.f1847a.f1853f) {
                    onDragEnd.onDragEnd(mapView, routeOverlay, i, geoPoint);
                }
            }
        }

        public void onDrag(MapView mapView, RouteOverlay routeOverlay, int i, GeoPoint geoPoint) {
            if (m1921a(mapView, routeOverlay, i, geoPoint)) {
                for (RouteMessageHandler onDrag : this.f1847a.f1853f) {
                    onDrag.onDrag(mapView, routeOverlay, i, geoPoint);
                }
            }
        }
    }

    public RouteOverlay(MapActivity mapActivity, Route route) {
        this.mRoute = null;
        this.f1848a = null;
        this.f1850c = null;
        this.f1851d = true;
        this.f1852e = true;
        this.f1849b = new C1037a(this);
        this.f1853f = new ArrayList();
        this.f1854g = null;
        this.f1855h = OnOverview;
        this.f1856i = false;
        this.f1857j = true;
        RouteResource.m541a(mapActivity);
        this.mContext = mapActivity;
        this.mRoute = route;
    }

    public Route getRoute() {
        return this.mRoute;
    }

    public boolean onTrackballEvent(MotionEvent motionEvent, MapView mapView) {
        return onTouchEvent(motionEvent, mapView);
    }

    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        boolean z = false;
        for (an a : this.f1848a) {
            z = a.m783a(motionEvent, mapView);
            if (z) {
                break;
            }
        }
        return z;
    }

    public void setBusLinePaint(Paint paint) {
        if (paint != null) {
            if (!Style.STROKE.equals(paint.getStyle())) {
                paint.setStyle(Style.STROKE);
            }
            RouteResource.f384l = paint;
        }
    }

    public void setFootLinePaint(Paint paint) {
        if (paint != null) {
            if (!Style.STROKE.equals(paint.getStyle())) {
                paint.setStyle(Style.STROKE);
            }
            RouteResource.f383k = paint;
        }
    }

    public void setCarLinePaint(Paint paint) {
        if (paint != null) {
            if (!Style.STROKE.equals(paint.getStyle())) {
                paint.setStyle(Style.STROKE);
            }
            RouteResource.f385m = paint;
        }
    }

    public void draw(Canvas canvas, MapView mapView, boolean z) {
        for (an anVar : this.f1848a) {
            if (anVar instanceof RouteOverlayDraw) {
                anVar.m781a(canvas, mapView, z);
            }
        }
        for (an anVar2 : this.f1848a) {
            if (!(anVar2 instanceof RouteOverlayDraw)) {
                anVar2.m781a(canvas, mapView, z);
            }
        }
    }

    public void enablePopup(boolean z) {
        this.f1851d = z;
        if (!this.f1851d) {
            closePopupWindow();
        }
    }

    public void showRouteButton(boolean z) {
        this.f1857j = z;
    }

    public void enableDrag(boolean z) {
        this.f1852e = z;
    }

    public void addToMap(MapView mapView) {
        this.f1854g = mapView;
        m1926a(this.f1854g);
        if (!this.f1854g.getOverlays().contains(this)) {
            this.f1854g.getOverlays().add(this);
        }
        for (an a : this.f1848a) {
            a.m782a(this.f1854g);
        }
    }

    private void m1926a(MapView mapView) {
        if (!this.f1856i) {
            this.f1848a = new ArrayList();
            this.f1848a.add(new RouteOverlayDraw(this, OnOverview, this.mRoute.mHelper.m880g(OnOverview), ItemizedOverlay.boundCenterBottom(RouteResource.f373a), this.f1849b, true));
            int stepCount = this.mRoute.getStepCount();
            int i = OnOverview;
            while (i < stepCount) {
                if (i <= 0 || i >= stepCount - 1) {
                    this.f1848a.add(new RouteOverlayDraw(this, this.mRoute.getStep(i).getShapes(), this.mRoute.mHelper.m871a(i)));
                } else {
                    Object shapes = this.mRoute.getStep(i).getShapes();
                    GeoPoint geoPoint = this.mRoute.getStep(i + OnDetail).getShapes()[OnOverview];
                    Object obj = new GeoPoint[(shapes.length + OnDetail)];
                    System.arraycopy(shapes, OnOverview, obj, OnOverview, shapes.length);
                    obj[obj.length - 1] = geoPoint;
                    this.f1848a.add(new RouteOverlayDraw(this, obj, this.mRoute.mHelper.m871a(i)));
                }
                View a = this.mRoute.mHelper.m872a(mapView, this.mContext, this.f1849b, this, i);
                if (a != null) {
                    GeoPoint g = this.mRoute.mHelper.m880g(i);
                    int i2 = i;
                    GeoPoint geoPoint2 = g;
                    this.f1848a.add(new bh(this, i2, geoPoint2, a, RouteResource.m543b(this.mContext), new LayoutParams(-2, -2, g, OnOverview, OnOverview, 85)));
                } else if (Route.isDrive(this.mRoute.getMode())) {
                    this.f1848a.add(new RouteOverlayDraw(this, i + OnDetail, this.mRoute.mHelper.m880g(i + OnDetail), ItemizedOverlay.boundCenter(RouteResource.f382j), this.f1849b, false));
                }
                i += OnDetail;
            }
            this.f1848a.add(new RouteOverlayDraw(this, stepCount, this.mRoute.mHelper.m880g(stepCount), ItemizedOverlay.boundCenterBottom(RouteResource.f374b), this.f1849b, true));
            this.f1856i = true;
        }
    }

    public void registerRouteMessage(RouteMessageHandler routeMessageHandler) {
        this.f1853f.add(routeMessageHandler);
    }

    public void unregisterRouteMessage(RouteMessageHandler routeMessageHandler) {
        this.f1853f.remove(routeMessageHandler);
    }

    protected void takeDefaultAction(MapView mapView, int i, int i2) {
        switch (i2) {
            case OnOverview /*0*/:
                closePopupWindow();
                m1927a(mapView, i);
                break;
            case OnDetail /*1*/:
                closePopupWindow();
                m1929b(mapView, i);
                break;
            case OnPrev /*2*/:
                i = this.mRoute.mHelper.m878e(i);
                break;
            case OnNext /*3*/:
                i = this.mRoute.mHelper.m877d(i);
                break;
        }
        showPopupWindow(i);
        this.f1855h = i;
    }

    public void showNextPopUpWindow() {
        if (this.mRoute != null && this.f1855h < this.mRoute.getStepCount()) {
            takeDefaultAction(this.f1854g, this.f1855h, OnNext);
        }
    }

    public void showPrevPopUpWindow() {
        if (this.f1855h > 0) {
            takeDefaultAction(this.f1854g, this.f1855h, OnPrev);
        }
    }

    private void m1927a(MapView mapView, int i) {
        mapView.getController().zoomOut();
    }

    private void m1929b(MapView mapView, int i) {
        mapView.getController().zoomIn();
    }

    public boolean removeFromMap(MapView mapView) {
        boolean remove = mapView.getOverlays().remove(this);
        if (remove) {
            closePopupWindow();
            this.f1854g = null;
            for (an b : this.f1848a) {
                b.m784b(mapView);
            }
        }
        return remove;
    }

    public boolean showPopupWindow(int i) {
        if (!this.f1851d || isStartEndMoved()) {
            return false;
        }
        if (this.f1854g == null) {
            throw new UnsupportedOperationException("routeoverlay must be added to map frist!");
        }
        View infoView = getInfoView(this.f1854g, i);
        if (infoView == null) {
            return false;
        }
        GeoPoint g = this.mRoute.mHelper.m880g(i);
        if (this.f1854g.mRouteCtrl.f453a) {
            m1930b(this.f1854g, g);
        }
        this.f1850c = new am(this.f1854g, infoView, g, this, i);
        this.f1850c.m1941a(this.f1857j);
        return true;
    }

    public void closePopupWindow() {
        if (this.f1850c != null) {
            this.f1850c.m1943c();
        }
        this.f1850c = null;
    }

    protected View getInfoView(MapView mapView, int i) {
        return this.mRoute.mHelper.m874b(mapView, this.mContext, this.f1849b, this, i);
    }

    public boolean isStartEndMoved() {
        return (getStartPos().equals(this.mRoute.getStartPos()) && getEndPos().equals(this.mRoute.getTargetPos())) ? false : true;
    }

    public void restoreOverlay(MapView mapView) {
        removeFromMap(mapView);
        m1923a((int) OnOverview).b = this.mRoute.getStartPos().m468e();
        m1923a(this.mRoute.getStepCount()).b = this.mRoute.getTargetPos().m468e();
        addToMap(mapView);
    }

    public void renewOverlay(MapView mapView) throws AMapException {
        removeFromMap(mapView);
        this.f1856i = false;
        if (isStartEndMoved()) {
            this.mRoute = (Route) Route.calculateRoute(this.mContext, new FromAndTo(getStartPos(), getEndPos(), OnOverview), this.mRoute.getMode()).get(OnOverview);
        }
        addToMap(mapView);
    }

    public GeoPoint getStartPos() {
        return m1923a((int) OnOverview).b;
    }

    public GeoPoint getEndPos() {
        return m1923a(this.mRoute.getStepCount()).b;
    }

    private void m1930b(MapView mapView, GeoPoint geoPoint) {
        Point a = m1922a(mapView, geoPoint);
        if (!m1928a(mapView, a, 30)) {
            a.x -= mapView.getWidth() / OnIconClick;
            mapView.getController().animateTo(mapView.getProjection().fromPixels(a.x, a.y));
        }
    }

    static boolean m1928a(MapView mapView, Point point, int i) {
        if (point == null) {
            return false;
        }
        int width = mapView.getWidth() - i;
        int height = mapView.getHeight() - i;
        if (point.x <= i || point.x >= width || point.y <= i || point.y >= height) {
            return false;
        }
        return true;
    }

    static Point m1922a(MapView mapView, GeoPoint geoPoint) {
        return mapView.getProjection().toPixels(geoPoint, null);
    }

    private RouteOverlayDraw m1923a(int i) {
        return i == 0 ? (RouteOverlayDraw) this.f1848a.get(OnOverview) : (RouteOverlayDraw) this.f1848a.get(this.f1848a.size() - 1);
    }
}
