package com.autonavi.gelocator.api;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.amap.mapapi.location.LocationManagerProxy;

/* renamed from: com.autonavi.gelocator.api.e */
final class C0102e implements LocationListener {
    private /* synthetic */ LbsManager f807a;

    C0102e(LbsManager lbsManager) {
        this.f807a = lbsManager;
    }

    public final void onLocationChanged(Location location) {
        if (location.getProvider().equals(LocationManagerProxy.GPS_PROVIDER)) {
            this.f807a.f767q.m915a();
            this.f807a.f767q.f818a = location.getLatitude();
            this.f807a.f767q.f819b = location.getLongitude();
            this.f807a.f767q.f820c = location.getAccuracy();
            this.f807a.f767q;
        }
    }

    public final void onProviderDisabled(String str) {
        if (str.equals(LocationManagerProxy.GPS_PROVIDER)) {
            this.f807a.f767q.m915a();
        }
    }

    public final void onProviderEnabled(String str) {
    }

    public final void onStatusChanged(String str, int i, Bundle bundle) {
    }
}
