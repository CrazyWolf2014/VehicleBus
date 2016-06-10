package com.iflytek.p005a;

import android.content.Context;
import android.text.TextUtils;
import com.iflytek.Version;
import com.iflytek.msc.p013f.C0272a;
import com.iflytek.p006b.C0252a;
import com.iflytek.p006b.C0254c;
import com.iflytek.p007c.C0255a;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.a.e */
public class C0248e {
    private C0252a f945a;

    public C0248e(Context context) {
        this.f945a = null;
        m1082a(context);
    }

    private void m1082a(Context context) {
        this.f945a = C0272a.m1207a(context).m1126b();
        this.f945a.m1121a("lang", C0255a.m1147b());
        this.f945a.m1121a("appid", C0254c.m1131a());
        this.f945a.m1121a("md5", C0254c.m1139b());
        this.f945a.m1121a("msc.ver", Version.getVersion());
        C0254c.m1140b(context, this.f945a);
        this.f945a.m1121a("logtime", XmlPullParser.NO_NAMESPACE + System.currentTimeMillis());
        if (TextUtils.isEmpty(C0255a.f966a)) {
            this.f945a.m1121a("msc.skin", "null");
        } else {
            this.f945a.m1121a("msc.skin", C0255a.f966a);
        }
        this.f945a.m1121a("msc.lat", XmlPullParser.NO_NAMESPACE + C0245a.m1052a(context).m1055a("msc.lat"));
        this.f945a.m1121a("msc.lng", XmlPullParser.NO_NAMESPACE + C0245a.m1052a(context).m1055a("msc.lng"));
    }

    public String m1083a() {
        this.f945a.m1121a("msc.init", XmlPullParser.NO_NAMESPACE + C0247c.f939a);
        return this.f945a.toString();
    }

    public JSONObject m1084a(boolean z) {
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        for (Entry entry : this.f945a.m1128c().entrySet()) {
            try {
                jSONObject2.put((String) entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jSONObject.put("header", (Object) jSONObject2);
        return z ? jSONObject : jSONObject2;
    }

    public void m1085a(String str, String str2) {
        this.f945a.m1121a(str, str2);
    }
}
