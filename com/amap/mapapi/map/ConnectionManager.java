package com.amap.mapapi.map;

/* renamed from: com.amap.mapapi.map.o */
class ConnectionManager extends Thread {
    aj f692a;
    int f693b;
    volatile boolean f694c;
    Thread f695d;
    MapView f696e;

    ConnectionManager() {
        this.f692a = new aj();
        this.f693b = 30;
        this.f694c = true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void m848a() {
        /*
        r6 = this;
        r3 = 0;
        monitor-enter(r6);
        r4 = r3;
    L_0x0003:
        r0 = r6.f692a;	 Catch:{ all -> 0x0076 }
        r0 = r0.size();	 Catch:{ all -> 0x0076 }
        if (r4 >= r0) goto L_0x0033;
    L_0x000b:
        r0 = r6.f692a;	 Catch:{ all -> 0x0076 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0076 }
        r0 = (com.amap.mapapi.map.aa) r0;	 Catch:{ all -> 0x0076 }
        r2 = r3;
    L_0x0014:
        r1 = r0.f521b;	 Catch:{ all -> 0x0076 }
        r1 = r1.size();	 Catch:{ all -> 0x0076 }
        if (r2 >= r1) goto L_0x002f;
    L_0x001c:
        r1 = r6.f696e;	 Catch:{ all -> 0x0076 }
        r5 = r1.tileDownloadCtrl;	 Catch:{ all -> 0x0076 }
        r1 = r0.f521b;	 Catch:{ all -> 0x0076 }
        r1 = r1.get(r4);	 Catch:{ all -> 0x0076 }
        r1 = (java.lang.String) r1;	 Catch:{ all -> 0x0076 }
        r5.m799a(r1);	 Catch:{ all -> 0x0076 }
        r1 = r2 + 1;
        r2 = r1;
        goto L_0x0014;
    L_0x002f:
        r0 = r4 + 1;
        r4 = r0;
        goto L_0x0003;
    L_0x0033:
        r0 = r6.f696e;	 Catch:{ all -> 0x0076 }
        r0 = r0.tileDownloadCtrl;	 Catch:{ all -> 0x0076 }
        r0.m798a();	 Catch:{ all -> 0x0076 }
        r4 = r3;
    L_0x003b:
        r0 = r6.f692a;	 Catch:{ all -> 0x0076 }
        r0 = r0.size();	 Catch:{ all -> 0x0076 }
        if (r4 >= r0) goto L_0x006f;
    L_0x0043:
        r0 = r6.f692a;	 Catch:{ all -> 0x0076 }
        r0 = r0.get(r4);	 Catch:{ all -> 0x0076 }
        r0 = (com.amap.mapapi.map.aa) r0;	 Catch:{ all -> 0x0076 }
        r1 = r0.f529j;	 Catch:{ all -> 0x0076 }
        if (r1 == 0) goto L_0x006b;
    L_0x004f:
        r2 = r3;
    L_0x0050:
        r1 = r0.f521b;	 Catch:{ all -> 0x0076 }
        r1 = r1.size();	 Catch:{ all -> 0x0076 }
        if (r2 >= r1) goto L_0x006b;
    L_0x0058:
        r1 = r6.f696e;	 Catch:{ all -> 0x0076 }
        r5 = r1.tileDownloadCtrl;	 Catch:{ all -> 0x0076 }
        r1 = r0.f521b;	 Catch:{ all -> 0x0076 }
        r1 = r1.get(r4);	 Catch:{ all -> 0x0076 }
        r1 = (java.lang.String) r1;	 Catch:{ all -> 0x0076 }
        r5.m803c(r1);	 Catch:{ all -> 0x0076 }
        r1 = r2 + 1;
        r2 = r1;
        goto L_0x0050;
    L_0x006b:
        r0 = r4 + 1;
        r4 = r0;
        goto L_0x003b;
    L_0x006f:
        r0 = r6.f692a;	 Catch:{ all -> 0x0076 }
        r0.clear();	 Catch:{ all -> 0x0076 }
        monitor-exit(r6);
        return;
    L_0x0076:
        r0 = move-exception;
        monitor-exit(r6);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.amap.mapapi.map.o.a():void");
    }

    public synchronized void m849a(aa aaVar) {
        this.f692a.insertElementAt(aaVar, 0);
    }

    public void m850b() {
        this.f694c = false;
        if (this.f695d != null) {
            this.f695d.interrupt();
        }
    }

    public void run() {
        while (this.f694c) {
            this.f695d = Thread.currentThread();
            aa aaVar = (aa) this.f692a.m773b();
            if (aaVar == null) {
                try {
                    ConnectionManager.sleep((long) this.f693b);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            } else if (System.currentTimeMillis() - aaVar.f523d > 50) {
                aaVar.m670b();
            }
        }
    }
}
