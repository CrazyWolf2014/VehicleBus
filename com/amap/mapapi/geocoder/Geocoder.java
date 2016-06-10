package com.amap.mapapi.geocoder;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.ClientInfoUtil;
import com.amap.mapapi.core.CoreUtil;
import com.amap.mapapi.core.GeoPoint.C0086b;
import com.amap.mapapi.core.LocTansServerHandler;
import com.amap.mapapi.core.ReverseGeocodingHandler;
import com.amap.mapapi.core.ReverseGeocodingParam;
import java.util.ArrayList;
import java.util.List;

public final class Geocoder {
    public static final String Cross = "Cross";
    public static final String POI = "POI";
    public static final String Street_Road = "StreetAndRoad";
    private String f401a;
    private Context f402b;

    public Geocoder(Activity activity) {
        ClientInfoUtil.m471a(activity);
        m556a(activity, CoreUtil.m485a((Context) activity));
    }

    public Geocoder(Context context, String str) {
        ClientInfoUtil.m471a(context);
        m556a(context, CoreUtil.m485a(context));
    }

    private void m556a(Context context, String str) {
        this.f402b = context;
        this.f401a = str;
    }

    public List<Address> getFromLocation(double d, double d2, int i) throws AMapException {
        return m554a(d, d2, i, false);
    }

    public List<Address> getFromRawGpsLocation(double d, double d2, int i) throws AMapException {
        try {
            double d3;
            double d4;
            C0086b c0086b = (C0086b) new LocTansServerHandler(new C0086b(d2, d), CoreUtil.m491b(this.f402b), this.f401a, null).m531j();
            if (c0086b != null) {
                d3 = c0086b.f302a;
                d4 = c0086b.f303b;
            } else {
                d3 = d2;
                d4 = d;
            }
            if (Double.valueOf(0.0d).doubleValue() == d3 && Double.valueOf(0.0d).doubleValue() == d4) {
                return null;
            }
            return m554a(d4, d3, i, false);
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    private List<Address> m554a(double d, double d2, int i, boolean z) throws AMapException {
        if (CoreUtil.f346a) {
            if (d < CoreUtil.m481a(1000000) || d > CoreUtil.m481a(65000000)) {
                throw new AMapException("\u65e0\u6548\u7684\u53c2\u6570 - IllegalArgumentException latitude == " + d);
            } else if (d2 < CoreUtil.m481a(50000000) || d2 > CoreUtil.m481a(145000000)) {
                throw new AMapException("\u65e0\u6548\u7684\u53c2\u6570 - IllegalArgumentException longitude == " + d2);
            }
        }
        if (i <= 0) {
            return new ArrayList();
        }
        return (List) new ReverseGeocodingHandler(new ReverseGeocodingParam(d2, d, i, z), CoreUtil.m491b(this.f402b), this.f401a, null).m531j();
    }

    public List<Address> getFromLocationName(String str, int i, double d, double d2, double d3, double d4) throws AMapException {
        if (str == null) {
            throw new IllegalArgumentException("locationName == null");
        }
        if (CoreUtil.f346a) {
            if (d < CoreUtil.m481a(1000000) || d > CoreUtil.m481a(65000000)) {
                throw new AMapException("\u65e0\u6548\u7684\u53c2\u6570 - IllegalArgumentException lowerLeftLatitude == " + d);
            } else if (d2 < CoreUtil.m481a(50000000) || d2 > CoreUtil.m481a(145000000)) {
                throw new AMapException("\u65e0\u6548\u7684\u53c2\u6570 - IllegalArgumentException lowerLeftLongitude == " + d2);
            } else if (d3 < CoreUtil.m481a(1000000) || d3 > CoreUtil.m481a(65000000)) {
                throw new AMapException("\u65e0\u6548\u7684\u53c2\u6570 - IllegalArgumentException upperRightLatitude == " + d3);
            } else if (d4 < CoreUtil.m481a(50000000) || d4 > CoreUtil.m481a(145000000)) {
                throw new AMapException("\u65e0\u6548\u7684\u53c2\u6570 - IllegalArgumentException upperRightLongitude == " + d4);
            }
        }
        if (i <= 0) {
            return new ArrayList();
        }
        List<Address> list = (List) new GeocodingHandler(new GeocodingParam(str, 15), CoreUtil.m491b(this.f402b), this.f401a, null).m531j();
        if (CoreUtil.f346a) {
            return m555a(list, d, d2, d3, d4, i);
        }
        return list;
    }

    private List<Address> m555a(List<Address> list, double d, double d2, double d3, double d4, int i) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (Address address : list) {
            double longitude = address.getLongitude();
            double latitude = address.getLatitude();
            if (longitude <= d4 && longitude >= d2 && latitude <= d3 && latitude >= d && arrayList.size() < i) {
                arrayList.add(address);
            }
        }
        return arrayList;
    }

    public List<Address> getFromLocationName(String str, int i) throws AMapException {
        return getFromLocationName(str, i, CoreUtil.m481a(1000000), CoreUtil.m481a(50000000), CoreUtil.m481a(65000000), CoreUtil.m481a(145000000));
    }
}
