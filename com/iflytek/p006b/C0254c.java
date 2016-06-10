package com.iflytek.p006b;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.Setting;
import com.iflytek.Setting.LOG_LEVEL;
import com.iflytek.Version;
import com.iflytek.msc.p008a.C0262c;
import com.iflytek.msc.p009b.C0266a;
import com.iflytek.msc.p013f.C0272a;
import com.iflytek.msc.p013f.C0278g;
import com.iflytek.msc.p013f.C0282j;
import com.iflytek.p005a.C0245a;
import com.iflytek.p007c.C0255a;
import com.iflytek.speech.C1076b;
import com.iflytek.speech.C1078c;
import com.iflytek.speech.C1079e;
import com.iflytek.speech.SpeechConfig;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SpeechUser;
import com.ifoer.mine.Contact;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.b.c */
public class C0254c {
    public static String m1131a() {
        C0262c d = C1076b.m2130d();
        if (d == null) {
            d = C1078c.m2146d();
        }
        if (d == null) {
            d = C1079e.m2158d();
        }
        if (d == null) {
            d = SpeechUser.getUser();
        }
        return d != null ? d.getInitParam().m1118a("appid") : XmlPullParser.NO_NAMESPACE;
    }

    private static String m1132a(int i) {
        return i == SpeechConfig.Rate8K ? "8k" : i == SpeechConfig.Rate11K ? "11k" : i == SpeechConfig.Rate16K ? "16k" : "22k";
    }

    public static String m1133a(Context context) {
        if (context == null) {
            return "null";
        }
        C0252a a = C0272a.m1207a(context);
        String str = a.m1118a("os.imsi") + "|" + a.m1118a("os.imei");
        if (str.length() < 10) {
            str = a.m1118a("net.mac");
        }
        return (TextUtils.isEmpty(str) || str.length() <= 0) ? null : str;
    }

    public static String m1134a(Context context, C0252a c0252a) throws SpeechError {
        if (context == null) {
            throw new SpeechError(8, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
        C0254c.m1140b(context, c0252a);
        c0252a.m1122a("timeout", "20000", false);
        c0252a.m1122a("auth", Contact.RELATION_FRIEND, false);
        c0252a.m1121a("msc.ver", Version.getVersion());
        if (TextUtils.isEmpty(C0255a.f966a)) {
            c0252a.m1121a("msc.skin", "null");
        } else {
            c0252a.m1121a("msc.skin", C0255a.f966a);
        }
        C0252a a = C0272a.m1207a(context);
        c0252a.m1122a("mac", a.m1118a("net.mac"), false);
        c0252a.m1122a("dvc", C0254c.m1133a(context), false);
        c0252a.m1122a("msc.lat", XmlPullParser.NO_NAMESPACE + C0245a.m1052a(context).m1055a("msc.lat"), false);
        c0252a.m1122a("msc.lng", XmlPullParser.NO_NAMESPACE + C0245a.m1052a(context).m1055a("msc.lng"), false);
        c0252a.m1120a(a, "app.name");
        c0252a.m1120a(a, "app.path");
        c0252a.m1120a(a, "app.pkg");
        c0252a.m1120a(a, "app.ver.name");
        c0252a.m1120a(a, "app.ver.code");
        c0252a.m1120a(a, "os.system");
        c0252a.m1120a(a, "os.resolution");
        c0252a.m1120a(a, "os.density");
        c0252a.m1120a(a, "net.mac");
        c0252a.m1120a(a, "os.imei");
        c0252a.m1120a(a, "os.imsi");
        c0252a.m1120a(a, "os.version");
        c0252a.m1120a(a, "os.release");
        c0252a.m1120a(a, C0272a.f1032a[0][0]);
        c0252a.m1120a(a, C0272a.f1032a[1][0]);
        c0252a.m1120a(a, C0272a.f1032a[2][0]);
        c0252a.m1120a(a, C0272a.f1032a[3][0]);
        C0254c.m1141b(c0252a);
        return c0252a.toString();
    }

    public static String m1135a(Context context, C0252a c0252a, String str) {
        C0254c.m1140b(context, c0252a);
        c0252a.m1122a("aue", "speex-wb", false);
        c0252a.m1122a("ssm", Contact.RELATION_FRIEND, false);
        c0252a.m1122a("auf=audio/L16;rate", XmlPullParser.NO_NAMESPACE + SpeechConfig.m1320c(), false);
        c0252a.m1122a("vcn", SpeechConfig.m1323d(), false);
        c0252a.m1122a("spd", SpeechConfig.m1326f(), false);
        c0252a.m1122a("vol", SpeechConfig.m1327g(), false);
        c0252a.m1122a("pch", XmlPullParser.NO_NAMESPACE + SpeechConfig.m1325e(), false);
        c0252a.m1122a("bgs", SpeechConfig.m1328h(), false);
        c0252a.m1122a("tte", str, false);
        return c0252a.toString();
    }

    public static String m1136a(Context context, String str, C0252a c0252a, boolean z) {
        C0254c.m1140b(context, c0252a);
        c0252a.m1122a("aue", "speex-wb", false);
        c0252a.m1122a("rst", "json", false);
        c0252a.m1122a("rse", "utf-8", false);
        c0252a.m1122a("tte", "gb2312", false);
        c0252a.m1122a("ssm", Contact.RELATION_FRIEND, false);
        if (TextUtils.isEmpty(str) || str.equals("asr")) {
            c0252a.m1122a("sub", "asr", false);
        } else {
            c0252a.m1122a("sub", "iat", false);
            c0252a.m1122a("ent", str + C0254c.m1132a(SpeechConfig.m1312a()), false);
        }
        c0252a.m1122a("auf=audio/L16;rate", XmlPullParser.NO_NAMESPACE + SpeechConfig.m1312a(), false);
        if (z) {
            c0252a.m1122a("vad_timeout", "5000", false);
            c0252a.m1122a("vad_speech_tail", "1800", false);
            c0252a.m1122a("eos", "1800", false);
        } else {
            c0252a.m1122a("vad_timeout", "4000", false);
            c0252a.m1122a("vad_speech_tail", "700", false);
            c0252a.m1122a("eos", "700", false);
        }
        return c0252a.toString();
    }

    public static boolean m1137a(C0252a c0252a) {
        return !c0252a.m1125a("net_enabled", true);
    }

    public static boolean m1138a(String str) {
        return TextUtils.isEmpty(str) ? false : str.contains("sms") || str.contains("cantonese");
    }

    public static String m1139b() {
        return C0266a.m1175e("md5");
    }

    public static void m1140b(Context context, C0252a c0252a) {
        if (context == null) {
            c0252a.m1122a("wap_proxy", PrivacyRule.SUBSCRIPTION_NONE, false);
            return;
        }
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            c0252a.m1122a("wap_proxy", PrivacyRule.SUBSCRIPTION_NONE, false);
            return;
        }
        c0252a.m1122a("wap_proxy", C0282j.m1262a(activeNetworkInfo), false);
        c0252a.m1122a("net_subtype", C0282j.m1264b(activeNetworkInfo), false);
    }

    private static void m1141b(C0252a c0252a) {
        if (c0252a != null && Setting.f931d != LOG_LEVEL.none) {
            String str = Setting.f932e;
            if (TextUtils.isEmpty(str)) {
                str = "/sdcard/msc/msc.log";
            }
            int i = -1;
            if (Setting.f931d == LOG_LEVEL.detail) {
                i = 31;
            } else if (Setting.f931d == LOG_LEVEL.normal) {
                i = 15;
            } else if (Setting.f931d == LOG_LEVEL.low) {
                i = 7;
            }
            C0278g.m1236c(str);
            c0252a.m1121a("log", str);
            c0252a.m1121a("lvl", XmlPullParser.NO_NAMESPACE + i);
        }
    }

    public static String m1142c(Context context, C0252a c0252a) {
        C0254c.m1140b(context, c0252a);
        c0252a.m1122a("ssm", Contact.RELATION_FRIEND, false);
        c0252a.m1122a("rst", "json", false);
        c0252a.m1122a("rse", "utf-8", false);
        c0252a.m1122a("tte", "utf-8", false);
        return c0252a.toString();
    }
}
