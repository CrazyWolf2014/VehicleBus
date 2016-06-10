package com.iflytek.p005a;

import android.content.Context;
import android.text.TextUtils;
import com.cnlaunch.framework.common.Constants;
import com.iflytek.msc.p013f.C0275d;
import com.iflytek.p007c.C0255a;
import com.iflytek.speech.SpeechError;
import com.tencent.mm.sdk.platformtools.Util;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.iflytek.a.i */
public class C0251i {
    private String f958a;
    private String f959b;
    private Vector<C0249f> f960c;
    private Vector<C0249f> f961d;
    private Object f962e;

    public C0251i() {
        this.f958a = "-1";
        this.f959b = Util.CHINA;
        this.f960c = null;
        this.f961d = null;
        this.f962e = new Object();
        this.f960c = new Vector();
        this.f961d = new Vector();
    }

    private JSONObject m1108b() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(Constants.VER, this.f958a);
            jSONObject.put("lang", this.f959b);
            jSONObject.put("ad", m1112a(this.f960c));
            jSONObject.put("err", m1112a(this.f961d));
        } catch (JSONException e) {
        }
        return jSONObject;
    }

    public C0249f m1109a(Context context, SpeechError speechError, boolean z) {
        synchronized (this.f962e) {
            if (!C0255a.m1146a(this.f959b)) {
                C0245a.m1052a(context).m1057a("ad_last_update", 0);
            } else if (this.f961d.size() > 0) {
                for (int i = 0; i < this.f961d.size(); i++) {
                    if (((C0249f) this.f961d.get(i)).m1090a(speechError.getErrorCode())) {
                        C0249f a = ((C0249f) this.f961d.get(i)).m1086a(speechError.getErrorCode(), z);
                        return a;
                    }
                }
            }
            return new C0249f(speechError.getErrorDescription(z), C0255a.m1143a(5), -1);
        }
    }

    public String m1110a() {
        return this.f958a;
    }

    public Vector<C0249f> m1111a(JSONArray jSONArray, boolean z) {
        Vector<C0249f> vector = new Vector();
        int i = 0;
        while (i < jSONArray.length()) {
            try {
                C0249f c0249f = new C0249f(new JSONObject(jSONArray.getString(i)), z);
                if (!z || c0249f.m1095f()) {
                    vector.add(c0249f);
                }
                i++;
            } catch (Exception e) {
            }
        }
        return vector;
    }

    public JSONArray m1112a(Vector<C0249f> vector) {
        JSONArray jSONArray = new JSONArray();
        for (int i = 0; i < vector.size(); i++) {
            jSONArray.put(((C0249f) vector.get(i)).toString());
        }
        return jSONArray;
    }

    public void m1113a(C0249f c0249f) {
        this.f960c.remove(c0249f);
    }

    public void m1114a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.f958a = jSONObject.getString(Constants.VER);
                this.f959b = jSONObject.getString("lang");
                synchronized (this.f962e) {
                    if (jSONObject.has("ad")) {
                        this.f960c = m1111a(jSONObject.getJSONArray("ad"), true);
                    }
                    if (jSONObject.has("err")) {
                        this.f961d = m1111a(jSONObject.getJSONArray("err"), false);
                    }
                }
            } catch (JSONException e) {
            }
        }
    }

    public boolean m1115a(Context context) {
        return (System.currentTimeMillis() - C0245a.m1052a(context).m1059b("ad_last_update", 0)) / 1000 > 86400;
    }

    public C0249f m1116b(Context context) {
        synchronized (this.f962e) {
            if (!C0255a.m1146a(this.f959b)) {
                C0245a.m1052a(context).m1057a("ad_last_update", 0);
            } else if (this.f960c.size() > 0) {
                C0249f c0249f = (C0249f) this.f960c.get(C0275d.m1215a(0, this.f960c.size()));
                return c0249f;
            }
            return new C0249f(C0255a.m1148b(0), null, -1);
        }
    }

    public String toString() {
        return m1108b().toString();
    }
}
