package com.ifoer.util;

public class Bridge {
    Boolean ishas;

    public Bridge() {
        this.ishas = Boolean.valueOf(false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void getData() {
        /*
        r4 = this;
        monitor-enter(r4);
    L_0x0001:
        r3 = r4.ishas;	 Catch:{ all -> 0x0018 }
        r3 = r3.booleanValue();	 Catch:{ all -> 0x0018 }
        if (r3 == 0) goto L_0x000f;
    L_0x0009:
        r1 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0018 }
        monitor-exit(r4);
        return;
    L_0x000f:
        r4.wait();	 Catch:{ InterruptedException -> 0x0013 }
        goto L_0x0001;
    L_0x0013:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0018 }
        goto L_0x0001;
    L_0x0018:
        r3 = move-exception;
        monitor-exit(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.util.Bridge.getData():void");
    }

    public synchronized void putData() {
        this.ishas = Boolean.valueOf(true);
        notifyAll();
    }
}
