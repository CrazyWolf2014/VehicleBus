package com.autonavi.gelocator.api;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import com.amap.mapapi.location.LocationManagerProxy;
import java.util.ArrayList;
import java.util.HashMap;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xmlpull.v1.XmlPullParser;

public class LbsManager {
    public static final String LBS_PROVIDER = "lbs";
    public static LbsManager lbsManager;
    private Context f751a;
    private String f752b;
    private String f753c;
    private String f754d;
    private WifiManager f755e;
    private C0104g f756f;
    private ArrayList f757g;
    private TelephonyManager f758h;
    private PhoneStateListener f759i;
    private int f760j;
    private ArrayList f761k;
    private ArrayList f762l;
    private int f763m;
    private C0108k f764n;
    private LocationManager f765o;
    private LocationListener f766p;
    private C0107j f767q;
    private Location f768r;
    private ConnectivityManager f769s;
    private DES f770t;
    private HashMap f771u;

    private LbsManager(Context context, String str) {
        this.f752b = XmlPullParser.NO_NAMESPACE;
        this.f753c = "autonavi00spas$#@!666666";
        this.f754d = "autonavi";
        this.f763m = 10;
        this.f770t = null;
        this.f751a = context;
        this.f768r = new Location(XmlPullParser.NO_NAMESPACE);
        this.f771u = new HashMap();
        this.f770t = new DES(this.f753c);
        this.f752b = str;
        if (this.f752b == null || (this.f752b != null && this.f752b.equals(XmlPullParser.NO_NAMESPACE))) {
            try {
                this.f752b = (String) context.getPackageManager().getApplicationInfo(context.getPackageName(), Flags.FLAG8).metaData.get("LICENSE");
            } catch (NameNotFoundException e) {
                e.printStackTrace();
                this.f752b = XmlPullParser.NO_NAMESPACE;
            }
        }
        this.f757g = new ArrayList();
        this.f755e = (WifiManager) this.f751a.getSystemService("wifi");
        this.f756f = new C0104g(this);
        this.f751a.registerReceiver(this.f756f, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
        new C0100c(this).start();
        this.f761k = new ArrayList();
        this.f762l = new ArrayList();
        this.f760j = -1;
        this.f759i = new C0101d(this);
        this.f758h = (TelephonyManager) this.f751a.getSystemService("phone");
        this.f758h.listen(this.f759i, KEYRecord.OWNER_ZONE);
        this.f758h.listen(this.f759i, 16);
        if (this.f758h.getCellLocation() != null) {
            this.f759i.onCellLocationChanged(this.f758h.getCellLocation());
        }
        this.f767q = new C0107j();
        this.f765o = (LocationManager) this.f751a.getSystemService(LocationManagerProxy.KEY_LOCATION_CHANGED);
        m901a();
        this.f769s = (ConnectivityManager) context.getSystemService("connectivity");
        this.f764n = new C0108k();
        if (this.f764n.f821a == null) {
            this.f764n.f821a = Build.MANUFACTURER;
        }
        if (this.f764n.f822b == null) {
            this.f764n.f822b = Build.DEVICE;
        }
        if (this.f764n.f823c == null) {
            this.f764n.f823c = VERSION.RELEASE;
        }
        if (this.f758h.getDeviceId() != null) {
            this.f764n.f824d = this.f758h.getDeviceId();
        }
        if (this.f764n.f825e == null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            displayMetrics = this.f751a.getResources().getDisplayMetrics();
            this.f764n.f825e = displayMetrics.widthPixels + "*" + displayMetrics.heightPixels;
        }
        NetworkInfo activeNetworkInfo = this.f769s.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return;
        }
        if (activeNetworkInfo.getSubtypeName().equals(XmlPullParser.NO_NAMESPACE)) {
            this.f764n.f826f = activeNetworkInfo.getTypeName();
            return;
        }
        this.f764n.f826f = activeNetworkInfo.getSubtypeName();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized com.autonavi.gelocator.api.MyLocaitonBean m900a(java.lang.String r6) {
        /*
        r5 = this;
        r3 = 0;
        monitor-enter(r5);
        r0 = r5.f751a;	 Catch:{ Exception -> 0x0057 }
        r0 = com.autonavi.gelocator.api.NetManagerApache.getInstance(r0);	 Catch:{ Exception -> 0x0057 }
        r1 = "http://naps.amap.com/SAPS/r";
        r0 = r0.doPostXmlAsString(r1, r6);	 Catch:{ Exception -> 0x0057 }
        r1 = "";
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x0057 }
        if (r1 != 0) goto L_0x005b;
    L_0x0017:
        r1 = r5.f770t;	 Catch:{ Exception -> 0x0057 }
        r2 = new com.autonavi.gelocator.api.ParserXml;	 Catch:{ Exception -> 0x0057 }
        r2.<init>();	 Catch:{ Exception -> 0x0057 }
        r0 = r2.ParserSapsXml(r0);	 Catch:{ Exception -> 0x0057 }
        r2 = "GBK";
        r0 = r1.decrypt(r0, r2);	 Catch:{ Exception -> 0x0057 }
        r1 = "";
        r1 = r0.equals(r1);	 Catch:{ Exception -> 0x0057 }
        if (r1 != 0) goto L_0x005b;
    L_0x0030:
        r1 = new com.autonavi.gelocator.api.ParserXml;	 Catch:{ Exception -> 0x0057 }
        r1.<init>();	 Catch:{ Exception -> 0x0057 }
        r0 = r1.ParserLocationXml(r0);	 Catch:{ Exception -> 0x0057 }
        r1 = r0.getResult();	 Catch:{ Exception -> 0x0057 }
        r2 = "";
        r1 = r1.equals(r2);	 Catch:{ Exception -> 0x0057 }
        if (r1 == 0) goto L_0x005b;
    L_0x0045:
        r1 = r0.getCenx();	 Catch:{ Exception -> 0x0057 }
        r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r1 == 0) goto L_0x005b;
    L_0x004d:
        r1 = r0.getCeny();	 Catch:{ Exception -> 0x0057 }
        r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r1 == 0) goto L_0x005b;
    L_0x0055:
        monitor-exit(r5);
        return r0;
    L_0x0057:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x005d }
    L_0x005b:
        r0 = 0;
        goto L_0x0055;
    L_0x005d:
        r0 = move-exception;
        monitor-exit(r5);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autonavi.gelocator.api.LbsManager.a(java.lang.String):com.autonavi.gelocator.api.MyLocaitonBean");
    }

    private void m901a() {
        this.f766p = new C0102e(this);
        for (String str : this.f765o.getAllProviders()) {
            if (str.equals(LocationManagerProxy.GPS_PROVIDER)) {
                this.f765o.requestLocationUpdates(str, 5000, 0.0f, this.f766p, Looper.getMainLooper());
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized java.lang.String m904b() {
        /*
        r9 = this;
        r7 = 0;
        r2 = 1;
        monitor-enter(r9);
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r3.<init>();	 Catch:{ all -> 0x0456 }
        r0 = "<?xml version=\"1.0\" encoding=\"GBK\" ?>";
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = "<location>";
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r1 = "<license>";
        r0.<init>(r1);	 Catch:{ all -> 0x0456 }
        r1 = r9.f752b;	 Catch:{ all -> 0x0456 }
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r1 = "</license>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r1 = "<src>";
        r0.<init>(r1);	 Catch:{ all -> 0x0456 }
        r1 = r9.f754d;	 Catch:{ all -> 0x0456 }
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r1 = "</src>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<imei>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r4 = r9.f764n;	 Catch:{ all -> 0x0456 }
        r5 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0456 }
        r5.<init>();	 Catch:{ all -> 0x0456 }
        r0 = r4.f824d;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x0299;
    L_0x0059:
        r0 = r4.f824d;	 Catch:{ all -> 0x0456 }
    L_0x005b:
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r6 = ":";
        r0.append(r6);	 Catch:{ all -> 0x0456 }
        r0 = r4.f823c;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x029d;
    L_0x0068:
        r0 = r4.f823c;	 Catch:{ all -> 0x0456 }
    L_0x006a:
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r6 = ":";
        r0.append(r6);	 Catch:{ all -> 0x0456 }
        r0 = r4.f821a;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x02a1;
    L_0x0077:
        r0 = r4.f821a;	 Catch:{ all -> 0x0456 }
    L_0x0079:
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r6 = ":";
        r0.append(r6);	 Catch:{ all -> 0x0456 }
        r0 = r4.f822b;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x02a5;
    L_0x0086:
        r0 = r4.f822b;	 Catch:{ all -> 0x0456 }
    L_0x0088:
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r6 = ":";
        r0.append(r6);	 Catch:{ all -> 0x0456 }
        r0 = r4.f825e;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x02a9;
    L_0x0095:
        r0 = r4.f825e;	 Catch:{ all -> 0x0456 }
    L_0x0097:
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r6 = ":";
        r0.append(r6);	 Catch:{ all -> 0x0456 }
        r0 = r4.f826f;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x02ad;
    L_0x00a4:
        r0 = r4.f826f;	 Catch:{ all -> 0x0456 }
    L_0x00a6:
        r5.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r5.toString();	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</imei>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f760j;	 Catch:{ all -> 0x0456 }
        if (r0 != r2) goto L_0x0313;
    L_0x00c2:
        r0 = "<cdma>0</cdma>";
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x0182;
    L_0x00cb:
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r0 = r0.size();	 Catch:{ all -> 0x0456 }
        if (r0 <= 0) goto L_0x0182;
    L_0x00d3:
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<mnc>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f801c;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</mnc>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<mcc>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f802d;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</mcc>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<lac>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f800b;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</lac>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<cellid>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f799a;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</cellid>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<signal>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f803e;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</signal>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
    L_0x0182:
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x01b8;
    L_0x0186:
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r0 = r0.size();	 Catch:{ all -> 0x0456 }
        if (r0 <= r2) goto L_0x01b8;
    L_0x018e:
        r4 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0456 }
        r4.<init>();	 Catch:{ all -> 0x0456 }
        r1 = r2;
    L_0x0194:
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r0 = r0.size();	 Catch:{ all -> 0x0456 }
        if (r1 < r0) goto L_0x02b1;
    L_0x019c:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r1 = "<nb>";
        r0.<init>(r1);	 Catch:{ all -> 0x0456 }
        r1 = r4.toString();	 Catch:{ all -> 0x0456 }
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r1 = "</nb>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
    L_0x01b8:
        r0 = r9.f767q;	 Catch:{ all -> 0x0456 }
        r0 = r0.f818a;	 Catch:{ all -> 0x0456 }
        r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
        if (r0 == 0) goto L_0x04bb;
    L_0x01c0:
        r0 = r9.f767q;	 Catch:{ all -> 0x0456 }
        r0 = r0.f819b;	 Catch:{ all -> 0x0456 }
        r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1));
        if (r0 == 0) goto L_0x04bb;
    L_0x01c8:
        r0 = "<gps>1</gps>";
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r1 = "<glong>";
        r0.<init>(r1);	 Catch:{ all -> 0x0456 }
        r1 = r9.f767q;	 Catch:{ all -> 0x0456 }
        r1 = r1.f819b;	 Catch:{ all -> 0x0456 }
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r1 = "</glong>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r1 = "<glat>";
        r0.<init>(r1);	 Catch:{ all -> 0x0456 }
        r1 = r9.f767q;	 Catch:{ all -> 0x0456 }
        r1 = r1.f818a;	 Catch:{ all -> 0x0456 }
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r1 = "</glat>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
    L_0x0205:
        r0 = r9.f757g;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x023e;
    L_0x0209:
        r0 = r9.f757g;	 Catch:{ all -> 0x0456 }
        r0 = r0.size();	 Catch:{ all -> 0x0456 }
        if (r0 <= 0) goto L_0x023e;
    L_0x0211:
        r1 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0456 }
        r1.<init>();	 Catch:{ all -> 0x0456 }
        r0 = r9.f757g;	 Catch:{ all -> 0x0456 }
        r2 = r0.iterator();	 Catch:{ all -> 0x0456 }
    L_0x021c:
        r0 = r2.hasNext();	 Catch:{ all -> 0x0456 }
        if (r0 != 0) goto L_0x04c2;
    L_0x0222:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r2 = "<macs>";
        r0.<init>(r2);	 Catch:{ all -> 0x0456 }
        r1 = r1.toString();	 Catch:{ all -> 0x0456 }
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r1 = "</macs>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
    L_0x023e:
        r0 = "</location>";
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0456 }
        r1.<init>();	 Catch:{ all -> 0x0456 }
        r0 = "<?xml version=\"1.0\" encoding=\"GBK\" ?>";
        r1.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = "<saps>";
        r1.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r2 = "<src>";
        r0.<init>(r2);	 Catch:{ all -> 0x0456 }
        r2 = r9.f754d;	 Catch:{ all -> 0x0456 }
        r0 = r0.append(r2);	 Catch:{ all -> 0x0456 }
        r2 = "</src>";
        r0 = r0.append(r2);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r1.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x04f9 }
        r2 = "<sreq>";
        r0.<init>(r2);	 Catch:{ Exception -> 0x04f9 }
        r2 = r9.f770t;	 Catch:{ Exception -> 0x04f9 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x04f9 }
        r2 = r2.encrypt(r3);	 Catch:{ Exception -> 0x04f9 }
        r0 = r0.append(r2);	 Catch:{ Exception -> 0x04f9 }
        r2 = "</sreq>";
        r0 = r0.append(r2);	 Catch:{ Exception -> 0x04f9 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x04f9 }
        r1.append(r0);	 Catch:{ Exception -> 0x04f9 }
    L_0x028e:
        r0 = "</saps>";
        r1.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r1.toString();	 Catch:{ all -> 0x0456 }
        monitor-exit(r9);
        return r0;
    L_0x0299:
        r0 = "";
        goto L_0x005b;
    L_0x029d:
        r0 = "";
        goto L_0x006a;
    L_0x02a1:
        r0 = "";
        goto L_0x0079;
    L_0x02a5:
        r0 = "";
        goto L_0x0088;
    L_0x02a9:
        r0 = "";
        goto L_0x0097;
    L_0x02ad:
        r0 = "";
        goto L_0x00a6;
    L_0x02b1:
        if (r1 <= r2) goto L_0x02b8;
    L_0x02b3:
        r0 = "*";
        r4.append(r0);	 Catch:{ all -> 0x0456 }
    L_0x02b8:
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f801c;	 Catch:{ all -> 0x0456 }
        r0 = java.lang.String.valueOf(r0);	 Catch:{ all -> 0x0456 }
        r5.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = ",";
        r5 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f800b;	 Catch:{ all -> 0x0456 }
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r5 = ",";
        r5 = r0.append(r5);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f799a;	 Catch:{ all -> 0x0456 }
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r5 = ",";
        r5 = r0.append(r5);	 Catch:{ all -> 0x0456 }
        r0 = r9.f761k;	 Catch:{ all -> 0x0456 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0099b) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f803e;	 Catch:{ all -> 0x0456 }
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r4.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0194;
    L_0x0313:
        r0 = r9.f760j;	 Catch:{ all -> 0x0456 }
        r1 = 2;
        if (r0 != r1) goto L_0x01b8;
    L_0x0318:
        r0 = "<cdma>1</cdma>";
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x041e;
    L_0x0321:
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r0 = r0.size();	 Catch:{ all -> 0x0456 }
        if (r0 <= 0) goto L_0x041e;
    L_0x0329:
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<mcc>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f794g;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</mcc>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<sid>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f792e;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</sid>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<nid>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f791d;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</nid>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<bid>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f790c;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</bid>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<lon>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f789b;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</lon>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<lat>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f788a;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</lat>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = "<signal>";
        r1.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r4 = 0;
        r0 = r0.get(r4);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f795h;	 Catch:{ all -> 0x0456 }
        r0 = r1.append(r0);	 Catch:{ all -> 0x0456 }
        r1 = "</signal>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
    L_0x041e:
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        if (r0 == 0) goto L_0x01b8;
    L_0x0422:
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r0 = r0.size();	 Catch:{ all -> 0x0456 }
        if (r0 <= r2) goto L_0x01b8;
    L_0x042a:
        r4 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0456 }
        r4.<init>();	 Catch:{ all -> 0x0456 }
        r1 = r2;
    L_0x0430:
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r0 = r0.size();	 Catch:{ all -> 0x0456 }
        if (r1 < r0) goto L_0x0459;
    L_0x0438:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r1 = "<nb>";
        r0.<init>(r1);	 Catch:{ all -> 0x0456 }
        r1 = r4.toString();	 Catch:{ all -> 0x0456 }
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r1 = "</nb>";
        r0 = r0.append(r1);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        goto L_0x01b8;
    L_0x0456:
        r0 = move-exception;
        monitor-exit(r9);
        throw r0;
    L_0x0459:
        if (r1 <= r2) goto L_0x0460;
    L_0x045b:
        r0 = "*";
        r4.append(r0);	 Catch:{ all -> 0x0456 }
    L_0x0460:
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f793f;	 Catch:{ all -> 0x0456 }
        r0 = java.lang.String.valueOf(r0);	 Catch:{ all -> 0x0456 }
        r5.<init>(r0);	 Catch:{ all -> 0x0456 }
        r0 = ",";
        r5 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f798k;	 Catch:{ all -> 0x0456 }
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r5 = ",";
        r5 = r0.append(r5);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f797j;	 Catch:{ all -> 0x0456 }
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r5 = ",";
        r5 = r0.append(r5);	 Catch:{ all -> 0x0456 }
        r0 = r9.f762l;	 Catch:{ all -> 0x0456 }
        r0 = r0.get(r1);	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0098a) r0;	 Catch:{ all -> 0x0456 }
        r0 = r0.f795h;	 Catch:{ all -> 0x0456 }
        r0 = r5.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r4.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0430;
    L_0x04bb:
        r0 = "<gps>0</gps>";
        r3.append(r0);	 Catch:{ all -> 0x0456 }
        goto L_0x0205;
    L_0x04c2:
        r0 = r2.next();	 Catch:{ all -> 0x0456 }
        r0 = (com.autonavi.gelocator.api.C0109l) r0;	 Catch:{ all -> 0x0456 }
        r4 = r1.toString();	 Catch:{ all -> 0x0456 }
        r5 = "";
        r4 = r4.equals(r5);	 Catch:{ all -> 0x0456 }
        if (r4 != 0) goto L_0x04d9;
    L_0x04d4:
        r4 = "*";
        r1.append(r4);	 Catch:{ all -> 0x0456 }
    L_0x04d9:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0456 }
        r5 = r0.f827a;	 Catch:{ all -> 0x0456 }
        r5 = java.lang.String.valueOf(r5);	 Catch:{ all -> 0x0456 }
        r4.<init>(r5);	 Catch:{ all -> 0x0456 }
        r5 = ",";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0456 }
        r0 = r0.f829c;	 Catch:{ all -> 0x0456 }
        r0 = r4.append(r0);	 Catch:{ all -> 0x0456 }
        r0 = r0.toString();	 Catch:{ all -> 0x0456 }
        r1.append(r0);	 Catch:{ all -> 0x0456 }
        goto L_0x021c;
    L_0x04f9:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0456 }
        goto L_0x028e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.autonavi.gelocator.api.LbsManager.b():java.lang.String");
    }

    public static LbsManager getInstance(Context context, String str) {
        if (lbsManager == null) {
            lbsManager = new LbsManager(context, str);
        }
        return lbsManager;
    }

    public void destroy() {
        if (this.f756f != null) {
            this.f751a.unregisterReceiver(this.f756f);
            this.f756f = null;
        }
        this.f758h.listen(this.f759i, 0);
        this.f765o.removeUpdates(this.f766p);
        lbsManager = null;
        this.f771u.clear();
    }

    public Location getLastKnownLocation() {
        if (this.f768r == null) {
            this.f768r = new Location(XmlPullParser.NO_NAMESPACE);
        }
        return this.f768r;
    }

    public Location getNowLocation(String str) {
        return getNowLocation(str, true);
    }

    public Location getNowLocation(String str, boolean z) {
        Location location;
        Exception e;
        String b = m904b();
        if (b != null) {
            MyLocaitonBean a = m900a(b);
            if (a != null) {
                try {
                    location = new Location(getLastKnownLocation());
                    try {
                        location.setLongitude(a.getCenx());
                        location.setLatitude(a.getCeny());
                        location.setAccuracy((float) a.getRadius());
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("address", a.getDes().split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR));
                        bundle.putString("cityCode", a.getCityCode());
                        location.setExtras(bundle);
                        location.setTime(System.currentTimeMillis());
                        location.setProvider(str);
                    } catch (Exception e2) {
                        e = e2;
                        e.printStackTrace();
                        if (z) {
                            this.f768r = location;
                        }
                        return location;
                    }
                } catch (Exception e3) {
                    e = e3;
                    location = null;
                    e.printStackTrace();
                    if (z) {
                        this.f768r = location;
                    }
                    return location;
                }
                if (z) {
                    this.f768r = location;
                }
                return location;
            }
        }
        location = null;
        if (z) {
            this.f768r = location;
        }
        return location;
    }

    public boolean removeLbsUpdates(LocationListener locationListener) {
        if (!this.f771u.containsKey(locationListener)) {
            return false;
        }
        this.f771u.remove(locationListener);
        return true;
    }

    public boolean requestLbsLocationUpdates(long j, float f, LocationListener locationListener, String str) {
        float f2 = 0.0f;
        if (locationListener == null) {
            throw new IllegalArgumentException("Listener==null");
        }
        long j2 = j <= 0 ? 1000 : j;
        if (f >= 0.0f) {
            f2 = f;
        }
        if (this.f771u.containsKey(locationListener)) {
            return false;
        }
        this.f771u.put(locationListener, Float.valueOf(f2));
        startOneLocationRequestListener(locationListener, f2, j2, str);
        return true;
    }

    public void startOneLocationRequestListener(LocationListener locationListener, float f, long j, String str) {
        this.f758h.listen(this.f759i, KEYRecord.OWNER_ZONE);
        this.f758h.listen(this.f759i, 16);
        new C0103f(this, str, f, locationListener, j).start();
    }
}
