package com.iflytek.p005a;

import com.iflytek.speech.SpeechError;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.achartengine.ChartFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.a.f */
public class C0249f {
    private static SimpleDateFormat f946c;
    ArrayList<Integer> f947a;
    boolean f948b;
    private String f949d;
    private String f950e;
    private int f951f;
    private String f952g;
    private String f953h;
    private String f954i;

    static {
        f946c = new SimpleDateFormat("yyyy.MM.dd");
    }

    public C0249f(String str, String str2, int i) {
        this.f949d = null;
        this.f950e = null;
        this.f951f = -1;
        this.f952g = XmlPullParser.NO_NAMESPACE;
        this.f953h = null;
        this.f954i = null;
        this.f947a = null;
        this.f948b = false;
        this.f953h = str;
        this.f954i = str2;
        this.f949d = XmlPullParser.NO_NAMESPACE + i;
    }

    public C0249f(JSONObject jSONObject, boolean z) {
        this.f949d = null;
        this.f950e = null;
        this.f951f = -1;
        this.f952g = XmlPullParser.NO_NAMESPACE;
        this.f953h = null;
        this.f954i = null;
        this.f947a = null;
        this.f948b = false;
        try {
            this.f948b = z;
            this.f949d = jSONObject.getString(LocaleUtil.INDONESIAN);
            this.f953h = jSONObject.getString("url");
            if (z) {
                this.f950e = jSONObject.getString("expire");
                this.f951f = jSONObject.getInt("valid");
                this.f952g = jSONObject.getString("pkg");
                return;
            }
            this.f954i = jSONObject.getString(ChartFactory.TITLE);
            this.f947a = m1088a(jSONObject.getString("code"));
        } catch (JSONException e) {
        }
    }

    public C0249f m1086a(int i, boolean z) {
        if (!z) {
            return this;
        }
        C0249f c0249f = new C0249f(this.f953h, this.f954i, -1);
        c0249f.f949d = this.f949d;
        c0249f.f953h = SpeechError.appendErrorCodeDes(this.f953h, i);
        return c0249f;
    }

    public String m1087a(ArrayList<Integer> arrayList) {
        String str = XmlPullParser.NO_NAMESPACE;
        int i = 0;
        while (i < arrayList.size()) {
            if (i != 0) {
                str = str + ",";
            }
            String str2 = str + String.valueOf(arrayList.get(i));
            i++;
            str = str2;
        }
        return str;
    }

    public ArrayList<Integer> m1088a(String str) {
        ArrayList<Integer> arrayList = new ArrayList();
        try {
            String[] split = str.trim().split(",");
            for (String trim : split) {
                arrayList.add(Integer.valueOf(Integer.parseInt(trim.trim())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public boolean m1089a() {
        return !this.f949d.equals("-1") && this.f951f >= 0;
    }

    public boolean m1090a(int i) {
        if (this.f947a == null) {
            return false;
        }
        for (int i2 = 0; i2 < this.f947a.size(); i2++) {
            if (((Integer) this.f947a.get(i2)).equals(Integer.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

    public String m1091b() {
        return this.f954i;
    }

    public String m1092c() {
        return this.f953h;
    }

    public String m1093d() {
        return this.f949d;
    }

    public long m1094e() {
        Date date = new Date(System.currentTimeMillis());
        try {
            date = f946c.parse(this.f950e);
        } catch (ParseException e) {
        }
        return date.getTime();
    }

    public boolean m1095f() {
        return (this.f948b && this.f951f != -1) ? m1094e() >= System.currentTimeMillis() && this.f951f != 0 : true;
    }

    public void m1096g() {
        if (this.f951f != -1) {
            this.f951f--;
        }
    }

    public String m1097h() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(LocaleUtil.INDONESIAN, this.f949d);
            jSONObject.put("url", this.f953h);
            if (this.f948b) {
                jSONObject.put("expire", this.f950e);
                jSONObject.put("valid", String.valueOf(this.f951f));
                jSONObject.put("pkg", this.f952g);
            } else {
                jSONObject.put(ChartFactory.TITLE, this.f954i);
                jSONObject.put("code", m1087a(this.f947a));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public String toString() {
        return m1097h();
    }
}
