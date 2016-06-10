package com.iflytek.p005a;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import com.amap.mapapi.location.LocationManagerProxy;
import com.iflytek.Setting;
import com.iflytek.msc.p013f.C0272a;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.msc.p013f.C0278g;
import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: com.iflytek.a.a */
public class C0245a {
    public static C0245a f933a;
    private SharedPreferences f934b;
    private Context f935c;
    private boolean f936d;
    private long f937e;

    static {
        f933a = null;
    }

    private C0245a(Context context) {
        this.f934b = null;
        this.f935c = null;
        this.f936d = true;
        this.f937e = 0;
        this.f935c = context;
        this.f934b = context.getSharedPreferences("com.iflytek.msc", 0);
        this.f936d = C0245a.m1053b(context);
    }

    public static C0245a m1052a(Context context) {
        if (f933a == null && context != null) {
            C0245a.m1054c(context);
        }
        return f933a;
    }

    public static boolean m1053b(Context context) {
        try {
            if (!Setting.f930c || context == null) {
                return false;
            }
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), Flags.EXTEND).requestedPermissions;
            int i = 0;
            while (i < strArr.length) {
                if ("android.permission.ACCESS_FINE_LOCATION".equalsIgnoreCase(strArr[i]) || "android.permission.ACCESS_COARSE_LOCATION".equalsIgnoreCase(strArr[i])) {
                    return true;
                }
                i++;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static synchronized C0245a m1054c(Context context) {
        C0245a c0245a;
        synchronized (C0245a.class) {
            if (f933a == null) {
                f933a = new C0245a(context);
            }
            C0278g.m1229a(context);
            C0272a.m1207a(context);
            C0250g.m1098a();
            C0247c.m1071a();
            c0245a = f933a;
        }
        return c0245a;
    }

    public synchronized float m1055a(String str) {
        try {
            if (this.f936d && System.currentTimeMillis() - this.f937e > 216000) {
                LocationManager locationManager = (LocationManager) this.f935c.getApplicationContext().getSystemService(LocationManagerProxy.KEY_LOCATION_CHANGED);
                this.f937e = System.currentTimeMillis();
                m1057a("loction_last_update", this.f937e);
                C0276e.m1224c("getLocation begin:" + System.currentTimeMillis());
                Criteria criteria = new Criteria();
                criteria.setAccuracy(1);
                C0276e.m1224c("bestProvider:" + locationManager.getBestProvider(criteria, true));
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManagerProxy.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    C0276e.m1220a(lastKnownLocation.toString());
                    m1056a(lastKnownLocation);
                } else {
                    Location lastKnownLocation2 = locationManager.getLastKnownLocation(LocationManagerProxy.NETWORK_PROVIDER);
                    if (lastKnownLocation2 != null) {
                        C0276e.m1220a(lastKnownLocation2.toString());
                        m1056a(lastKnownLocation2);
                    }
                }
                C0276e.m1224c("getLocation end:" + System.currentTimeMillis());
            }
        } catch (Exception e) {
        }
        return this.f934b.getFloat(str, -0.1f);
    }

    public void m1056a(Location location) {
        if (location != null) {
            Editor edit = this.f934b.edit();
            edit.putFloat("msc.lat", (float) location.getLatitude());
            edit.putFloat("msc.lng", (float) location.getLongitude());
            edit.commit();
        }
    }

    public void m1057a(String str, long j) {
        Editor edit = this.f934b.edit();
        edit.putLong(str, j);
        edit.commit();
    }

    public void m1058a(String str, boolean z) {
        Editor edit = this.f934b.edit();
        edit.putBoolean(str, z);
        edit.commit();
    }

    public long m1059b(String str, long j) {
        return this.f934b.getLong(str, j);
    }

    public boolean m1060b(String str, boolean z) {
        return this.f934b.getBoolean(str, z);
    }
}
