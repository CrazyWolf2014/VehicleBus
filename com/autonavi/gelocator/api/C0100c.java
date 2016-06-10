package com.autonavi.gelocator.api;

/* renamed from: com.autonavi.gelocator.api.c */
final class C0100c extends Thread {
    private /* synthetic */ LbsManager f805a;

    C0100c(LbsManager lbsManager) {
        this.f805a = lbsManager;
    }

    public final void run() {
        if (this.f805a.f755e.getWifiState() == 3) {
            this.f805a.f755e.startScan();
        }
    }
}
