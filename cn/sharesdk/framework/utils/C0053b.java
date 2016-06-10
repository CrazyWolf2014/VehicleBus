package cn.sharesdk.framework.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.entity.Constant;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.json.JSONArray;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.utils.b */
public class C0053b {
    private static C0053b f101b;
    private Context f102a;

    public C0053b(Context context) {
        this.f102a = context.getApplicationContext();
    }

    public static C0053b m175a(Context context) {
        if (f101b == null) {
            f101b = new C0053b(context);
        }
        return f101b;
    }

    private Object m176a(String str) {
        try {
            return this.f102a.getSystemService(str);
        } catch (Throwable th) {
            C0058e.m220b(th);
            return null;
        }
    }

    private boolean m177a(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & 1) == 1) || ((packageInfo.applicationInfo.flags & Flags.FLAG8) == 1);
    }

    private boolean m178x() {
        TelephonyManager telephonyManager = (TelephonyManager) m176a("phone");
        if (telephonyManager == null) {
            return false;
        }
        switch (telephonyManager.getNetworkType()) {
            case KEYRecord.OWNER_USER /*0*/:
                return false;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return false;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return false;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return true;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return false;
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return true;
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return true;
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return false;
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return true;
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return true;
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return true;
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return false;
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return true;
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return true;
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return true;
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                return true;
            default:
                return false;
        }
    }

    public String m179a() {
        WifiManager wifiManager = (WifiManager) m176a("wifi");
        if (wifiManager == null) {
            return null;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo == null) {
            return null;
        }
        String macAddress = connectionInfo.getMacAddress();
        if (macAddress == null) {
            macAddress = null;
        }
        return macAddress;
    }

    public String m180a(String str, String str2) {
        String encodeToString;
        Throwable th;
        try {
            encodeToString = Base64.encodeToString(C0052a.m167a(str2, str), 0);
            try {
                if (encodeToString.contains(SpecilApiUtil.LINE_SEP)) {
                    encodeToString = encodeToString.replace(SpecilApiUtil.LINE_SEP, XmlPullParser.NO_NAMESPACE);
                }
            } catch (Throwable th2) {
                th = th2;
                C0058e.m220b(th);
                return encodeToString;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            encodeToString = null;
            th = th4;
            C0058e.m220b(th);
            return encodeToString;
        }
        return encodeToString;
    }

    public ArrayList<HashMap<String, String>> m181a(boolean z) {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList();
        try {
            PackageManager packageManager = this.f102a.getPackageManager();
            for (PackageInfo packageInfo : packageManager.getInstalledPackages(0)) {
                if (z || !m177a(packageInfo)) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("pkg", packageInfo.packageName);
                    hashMap.put("name", packageInfo.applicationInfo.loadLabel(packageManager).toString());
                    hashMap.put(AlixDefine.VERSION, packageInfo.versionName);
                    arrayList.add(hashMap);
                }
            }
        } catch (Throwable th) {
            C0058e.m220b(th);
        }
        return arrayList;
    }

    public String m182b() {
        return Build.MODEL;
    }

    public String m183c() {
        return Build.MANUFACTURER;
    }

    public String m184d() {
        TelephonyManager telephonyManager = (TelephonyManager) m176a("phone");
        if (telephonyManager == null) {
            return null;
        }
        String deviceId = telephonyManager.getDeviceId();
        String str = XmlPullParser.NO_NAMESPACE;
        if (deviceId != null) {
            str = new String(deviceId).replace(Contact.RELATION_ASK, XmlPullParser.NO_NAMESPACE);
        }
        if ((deviceId == null || r2.length() <= 0) && VERSION.SDK_INT >= 9) {
            try {
                Class cls = Class.forName("android.os.SystemProperties");
                deviceId = (String) cls.getMethod("get", new Class[]{String.class, String.class}).invoke(cls, new Object[]{"ro.serialno", "unknown"});
            } catch (Throwable th) {
                C0058e.m217a(th);
                deviceId = null;
            }
        }
        return deviceId;
    }

    public String m185e() {
        return m180a(m182b() + "|" + m187g() + "|" + m183c() + "|" + m190j() + "|" + m189i(), m195o().substring(0, 16));
    }

    public String m186f() {
        return m182b() + "|" + m187g() + "|" + m183c() + "|" + m190j() + "|" + m189i();
    }

    public String m187g() {
        return String.valueOf(VERSION.SDK_INT);
    }

    public String m188h() {
        return VERSION.RELEASE;
    }

    public String m189i() {
        DisplayMetrics displayMetrics = this.f102a.getResources().getDisplayMetrics();
        return this.f102a.getResources().getConfiguration().orientation == 1 ? displayMetrics.widthPixels + GroupChatInvitation.ELEMENT_NAME + displayMetrics.heightPixels : displayMetrics.heightPixels + GroupChatInvitation.ELEMENT_NAME + displayMetrics.widthPixels;
    }

    public String m190j() {
        TelephonyManager telephonyManager = (TelephonyManager) m176a("phone");
        if (telephonyManager == null) {
            return "-1";
        }
        String simOperator = telephonyManager.getSimOperator();
        if (TextUtils.isEmpty(simOperator)) {
            simOperator = "-1";
        }
        C0058e.m221c("================= operator: " + simOperator, new Object[0]);
        return simOperator;
    }

    public String m191k() {
        ConnectivityManager connectivityManager = (ConnectivityManager) m176a("connectivity");
        if (connectivityManager == null) {
            return PrivacyRule.SUBSCRIPTION_NONE;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
            return PrivacyRule.SUBSCRIPTION_NONE;
        }
        int type = activeNetworkInfo.getType();
        if (1 == type) {
            return "wifi";
        }
        if (type != 0) {
            return PrivacyRule.SUBSCRIPTION_NONE;
        }
        String defaultHost = Proxy.getDefaultHost();
        String str = XmlPullParser.NO_NAMESPACE;
        if (defaultHost != null && defaultHost.length() > 0) {
            str = " wap";
        }
        return (m178x() ? "3G" : "2G") + str;
    }

    public String m192l() {
        String k = m191k();
        return TextUtils.isEmpty(k) ? PrivacyRule.SUBSCRIPTION_NONE : ("wifi".equals(k) || PrivacyRule.SUBSCRIPTION_NONE.equals(k)) ? k : "cell";
    }

    public int m193m() {
        return 1;
    }

    public JSONArray m194n() {
        JSONArray jSONArray = new JSONArray();
        ActivityManager activityManager = (ActivityManager) m176a("activity");
        if (activityManager == null) {
            return jSONArray;
        }
        List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return jSONArray;
        }
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            jSONArray.put(runningAppProcessInfo.processName);
        }
        return jSONArray;
    }

    public String m195o() {
        try {
            String a = m179a();
            String d = m184d();
            return C0052a.m165a(C0052a.m166a(a + ":" + d + ":" + m182b()));
        } catch (Throwable th) {
            C0058e.m217a(th);
            return null;
        }
    }

    public String m196p() {
        return this.f102a.getPackageName();
    }

    public String m197q() {
        String str = this.f102a.getApplicationInfo().name;
        if (str != null) {
            return str;
        }
        int i = this.f102a.getApplicationInfo().labelRes;
        return i > 0 ? this.f102a.getString(i) : str;
    }

    public int m198r() {
        int i = 0;
        try {
            return this.f102a.getPackageManager().getPackageInfo(this.f102a.getPackageName(), 0).versionCode;
        } catch (Throwable th) {
            C0058e.m217a(th);
            return i;
        }
    }

    public String m199s() {
        try {
            return this.f102a.getPackageManager().getPackageInfo(this.f102a.getPackageName(), 0).versionName;
        } catch (Throwable th) {
            C0058e.m217a(th);
            return Constant.APP_VERSION;
        }
    }

    public String m200t() {
        TelephonyManager telephonyManager = (TelephonyManager) m176a("phone");
        return telephonyManager == null ? null : telephonyManager.getNetworkOperator();
    }

    public String m201u() {
        try {
            ActivityManager activityManager = (ActivityManager) m176a("activity");
            return activityManager == null ? null : ((RunningTaskInfo) activityManager.getRunningTasks(1).get(0)).topActivity.getPackageName();
        } catch (Throwable th) {
            C0058e.m220b(th);
            return null;
        }
    }

    public boolean m202v() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public String m203w() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
