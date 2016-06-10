package com.iflytek.ui.p016b;

import android.content.Context;
import com.iflytek.msc.p013f.C0278g;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.iflytek.ui.b.a */
public class C0310a {
    private static C0310a f1165b;
    private HashMap<String, C0313b> f1166a;

    static {
        f1165b = null;
    }

    public C0310a(Context context, String str) {
        this.f1166a = new HashMap();
        this.f1166a = C0310a.m1405b(context, str);
    }

    public static C0310a m1403a() {
        return f1165b;
    }

    public static C0310a m1404a(Context context, String str) {
        if (f1165b == null) {
            f1165b = new C0310a(context, str);
        }
        return f1165b;
    }

    public static HashMap<String, C0313b> m1405b(Context context, String str) {
        HashMap<String, C0313b> hashMap = new HashMap();
        try {
            JSONArray jSONArray = new JSONObject(EncodingUtils.getString(C0278g.m1234a(context, str + File.separator + "msc.cfg"), "utf-8")).getJSONArray("views");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                C0313b c0313b = new C0313b();
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str2 = (String) keys.next();
                    if (str2.equals("tag")) {
                        c0313b.m1411a(jSONObject.getString(str2));
                    } else {
                        c0313b.m1412a(str2, jSONObject.getString(str2));
                    }
                }
                hashMap.put(jSONObject.getString("tag"), c0313b);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public C0313b m1406a(String str) {
        return (C0313b) this.f1166a.get(str);
    }
}
