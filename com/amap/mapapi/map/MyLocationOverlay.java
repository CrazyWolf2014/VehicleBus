package com.amap.mapapi.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.amap.mapapi.core.ConfigableConst;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.location.LocationListenerProxy;
import com.amap.mapapi.location.LocationManagerProxy;
import com.amap.mapapi.map.Overlay.Snappable;
import com.googlecode.leptonica.android.Skew;
import java.util.Iterator;
import java.util.LinkedList;
import org.xbill.DNS.KEYRecord;

public class MyLocationOverlay extends Overlay implements SensorEventListener, SensorListener, LocationListener, Snappable {
    private ag f1834a;
    private CompassServer f1835b;
    private boolean f1836c;
    private boolean f1837d;
    private float f1838e;
    private CompassOverlay f1839f;
    private FrameAnim f1840g;
    private final LinkedList<Runnable> f1841h;
    private LocationManagerProxy f1842i;
    private LocationListenerProxy f1843j;
    private Criteria f1844k;
    private Location f1845l;
    private Context f1846m;

    public MyLocationOverlay(Context context, MapView mapView) {
        this.f1836c = false;
        this.f1837d = false;
        this.f1838e = Float.NaN;
        this.f1841h = new LinkedList();
        if (mapView == null) {
            throw new RuntimeException("MapView \u4e0d\u80fd\u4e3a\u7a7a\uff01");
        }
        this.f1846m = context;
        this.f1834a = mapView.m642a();
        this.f1835b = (CompassServer) this.f1834a.f597e.m732a(2);
        this.f1838e = 0.0f;
        this.f1839f = new CompassOverlay(this.f1834a);
        this.f1840g = new FrameAnim(-1, 1000, this.f1834a, new Bitmap[]{ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.eloc1.ordinal()), ConfigableConst.f337g.m533a(ConfigableConst.ConfigableConst.eloc1.ordinal())});
        this.f1842i = LocationManagerProxy.getInstance(context);
        if (this.f1843j != null) {
            disableMyLocation();
        }
        m1918c();
        disableCompass();
    }

    public float getOrientation() {
        return this.f1838e;
    }

    public void disableCompass() {
        this.f1835b.m1997b();
        this.f1837d = false;
    }

    public boolean enableCompass() {
        if (!this.f1835b.m1996a(this)) {
            return false;
        }
        this.f1837d = true;
        return true;
    }

    public boolean isCompassEnabled() {
        return this.f1837d;
    }

    public boolean isMyLocationEnabled() {
        return this.f1836c;
    }

    public void disableMyLocation() {
        if (this.f1843j != null) {
            this.f1843j.m568a();
        }
        this.f1843j = null;
        this.f1836c = false;
        this.f1842i = null;
    }

    void m1920a() {
    }

    public boolean enableMyLocation() {
        boolean z = false;
        if (this.f1843j == null) {
            if (this.f1842i == null) {
                this.f1842i = LocationManagerProxy.getInstance(this.f1846m);
            }
            this.f1843j = new LocationListenerProxy(this.f1842i);
            String b = m1917b();
            if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(b)) {
                z = this.f1843j.m571a(this, 10000, Skew.SWEEP_DELTA, b);
            } else {
                z = this.f1843j.m570a(this, 10000, Skew.SWEEP_DELTA);
            }
            if (z) {
                this.f1845l = this.f1842i.getLastKnownLocation(b);
                this.f1836c = true;
            }
        }
        return z;
    }

    private String m1917b() {
        String bestProvider = this.f1842i.getBestProvider(this.f1844k, true);
        if (bestProvider == null) {
            return this.f1842i.getBestProvider(null, true);
        }
        return bestProvider;
    }

    private void m1918c() {
        this.f1844k = new Criteria();
        this.f1844k.setAccuracy(2);
        this.f1844k.setAltitudeRequired(false);
        this.f1844k.setBearingRequired(false);
        this.f1844k.setPowerRequirement(2);
    }

    public Location getLastFix() {
        return this.f1845l;
    }

    public GeoPoint getMyLocation() {
        return m1916a(getLastFix());
    }

    private GeoPoint m1916a(Location location) {
        if (location != null) {
            return new GeoPoint(CoreUtil.m484a(location.getLatitude()), CoreUtil.m484a(location.getLongitude()));
        }
        return null;
    }

    private Rect m1919d() {
        GeoPoint myLocation = getMyLocation();
        if (myLocation == null) {
            return null;
        }
        int h = this.f1840g.m2002h() / 2;
        int i = this.f1840g.m2003i() / 2;
        Point toPixels = this.f1834a.f593a.toPixels(myLocation, null);
        return new Rect(toPixels.x - h, toPixels.y - i, h + toPixels.x, toPixels.y + i);
    }

    protected boolean dispatchTap() {
        return false;
    }

    public boolean onTap(GeoPoint geoPoint, MapView mapView) {
        if (!this.f1836c) {
            return false;
        }
        Rect d = m1919d();
        if (d == null) {
            return false;
        }
        Point toPixels = this.f1834a.f593a.toPixels(geoPoint, null);
        if (d.contains(toPixels.x, toPixels.y)) {
            return dispatchTap();
        }
        return false;
    }

    public boolean runOnFirstFix(Runnable runnable) {
        if (this.f1845l == null || this.f1843j == null) {
            this.f1841h.addLast(runnable);
            return false;
        }
        new Thread(runnable).start();
        return true;
    }

    public boolean onSnapToItem(int i, int i2, Point point, MapView mapView) {
        GeoPoint myLocation = getMyLocation();
        if (myLocation == null) {
            return false;
        }
        Point toPixels = mapView.getProjection().toPixels(myLocation, null);
        point.x = toPixels.x;
        point.y = toPixels.y;
        double d = (double) (i - toPixels.x);
        double d2 = (double) (i2 - toPixels.y);
        if ((d * d) + (d2 * d2) < 64.0d) {
            return true;
        }
        return false;
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            this.f1845l = location;
            if (this.f1834a.f596d != null) {
                this.f1834a.f596d.m721d();
            }
            if (this.f1841h != null && this.f1841h.size() > 0) {
                Iterator it = this.f1841h.iterator();
                while (it.hasNext()) {
                    Runnable runnable = (Runnable) it.next();
                    if (runnable != null) {
                        new Thread(runnable).start();
                    }
                }
                this.f1841h.clear();
            }
        }
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
    }

    public void onProviderEnabled(String str) {
        this.f1836c = true;
    }

    public void onProviderDisabled(String str) {
        this.f1836c = false;
    }

    public boolean draw(Canvas canvas, MapView mapView, boolean z, long j) {
        if (!z) {
            if (this.f1836c) {
                Location lastFix = getLastFix();
                if (lastFix != null) {
                    drawMyLocation(canvas, this.f1834a.f594b.m755g(), lastFix, m1916a(lastFix), j);
                }
            }
            if (this.f1837d) {
                drawCompass(canvas, this.f1838e);
            }
        }
        return false;
    }

    protected void drawMyLocation(Canvas canvas, MapView mapView, Location location, GeoPoint geoPoint, long j) {
        Point toPixels = this.f1834a.f593a.toPixels(geoPoint, null);
        float f = 500.0f;
        Paint paint = new Paint();
        paint.setColor(-16776961);
        paint.setAlpha(40);
        ah a = mapView.m648b().m1900a();
        if (!location.equals(com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER) && location.hasAccuracy() && location.getAccuracy() > 0.0f) {
            f = a.f615m ? ah.f599j * location.getAccuracy() : bk.f1885h * location.getAccuracy();
        }
        canvas.drawCircle((float) toPixels.x, (float) toPixels.y, (float) ((int) mapView.getProjection().metersToEquatorPixels(f)), paint);
        paint.setAlpha(KEYRecord.PROTOCOL_ANY);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle((float) toPixels.x, (float) toPixels.y, (float) ((int) mapView.getProjection().metersToEquatorPixels(f)), paint);
        this.f1840g.m2000a(canvas, toPixels.x, toPixels.y);
    }

    protected void drawCompass(Canvas canvas, float f) {
        this.f1839f.m2646a(f);
        this.f1839f.draw(canvas, this.f1834a.f594b.m755g(), false, 0);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        this.f1838e = sensorEvent.values[0];
        if (this.f1834a.f596d != null) {
            this.f1834a.f596d.m704a(this.f1839f.m2543c().left, this.f1839f.m2543c().top, this.f1839f.b.getWidth() + this.f1839f.m2645a().x, this.f1839f.b.getHeight() + this.f1839f.m2645a().y);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onAccuracyChanged(int i, int i2) {
    }

    public void onSensorChanged(int i, float[] fArr) {
        this.f1838e = fArr[0];
        if (this.f1834a.f596d != null) {
            this.f1834a.f596d.m704a(this.f1839f.m2543c().left, this.f1839f.m2543c().top, this.f1839f.b.getWidth() + this.f1839f.m2645a().x, this.f1839f.b.getHeight() + this.f1839f.m2645a().y);
        }
    }
}
