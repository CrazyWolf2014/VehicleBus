package com.iflytek.util.p017a.p026b;

import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.ContactsContract.Contacts;
import com.iflytek.util.p017a.p019c.C0326a;

/* renamed from: com.iflytek.util.a.b.b */
public class C1094b extends C0326a {
    private static final String[] f2110d;
    private static final String[] f2111e;
    private static final String[] f2112f;
    private static final String[] f2113g;
    private static final String[] f2114h;
    private static final String[] f2115i;
    private static final String[] f2116j;

    static {
        f2110d = new String[]{"display_name", "_id"};
        f2111e = new String[]{"display_name", "data1", "contact_id"};
        f2112f = new String[]{"_id", "has_phone_number"};
        f2113g = new String[]{"contact_id"};
        f2114h = new String[]{"display_name"};
        f2115i = new String[]{"data1", "data2", "display_name"};
        f2116j = new String[]{"has_phone_number"};
    }

    public C1094b(Context context) {
        super(context);
        m1458a(context);
    }

    public Uri m2294a() {
        return Contacts.CONTENT_URI;
    }

    protected String[] m2295b() {
        return f2110d;
    }

    protected String m2296c() {
        return Integer.parseInt(VERSION.SDK) >= 8 ? "sort_key" : "display_name";
    }
}
