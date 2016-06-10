package com.amap.mapapi.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.Random;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.amap.mapapi.core.a */
public class ClientInfoUtil {
    private static ClientInfoUtil f311a;
    private static String f312b;
    private static Context f313c;
    private static TelephonyManager f314d;
    private static ConnectivityManager f315e;
    private static String f316f;

    static {
        f311a = null;
        f312b = null;
        f313c = null;
    }

    public static ClientInfoUtil m471a(Context context) {
        if (f311a == null) {
            f311a = new ClientInfoUtil();
            f313c = context;
            f314d = (TelephonyManager) f313c.getSystemService("phone");
            f316f = f313c.getApplicationContext().getPackageName();
        }
        return f311a;
    }

    private ClientInfoUtil() {
    }

    public String m472a() {
        StringBuilder stringBuilder = new StringBuilder();
        String simSerialNumber = f314d.getSimSerialNumber();
        String subscriberId = f314d.getSubscriberId();
        stringBuilder.append("imei=" + simSerialNumber);
        stringBuilder.append("&imsi=" + subscriberId);
        stringBuilder.append("&pkg=" + f316f);
        stringBuilder.append("&model=");
        stringBuilder.append(m474c());
        stringBuilder.append("&sys=");
        stringBuilder.append(m473b());
        stringBuilder.append("&nettype=");
        stringBuilder.append(m476e());
        simSerialNumber = f314d.getSimOperatorName();
        stringBuilder.append("&netprovider=");
        stringBuilder.append(simSerialNumber);
        stringBuilder.append("&uid=");
        stringBuilder.append("'" + m477f() + "'");
        stringBuilder.append("&sys_ver=");
        stringBuilder.append(m473b());
        stringBuilder.append("&ntt=");
        stringBuilder.append(m476e());
        stringBuilder.append("&bid=");
        stringBuilder.append(m475d());
        Log.d("inituser net", stringBuilder.toString());
        return stringBuilder.toString();
    }

    public String m473b() {
        return VERSION.RELEASE;
    }

    public String m474c() {
        return Build.MODEL;
    }

    public String m475d() {
        return Build.ID;
    }

    public String m476e() {
        if (f313c.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        if (f315e == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        NetworkInfo activeNetworkInfo = f315e.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return activeNetworkInfo.getTypeName();
    }

    public String m477f() {
        int nextInt = new Random().nextInt();
        f312b = f314d.getDeviceId();
        return nextInt + f312b;
    }
}
