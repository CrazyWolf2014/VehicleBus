package com.iflytek.util.p017a.p018a;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* renamed from: com.iflytek.util.a.a.b */
final class C0323b implements Creator<C0322a> {
    C0323b() {
    }

    public C0322a m1449a(Parcel parcel) {
        C0322a c0322a = new C0322a();
        c0322a.f1198a = parcel.readString();
        c0322a.f1199b = parcel.readString();
        c0322a.f1200c = parcel.readString();
        c0322a.f1201d = parcel.readString();
        c0322a.f1202e = parcel.readString();
        c0322a.f1203f = parcel.readString();
        c0322a.f1204g = parcel.readString();
        return c0322a;
    }

    public C0322a[] m1450a(int i) {
        return new C0322a[i];
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return m1449a(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return m1450a(i);
    }
}
