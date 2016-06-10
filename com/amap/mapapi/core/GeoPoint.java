package com.amap.mapapi.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* renamed from: com.amap.mapapi.core.e */
class GeoPoint implements Creator<GeoPoint> {
    GeoPoint() {
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m497a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m498a(i);
    }

    public GeoPoint m497a(Parcel parcel) {
        return new GeoPoint(null);
    }

    public GeoPoint[] m498a(int i) {
        return new GeoPoint[i];
    }
}
