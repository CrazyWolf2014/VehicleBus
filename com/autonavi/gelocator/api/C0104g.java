package com.autonavi.gelocator.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.autonavi.gelocator.api.g */
final class C0104g extends BroadcastReceiver {
    private /* synthetic */ LbsManager f813a;

    C0104g(LbsManager lbsManager) {
        this.f813a = lbsManager;
    }

    public final void onReceive(Context context, Intent intent) {
        if (this.f813a.f755e != null && this.f813a.f755e.getScanResults() != null && this.f813a.f755e.getScanResults().size() > 0) {
            ArrayList arrayList = new ArrayList(this.f813a.f755e.getScanResults());
            this.f813a.f757g.size();
            this.f813a.f757g.clear();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ScanResult scanResult = (ScanResult) it.next();
                C0109l c0109l = new C0109l();
                c0109l.f827a = scanResult.BSSID;
                c0109l.f828b = scanResult.SSID;
                c0109l.f829c = scanResult.level;
                this.f813a.f757g.add(c0109l);
            }
        }
    }
}
