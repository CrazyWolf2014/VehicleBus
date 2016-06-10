package com.autonavi.gelocator.api;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import java.util.ArrayList;
import java.util.List;

public class LocationManagerProxy {
    public static final String LBS_PROVIDER = "lbs";
    private static LocationManagerProxy f772a;
    private static LbsManager f773b;
    private Context f774c;
    private LocationManager f775d;

    private LocationManagerProxy(Context context, String str) {
        this.f775d = null;
        this.f774c = context;
        f773b = LbsManager.getInstance(context, str);
    }

    public static LocationManagerProxy getInstance(Context context) {
        if (f772a == null) {
            f772a = new LocationManagerProxy(context, null);
        }
        return f772a;
    }

    public static LocationManagerProxy getInstance(Context context, String str) {
        if (f772a == null) {
            f772a = new LocationManagerProxy(context, str);
        }
        return f772a;
    }

    public static String getVersion() {
        return "1.0.201202091702";
    }

    public void destroy() {
        f773b.destroy();
        f772a = null;
    }

    public List getAllProviders() {
        List arrayList = new ArrayList();
        arrayList.add(LBS_PROVIDER);
        return arrayList;
    }

    public Location getCurrentLocation(String str) {
        return str.equals(LBS_PROVIDER) ? f773b.getNowLocation(LBS_PROVIDER) : null;
    }

    public Location getLastUpdatedLocation(String str) {
        return str.equals(LBS_PROVIDER) ? f773b.getLastKnownLocation() : null;
    }

    public LocationManager getSystemLocationManager() {
        if (this.f775d == null) {
            this.f775d = (LocationManager) this.f774c.getSystemService(com.amap.mapapi.location.LocationManagerProxy.KEY_LOCATION_CHANGED);
        }
        return this.f775d;
    }

    public void removeUpdates(LocationListener locationListener) {
        f773b.removeLbsUpdates(locationListener);
    }

    public void requestLocationUpdates(String str, long j, float f, LocationListener locationListener) {
        if (str.equals(LBS_PROVIDER)) {
            f773b.requestLbsLocationUpdates(j, f, locationListener, LBS_PROVIDER);
        }
    }
}
