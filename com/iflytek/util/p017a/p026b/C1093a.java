package com.iflytek.util.p017a.p026b;

import android.content.Context;
import android.net.Uri;
import android.provider.Contacts.People;
import com.iflytek.util.p017a.p019c.C0326a;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;

/* renamed from: com.iflytek.util.a.b.a */
public class C1093a extends C0326a {
    private static final String[] f2103d;
    private static final String[] f2104e;
    private static final String[] f2105f;
    private static final String[] f2106g;
    private static final String[] f2107h;
    private static final String[] f2108i;
    private static final String[] f2109j;

    static {
        f2103d = new String[]{"_id", "name"};
        f2104e = new String[]{"name", "number", "_id"};
        f2105f = new String[]{"person"};
        f2106g = new String[]{"display_name"};
        f2107h = new String[]{"number", SharedPref.TYPE, "name"};
        f2108i = new String[]{"_id", "name", "number", SharedPref.TYPE};
        f2109j = new String[]{"number"};
    }

    public C1093a(Context context) {
        super(context);
        m1458a(context);
    }

    public Uri m2291a() {
        return People.CONTENT_URI;
    }

    protected String[] m2292b() {
        return f2103d;
    }

    protected String m2293c() {
        return "name";
    }
}
