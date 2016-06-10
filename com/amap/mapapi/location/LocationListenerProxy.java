package com.amap.mapapi.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.autonavi.gelocator.api.LocationManagerProxy;

/* renamed from: com.amap.mapapi.location.a */
public class LocationListenerProxy implements LocationListener {
    private LocationManagerProxy f425a;
    private LocationListener f426b;

    public LocationListenerProxy(LocationManagerProxy locationManagerProxy) {
        this.f426b = null;
        this.f425a = locationManagerProxy;
    }

    public boolean m571a(LocationListener locationListener, long j, float f, String str) {
        this.f426b = locationListener;
        if (!LocationManagerProxy.LBS_PROVIDER.equals(str)) {
            return false;
        }
        this.f425a.requestLocationUpdates(str, j, f, (LocationListener) this);
        return true;
    }

    public boolean m570a(LocationListener locationListener, long j, float f) {
        boolean z = false;
        this.f426b = locationListener;
        for (String str : this.f425a.getProviders(true)) {
            if (LocationManagerProxy.GPS_PROVIDER.equals(str) || LocationManagerProxy.NETWORK_PROVIDER.equals(str)) {
                this.f425a.requestLocationUpdates(str, j, f, (LocationListener) this);
                z = true;
            }
        }
        return z;
    }

    public void m568a() {
        if (this.f425a != null) {
            this.f425a.removeUpdates((LocationListener) this);
            this.f425a.destory();
        }
        this.f425a = null;
        this.f426b = null;
    }

    public void m569a(boolean z) {
        if (z) {
            m568a();
            return;
        }
        if (this.f425a != null) {
            this.f425a.removeUpdates((LocationListener) this);
        }
        this.f426b = null;
    }

    public void onLocationChanged(Location location) {
        if (this.f426b != null) {
            this.f426b.onLocationChanged(location);
        }
    }

    public void onStatusChanged(String str, int i, Bundle bundle) {
        if (this.f426b != null) {
            this.f426b.onStatusChanged(str, i, bundle);
        }
    }

    public void onProviderEnabled(String str) {
        if (this.f426b != null) {
            this.f426b.onProviderEnabled(str);
        }
    }

    public void onProviderDisabled(String str) {
        if (this.f426b != null) {
            this.f426b.onProviderDisabled(str);
        }
    }
}
