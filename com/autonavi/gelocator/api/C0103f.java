package com.autonavi.gelocator.api;

import android.location.Location;
import android.location.LocationListener;

/* renamed from: com.autonavi.gelocator.api.f */
final class C0103f extends Thread {
    private /* synthetic */ LbsManager f808a;
    private final /* synthetic */ String f809b;
    private final /* synthetic */ float f810c;
    private final /* synthetic */ LocationListener f811d;
    private final /* synthetic */ long f812e;

    C0103f(LbsManager lbsManager, String str, float f, LocationListener locationListener, long j) {
        this.f808a = lbsManager;
        this.f809b = str;
        this.f810c = f;
        this.f811d = locationListener;
        this.f812e = j;
    }

    public final void run() {
        do {
            Location nowLocation = this.f808a.getNowLocation(this.f809b, false);
            if (nowLocation != null) {
                float[] fArr = new float[3];
                System.out.println("nowlat:" + nowLocation.getLatitude() + ";nowlon:" + nowLocation.getLongitude() + ";lastlat:" + this.f808a.f768r.getLatitude() + ";lastlon:" + this.f808a.f768r.getLongitude());
                Location.distanceBetween(nowLocation.getLatitude(), nowLocation.getLongitude(), this.f808a.f768r.getLatitude(), this.f808a.f768r.getLongitude(), fArr);
                System.out.println("distance:" + fArr[0] + ";minDistance:" + this.f810c);
                this.f808a.f768r = nowLocation;
                if (fArr[0] >= this.f810c) {
                    this.f811d.onLocationChanged(nowLocation);
                }
            }
            try {
                Thread.sleep(this.f812e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (this.f808a.f771u.containsKey(this.f811d));
    }
}
