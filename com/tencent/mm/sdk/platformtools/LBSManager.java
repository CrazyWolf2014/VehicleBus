package com.tencent.mm.sdk.platformtools;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import com.amap.mapapi.location.LocationManagerProxy;
import com.ifoer.util.MyHttpException;
import com.tencent.mm.sdk.platformtools.MTimerHandler.CallBack;
import com.tencent.mm.sdk.platformtools.PhoneUtil.MacInfo;
import java.util.LinkedList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class LBSManager extends BroadcastReceiver {
    private static LocationCache f1642F = null;
    public static final String FILTER_GPS = "filter_gps";
    public static final int INVALID_ACC = -1000;
    public static final float INVALID_LAT = -1000.0f;
    public static final float INVALID_LNG = -1000.0f;
    public static final int MM_SOURCE_HARDWARE = 0;
    public static final int MM_SOURCE_NET = 1;
    public static final int MM_SOURCE_REPORT_HARWARE = 3;
    public static final int MM_SOURCE_REPORT_NETWORK = 4;
    private OnLocationGotListener f1643G;
    private LocationManager f1644H;
    private PendingIntent f1645I;
    private boolean f1646J;
    boolean f1647K;
    boolean f1648L;
    boolean f1649M;
    int f1650N;
    private MTimerHandler f1651O;
    private Context f1652q;

    static class LocationCache {
        float f1638Q;
        float f1639R;
        int f1640S;
        int f1641T;
        long time;

        LocationCache() {
            this.f1638Q = LBSManager.INVALID_LNG;
            this.f1639R = LBSManager.INVALID_LNG;
            this.f1640S = LBSManager.INVALID_ACC;
            this.f1641T = LBSManager.MM_SOURCE_NET;
        }
    }

    public interface OnLocationGotListener {
        void onLocationGot(float f, float f2, int i, int i2, String str, String str2, boolean z);
    }

    /* renamed from: com.tencent.mm.sdk.platformtools.LBSManager.1 */
    class C11121 implements CallBack {
        final /* synthetic */ LBSManager f2239P;

        C11121(LBSManager lBSManager) {
            this.f2239P = lBSManager;
        }

        public boolean onTimerExpired() {
            Log.m1663v("MicroMsg.LBSManager", "get location by GPS failed.");
            this.f2239P.f1647K = true;
            this.f2239P.start();
            this.f2239P.f1646J = false;
            return false;
        }
    }

    public LBSManager(Context context, OnLocationGotListener onLocationGotListener) {
        this.f1646J = false;
        this.f1648L = false;
        this.f1649M = false;
        this.f1651O = new MTimerHandler(new C11121(this), false);
        this.f1643G = onLocationGotListener;
        this.f1647K = false;
        this.f1650N = MM_SOURCE_HARDWARE;
        this.f1652q = context;
        PhoneUtil.getSignalStrength(context);
        this.f1644H = (LocationManager) context.getSystemService(LocationManagerProxy.KEY_LOCATION_CHANGED);
        m1650a();
        this.f1645I = PendingIntent.getBroadcast(context, MM_SOURCE_HARDWARE, new Intent(FILTER_GPS), 134217728);
    }

    private boolean m1650a() {
        if (this.f1644H == null) {
            return false;
        }
        try {
            this.f1644H.sendExtraCommand(LocationManagerProxy.GPS_PROVIDER, "force_xtra_injection", null);
            this.f1644H.sendExtraCommand(LocationManagerProxy.GPS_PROVIDER, "force_time_injection", null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void m1652b() {
        this.f1651O.stopTimer();
        this.f1647K = true;
    }

    public static void setLocationCache(float f, float f2, int i, int i2) {
        if (i != 0) {
            Log.m1663v("MicroMsg.LBSManager", "setLocationCache [" + f + "," + f2 + "] acc:" + i + " source:" + i2);
            if (f1642F == null) {
                f1642F = new LocationCache();
            }
            f1642F.f1638Q = f;
            f1642F.f1639R = f2;
            f1642F.f1640S = i;
            f1642F.time = System.currentTimeMillis();
            f1642F.f1641T = i2;
        }
    }

    public String getTelLocation() {
        return PhoneUtil.getCellXml(PhoneUtil.getCellInfoList(this.f1652q));
    }

    public String getWIFILocation() {
        WifiManager wifiManager = (WifiManager) this.f1652q.getSystemService("wifi");
        if (wifiManager == null) {
            Log.m1657e("MicroMsg.LBSManager", "no wifi service");
            return XmlPullParser.NO_NAMESPACE;
        } else if (wifiManager.getConnectionInfo() == null) {
            Log.m1657e("MicroMsg.LBSManager", "WIFILocation wifi info null");
            return XmlPullParser.NO_NAMESPACE;
        } else {
            List linkedList = new LinkedList();
            List scanResults = wifiManager.getScanResults();
            if (scanResults != null) {
                for (int i = MM_SOURCE_HARDWARE; i < scanResults.size(); i += MM_SOURCE_NET) {
                    linkedList.add(new MacInfo(((ScanResult) scanResults.get(i)).BSSID, ((ScanResult) scanResults.get(i)).level));
                }
            }
            return PhoneUtil.getMacXml(linkedList);
        }
    }

    public boolean isGpsEnable() {
        try {
            return this.f1644H.isProviderEnabled(LocationManagerProxy.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNetworkPrividerEnable() {
        try {
            return this.f1644H.isProviderEnabled(LocationManagerProxy.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onReceive(Context context, Intent intent) {
        Location location = (Location) intent.getExtras().get(LocationManagerProxy.KEY_LOCATION_CHANGED);
        this.f1650N += MM_SOURCE_NET;
        if (location != null) {
            boolean equals = LocationManagerProxy.GPS_PROVIDER.equals(location.getProvider());
            if (((equals && location.getAccuracy() <= 200.0f) || (!equals && location.getAccuracy() <= 1000.0f)) && location.getAccuracy() > 0.0f) {
                int i = equals ? MM_SOURCE_HARDWARE : MM_SOURCE_NET;
                setLocationCache((float) location.getLatitude(), (float) location.getLongitude(), (int) location.getAccuracy(), i);
                if (this.f1643G == null) {
                    return;
                }
                if (!this.f1647K || !this.f1648L || !this.f1649M) {
                    String nullAsNil = Util.nullAsNil(getWIFILocation());
                    String nullAsNil2 = Util.nullAsNil(getTelLocation());
                    if (!this.f1647K) {
                        m1652b();
                        this.f1647K = true;
                        Log.m1663v("MicroMsg.LBSManager", "location by provider ok:[" + location.getLatitude() + " , " + location.getLongitude() + "]  accuracy:" + location.getAccuracy() + "  retry count:" + this.f1650N + " isGpsProvider:" + equals);
                        this.f1643G.onLocationGot((float) location.getLatitude(), (float) location.getLongitude(), (int) location.getAccuracy(), i, nullAsNil, nullAsNil2, true);
                    } else if (!this.f1648L && i == 0) {
                        this.f1648L = true;
                        Log.m1663v("MicroMsg.LBSManager", "report location by GPS ok:[" + location.getLatitude() + " , " + location.getLongitude() + "]  accuracy:" + location.getAccuracy() + "  retry count:" + this.f1650N + " isGpsProvider:" + equals);
                        this.f1643G.onLocationGot((float) location.getLatitude(), (float) location.getLongitude(), (int) location.getAccuracy(), MM_SOURCE_REPORT_HARWARE, nullAsNil, nullAsNil2, true);
                    } else if (!this.f1649M && i == MM_SOURCE_NET) {
                        this.f1649M = true;
                        Log.m1663v("MicroMsg.LBSManager", "report location by Network ok:[" + location.getLatitude() + " , " + location.getLongitude() + "]  accuracy:" + location.getAccuracy() + "  retry count:" + this.f1650N + " isGpsProvider:" + equals);
                        this.f1643G.onLocationGot((float) location.getLatitude(), (float) location.getLongitude(), (int) location.getAccuracy(), MM_SOURCE_REPORT_NETWORK, nullAsNil, nullAsNil2, true);
                    }
                }
            }
        }
    }

    public void removeGpsUpdate() {
        Log.m1663v("MicroMsg.LBSManager", "removed gps update");
        if (this.f1644H != null) {
            this.f1644H.removeUpdates(this.f1645I);
        }
        try {
            this.f1652q.unregisterReceiver(this);
        } catch (Exception e) {
            Log.m1663v("MicroMsg.LBSManager", "location receiver has already unregistered");
        }
    }

    public void removeListener() {
        Log.m1663v("MicroMsg.LBSManager", "removed gps update on destroy");
        removeGpsUpdate();
        if (this.f1651O != null) {
            m1652b();
        }
        this.f1643G = null;
        this.f1652q = null;
        this.f1651O = null;
        this.f1644H = null;
    }

    public void requestGpsUpdate() {
        requestGpsUpdate(MyHttpException.ERROR_SERVER);
    }

    public void requestGpsUpdate(int i) {
        if (isGpsEnable() || isNetworkPrividerEnable()) {
            if (i <= 0) {
                i = MyHttpException.ERROR_SERVER;
            }
            Log.m1663v("MicroMsg.LBSManager", "requested gps update");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(FILTER_GPS);
            this.f1652q.registerReceiver(this, intentFilter);
            if (isGpsEnable()) {
                this.f1644H.requestLocationUpdates(LocationManagerProxy.GPS_PROVIDER, (long) i, 0.0f, this.f1645I);
            }
            if (isNetworkPrividerEnable()) {
                this.f1644H.requestLocationUpdates(LocationManagerProxy.NETWORK_PROVIDER, (long) i, 0.0f, this.f1645I);
            }
        }
    }

    public void resetForContinueGetLocation() {
        this.f1648L = false;
        this.f1649M = false;
    }

    public void start() {
        String nullAsNil = Util.nullAsNil(getWIFILocation());
        String nullAsNil2 = Util.nullAsNil(getTelLocation());
        int i = (isGpsEnable() || isNetworkPrividerEnable()) ? true : MM_SOURCE_HARDWARE;
        if (i == 0 || this.f1646J) {
            if (f1642F == null) {
                i = MM_SOURCE_HARDWARE;
            } else if (System.currentTimeMillis() - f1642F.time > 180000 || f1642F.f1640S <= 0) {
                i = MM_SOURCE_HARDWARE;
            } else {
                boolean z = true;
            }
            if (i == 0) {
                this.f1647K = true;
                if (nullAsNil.equals(XmlPullParser.NO_NAMESPACE) && nullAsNil2.equals(XmlPullParser.NO_NAMESPACE)) {
                    Log.m1663v("MicroMsg.LBSManager", "get location by network failed");
                    if (this.f1643G != null) {
                        this.f1643G.onLocationGot(INVALID_LNG, INVALID_LNG, INVALID_ACC, MM_SOURCE_HARDWARE, XmlPullParser.NO_NAMESPACE, XmlPullParser.NO_NAMESPACE, false);
                        return;
                    }
                    return;
                }
                Log.m1663v("MicroMsg.LBSManager", "get location by network ok, macs : " + nullAsNil + " cell ids :" + nullAsNil2);
                if (this.f1643G != null) {
                    this.f1643G.onLocationGot(INVALID_LNG, INVALID_LNG, INVALID_ACC, MM_SOURCE_HARDWARE, nullAsNil, nullAsNil2, true);
                    return;
                }
                return;
            } else if (this.f1643G != null) {
                this.f1647K = true;
                Log.m1663v("MicroMsg.LBSManager", "location by GPS cache ok:[" + f1642F.f1638Q + " , " + f1642F.f1639R + "]  accuracy:" + f1642F.f1640S + " source:" + f1642F.f1641T);
                this.f1643G.onLocationGot(f1642F.f1638Q, f1642F.f1639R, f1642F.f1640S, f1642F.f1641T, nullAsNil, nullAsNil2, true);
                return;
            } else {
                return;
            }
        }
        this.f1646J = true;
        this.f1650N = MM_SOURCE_HARDWARE;
        requestGpsUpdate();
        this.f1651O.startTimer(3000);
    }
}
