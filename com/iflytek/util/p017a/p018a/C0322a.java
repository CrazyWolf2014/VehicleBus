package com.iflytek.util.p017a.p018a;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/* renamed from: com.iflytek.util.a.a.a */
public class C0322a implements Parcelable {
    public static final Creator<C0322a> CREATOR;
    private String f1198a;
    private String f1199b;
    private String f1200c;
    private String f1201d;
    private String f1202e;
    private String f1203f;
    private String f1204g;

    static {
        CREATOR = new C0323b();
    }

    public C0322a() {
        this.f1198a = null;
        this.f1199b = null;
        this.f1200c = null;
        this.f1201d = null;
        this.f1202e = null;
        this.f1203f = null;
        this.f1204g = null;
    }

    public C0322a(String str, String str2, String str3, String str4, String str5, String str6) {
        this.f1198a = null;
        this.f1199b = null;
        this.f1200c = null;
        this.f1201d = null;
        this.f1202e = null;
        this.f1203f = null;
        this.f1204g = null;
        this.f1198a = str;
        this.f1199b = str2;
        this.f1200c = str3;
        this.f1201d = str4;
        this.f1202e = str5;
        this.f1204g = str6;
    }

    public String m1446a() {
        return this.f1198a;
    }

    public String m1447b() {
        return this.f1199b;
    }

    public String m1448c() {
        return this.f1201d;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.f1198a);
        parcel.writeString(this.f1199b);
        parcel.writeString(this.f1200c);
        parcel.writeString(this.f1201d);
        parcel.writeString(this.f1202e);
        parcel.writeString(this.f1203f);
        parcel.writeString(this.f1204g);
    }
}
