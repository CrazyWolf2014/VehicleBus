package com.amap.mapapi.core;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import org.xmlpull.v1.XmlPullParser;

public class PoiItem extends OverlayItem {
    public static final Creator<PoiItem> CREATOR;
    public static final String DesSplit = " - ";
    private String f1792a;
    private String f1793b;
    private String f1794c;
    private String f1795d;
    private String f1796e;
    private String f1797f;

    public PoiItem(String str, GeoPoint geoPoint, String str2, String str3) {
        super(geoPoint, str2, str3);
        this.f1796e = XmlPullParser.NO_NAMESPACE;
        this.f1792a = str;
    }

    private PoiItem(Parcel parcel) {
        super(parcel);
        this.f1796e = XmlPullParser.NO_NAMESPACE;
        this.f1792a = parcel.readString();
        this.f1795d = parcel.readString();
        this.f1794c = parcel.readString();
        this.f1793b = parcel.readString();
        this.f1796e = parcel.readString();
    }

    static {
        CREATOR = new PoiItem();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.f1792a);
        parcel.writeString(this.f1795d);
        parcel.writeString(this.f1794c);
        parcel.writeString(this.f1793b);
        parcel.writeString(this.f1796e);
    }

    public String getTypeDes() {
        return this.f1796e;
    }

    public void setTypeDes(String str) {
        this.f1796e = str;
    }

    public String getXmlNode() {
        return this.f1797f;
    }

    public void setXmlNode(String str) {
        this.f1797f = str;
    }

    public String getTel() {
        return this.f1794c;
    }

    public void setTel(String str) {
        this.f1794c = str;
    }

    public String getAdCode() {
        return this.f1795d;
    }

    public void setAdCode(String str) {
        this.f1795d = str;
    }

    public String getPoiId() {
        return this.f1792a;
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        if (this.f1792a == ((PoiItem) obj).f1792a) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.f1792a.hashCode();
    }

    public String toString() {
        return this.mTitle;
    }

    public String getTypeCode() {
        return this.f1793b;
    }

    public void setTypeCode(String str) {
        this.f1793b = str;
    }
}
