package com.ifoer.util;

public class CarDiagnoseBridge {
    Boolean ishas;

    public CarDiagnoseBridge() {
        this.ishas = Boolean.valueOf(false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void getData() {
        /*
        r2 = this;
        monitor-enter(r2);
    L_0x0001:
        r1 = r2.ishas;	 Catch:{ all -> 0x0014 }
        r1 = r1.booleanValue();	 Catch:{ all -> 0x0014 }
        if (r1 == 0) goto L_0x000b;
    L_0x0009:
        monitor-exit(r2);
        return;
    L_0x000b:
        r2.wait();	 Catch:{ InterruptedException -> 0x000f }
        goto L_0x0001;
    L_0x000f:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0014 }
        goto L_0x0001;
    L_0x0014:
        r1 = move-exception;
        monitor-exit(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.util.CarDiagnoseBridge.getData():void");
    }

    public synchronized void putData() {
        this.ishas = Boolean.valueOf(true);
        notifyAll();
    }
}
