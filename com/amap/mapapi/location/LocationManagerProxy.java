package com.amap.mapapi.location;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.CoreUtil;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class LocationManagerProxy {
    public static final String GPS_PROVIDER = "gps";
    public static final String KEY_LOCATION_CHANGED = "location";
    public static final String KEY_PROVIDER_ENABLED = "providerEnabled";
    public static final String KEY_PROXIMITY_ENTERING = "entering";
    public static final String KEY_STATUS_CHANGED = "status";
    public static final String NETWORK_PROVIDER = "network";
    private static LocationManagerProxy f406b;
    C0087a f407a;
    private Hashtable<String, LocationProviderProxy> f408c;
    private com.autonavi.gelocator.api.LocationManagerProxy f409d;
    private LocationManager f410e;
    private List<PendingIntent> f411f;
    private Context f412g;
    private LocationListenerProxy f413h;
    private String f414i;
    private double f415j;
    private double f416k;
    private boolean f417l;
    private long f418m;
    private long f419n;
    private double f420o;
    private LocationListenerProxy f421p;
    private List<PendingIntent> f422q;

    /* renamed from: com.amap.mapapi.location.LocationManagerProxy.a */
    class C0087a implements LocationListener {
        final /* synthetic */ LocationManagerProxy f405a;

        C0087a(LocationManagerProxy locationManagerProxy) {
            this.f405a = locationManagerProxy;
        }

        public void onLocationChanged(Location location) {
            Log.d("locationlistener", location.getLatitude() + " \uff1a lng " + location.getLongitude());
            if (this.f405a.f411f.size() > 0) {
                for (PendingIntent pendingIntent : this.f405a.f411f) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(LocationManagerProxy.KEY_LOCATION_CHANGED, location);
                    intent.putExtras(bundle);
                    try {
                        pendingIntent.send(this.f405a.f412g, 0, intent);
                    } catch (CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (this.f405a.f417l && this.f405a.f422q.size() > 0) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double abs = Math.abs(((latitude - this.f405a.f415j) * (latitude - this.f405a.f415j)) + ((longitude - this.f405a.f416k) * (longitude - this.f405a.f416k)));
                long currentTimeMillis = System.currentTimeMillis();
                for (PendingIntent pendingIntent2 : this.f405a.f422q) {
                    if (currentTimeMillis > this.f405a.f419n) {
                        this.f405a.removeProximityAlert(pendingIntent2);
                    } else if (Math.abs(abs - (this.f405a.f420o * this.f405a.f420o)) < 0.5d) {
                        Intent intent2 = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putParcelable(LocationManagerProxy.KEY_LOCATION_CHANGED, location);
                        intent2.putExtras(bundle2);
                        try {
                            pendingIntent2.send(this.f405a.f412g, 0, intent2);
                        } catch (CanceledException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onProviderDisabled(String str) {
        }
    }

    public static synchronized LocationManagerProxy getInstance(Activity activity) {
        LocationManagerProxy locationManagerProxy;
        synchronized (LocationManagerProxy.class) {
            if (f406b == null) {
                f406b = new LocationManagerProxy(activity);
            } else {
                f406b.destory();
                f406b = new LocationManagerProxy(activity);
            }
            locationManagerProxy = f406b;
        }
        return locationManagerProxy;
    }

    public static synchronized LocationManagerProxy getInstance(Context context) {
        LocationManagerProxy locationManagerProxy;
        synchronized (LocationManagerProxy.class) {
            if (f406b == null) {
                f406b = new LocationManagerProxy(context);
            } else {
                f406b.destory();
                f406b = new LocationManagerProxy(context);
            }
            locationManagerProxy = f406b;
        }
        return locationManagerProxy;
    }

    private LocationManagerProxy(Activity activity) {
        this.f408c = new Hashtable();
        this.f411f = new ArrayList();
        this.f417l = false;
        this.f418m = 0;
        this.f419n = 0;
        this.f420o = 0.0d;
        this.f422q = new ArrayList();
        ClientInfoUtil.m471a(activity);
        m558a(activity, CoreUtil.m485a((Context) activity));
    }

    private LocationManagerProxy(Context context) {
        this.f408c = new Hashtable();
        this.f411f = new ArrayList();
        this.f417l = false;
        this.f418m = 0;
        this.f419n = 0;
        this.f420o = 0.0d;
        this.f422q = new ArrayList();
        m558a(context, CoreUtil.m485a(context));
    }

    private void m558a(Context context, String str) {
        this.f412g = context;
        this.f409d = com.autonavi.gelocator.api.LocationManagerProxy.getInstance(context, "401FFB6E52385325E41206A6AFF7A316");
        this.f410e = this.f409d.getSystemLocationManager();
        this.f407a = new C0087a(this);
    }

    public void addProximityAlert(double d, double d2, float f, long j, PendingIntent pendingIntent) {
        if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(this.f414i)) {
            if (this.f421p == null) {
                this.f421p = new LocationListenerProxy(this);
                this.f421p.m571a(this.f407a, 10000, f, this.f414i);
            }
            this.f417l = true;
            this.f415j = d;
            this.f416k = d2;
            this.f420o = (double) f;
            if (j != -1) {
                this.f418m = System.currentTimeMillis();
                this.f419n = this.f418m + j;
            }
            this.f422q.add(pendingIntent);
            return;
        }
        this.f410e.addProximityAlert(d, d2, f, j, pendingIntent);
    }

    public void removeProximityAlert(PendingIntent pendingIntent) {
        if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(this.f414i)) {
            if (this.f421p != null) {
                this.f422q.remove(pendingIntent);
                this.f421p.m569a(false);
            }
            this.f421p = null;
            this.f417l = false;
            this.f418m = 0;
            this.f419n = 0;
            this.f420o = 0.0d;
            return;
        }
        this.f410e.removeProximityAlert(pendingIntent);
    }

    public Location getCurrentLocation(String str) {
        this.f414i = str;
        if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(str)) {
            return this.f409d.getCurrentLocation(str);
        }
        return null;
    }

    public void requestLocationUpdates(String str, long j, float f, LocationListener locationListener) {
        this.f414i = str;
        if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(str)) {
            this.f409d.requestLocationUpdates(str, j, f, locationListener);
        } else {
            this.f410e.requestLocationUpdates(str, j, f, locationListener);
        }
    }

    public void removeUpdates(LocationListener locationListener) {
        if (locationListener != null) {
            if (this.f409d != null) {
                this.f409d.removeUpdates(locationListener);
            }
            this.f410e.removeUpdates(locationListener);
        }
    }

    public void requestLocationUpdates(String str, long j, float f, PendingIntent pendingIntent) {
        this.f414i = str;
        if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(str)) {
            if (this.f413h == null) {
                this.f413h = new LocationListenerProxy(this);
                this.f413h.m571a(this.f407a, j, f, str);
            }
            this.f411f.add(pendingIntent);
            return;
        }
        this.f410e.requestLocationUpdates(str, j, f, pendingIntent);
    }

    public void removeUpdates(PendingIntent pendingIntent) {
        if (this.f413h != null) {
            this.f411f.remove(pendingIntent);
            this.f413h.m569a(false);
        }
        this.f413h = null;
        this.f410e.removeUpdates(pendingIntent);
    }

    public GpsStatus getGpsStatus(GpsStatus gpsStatus) {
        return this.f410e.getGpsStatus(gpsStatus);
    }

    public void removeGpsStatusListener(Listener listener) {
        this.f410e.removeGpsStatusListener(listener);
    }

    public boolean addGpsStatusListener(Listener listener) {
        return this.f410e.addGpsStatusListener(listener);
    }

    public void addTestProvider(String str, boolean z, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, boolean z7, int i, int i2) {
        this.f410e.addTestProvider(str, z, z2, z3, z4, z5, z6, z7, i, i2);
    }

    public void clearTestProviderEnabled(String str) {
        this.f410e.clearTestProviderEnabled(str);
    }

    public void clearTestProviderLocation(String str) {
        this.f410e.clearTestProviderLocation(str);
    }

    public void clearTestProviderStatus(String str) {
        this.f410e.clearTestProviderStatus(str);
    }

    public List<String> getAllProviders() {
        List<String> allProviders = this.f409d.getAllProviders();
        if (allProviders == null || !allProviders.contains(com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER)) {
            allProviders = new ArrayList();
            allProviders.add(com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER);
            allProviders.addAll(this.f410e.getAllProviders());
            return allProviders;
        }
        allProviders.addAll(this.f410e.getAllProviders());
        return allProviders;
    }

    public List<String> getProviders(boolean z) {
        List<String> providers = this.f410e.getProviders(z);
        if (isProviderEnabled(com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER)) {
            if (providers == null) {
                providers = new ArrayList();
            }
            providers.add(com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER);
        }
        return providers;
    }

    public List<String> getProviders(Criteria criteria, boolean z) {
        List<String> providers = this.f410e.getProviders(criteria, z);
        if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(getBestProvider(criteria, z))) {
            providers.add(com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER);
        }
        return providers;
    }

    public String getBestProvider(Criteria criteria, boolean z) {
        String str = com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER;
        if (!getProvider(com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER).meetsCriteria(criteria)) {
            str = this.f410e.getBestProvider(criteria, z);
        }
        if (!z || CoreUtil.m496c(this.f412g)) {
            return str;
        }
        return this.f410e.getBestProvider(criteria, z);
    }

    public LocationProviderProxy getProvider(String str) {
        if (str == null) {
            throw new IllegalArgumentException("name\u4e0d\u80fd\u4e3a\u7a7a\uff01");
        } else if (this.f408c.containsKey(str)) {
            return (LocationProviderProxy) this.f408c.get(str);
        } else {
            LocationProviderProxy a = LocationProviderProxy.m567a(this.f410e, str);
            this.f408c.put(str, a);
            return a;
        }
    }

    public boolean isProviderEnabled(String str) {
        if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(str)) {
            return CoreUtil.m496c(this.f412g);
        }
        return this.f410e.isProviderEnabled(str);
    }

    public Location getLastKnownLocation(String str) {
        this.f414i = str;
        if (com.autonavi.gelocator.api.LocationManagerProxy.LBS_PROVIDER.equals(str)) {
            return this.f409d.getLastUpdatedLocation(str);
        }
        return this.f410e.getLastKnownLocation(str);
    }

    public void destory() {
        if (this.f409d != null) {
            this.f409d.destroy();
        }
        this.f409d = null;
        f406b = null;
    }
}
