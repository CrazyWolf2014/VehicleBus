package com.amap.mapapi.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* renamed from: com.amap.mapapi.core.j */
class OverlayItem implements Creator<OverlayItem> {
    OverlayItem() {
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m516a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m517a(i);
    }

    public OverlayItem m516a(Parcel parcel) {
        return new OverlayItem(parcel);
    }

    public OverlayItem[] m517a(int i) {
        return new OverlayItem[i];
    }
}
