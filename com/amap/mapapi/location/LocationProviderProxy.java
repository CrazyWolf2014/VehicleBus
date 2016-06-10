package com.amap.mapapi.location;

import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;

public class LocationProviderProxy {
    public static final int AVAILABLE = 2;
    public static final String MapABCNetwork = "lbs";
    public static final int OUT_OF_SERVICE = 0;
    public static final int TEMPORARILY_UNAVAILABLE = 1;
    private LocationManager f423a;
    private String f424b;

    protected LocationProviderProxy(LocationManager locationManager, String str) {
        this.f423a = locationManager;
        this.f424b = str;
    }

    static LocationProviderProxy m567a(LocationManager locationManager, String str) {
        return new LocationProviderProxy(locationManager, str);
    }

    private LocationProvider m566a() {
        return this.f423a.getProvider(this.f424b);
    }

    public int getAccuracy() {
        if (MapABCNetwork.equals(this.f424b)) {
            return AVAILABLE;
        }
        return m566a().getAccuracy();
    }

    public String getName() {
        if (MapABCNetwork.equals(this.f424b)) {
            return MapABCNetwork;
        }
        return m566a().getName();
    }

    public int getPowerRequirement() {
        if (MapABCNetwork.equals(this.f424b)) {
            return AVAILABLE;
        }
        return m566a().getPowerRequirement();
    }

    public boolean hasMonetaryCost() {
        if (MapABCNetwork.equals(this.f424b)) {
            return false;
        }
        return m566a().hasMonetaryCost();
    }

    public boolean meetsCriteria(Criteria criteria) {
        if (!MapABCNetwork.equals(this.f424b)) {
            return m566a().meetsCriteria(criteria);
        }
        if (criteria == null) {
            return true;
        }
        if (criteria.isAltitudeRequired() || criteria.isBearingRequired() || criteria.isSpeedRequired() || criteria.getAccuracy() == TEMPORARILY_UNAVAILABLE) {
            return false;
        }
        return true;
    }

    public boolean requiresCell() {
        if (MapABCNetwork.equals(this.f424b)) {
            return true;
        }
        return m566a().requiresCell();
    }

    public boolean requiresNetwork() {
        if (MapABCNetwork.equals(this.f424b)) {
            return true;
        }
        return m566a().requiresNetwork();
    }

    public boolean requiresSatellite() {
        if (MapABCNetwork.equals(this.f424b)) {
            return false;
        }
        return m566a().requiresNetwork();
    }

    public boolean supportsAltitude() {
        if (MapABCNetwork.equals(this.f424b)) {
            return false;
        }
        return m566a().supportsAltitude();
    }

    public boolean supportsBearing() {
        if (MapABCNetwork.equals(this.f424b)) {
            return false;
        }
        return m566a().supportsBearing();
    }

    public boolean supportsSpeed() {
        if (MapABCNetwork.equals(this.f424b)) {
            return false;
        }
        return m566a().supportsSpeed();
    }
}
