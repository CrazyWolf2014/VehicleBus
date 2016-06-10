package com.amap.mapapi.map;

/* compiled from: ThreadPool */
class aq {
    private Thread[] f628a;

    public aq(int i, Runnable runnable, Runnable runnable2) {
        this.f628a = new Thread[i];
        for (int i2 = 0; i2 < i; i2++) {
            if (i2 != 0 || i <= 1) {
                this.f628a[i2] = new Thread(runnable2);
            } else {
                this.f628a[i2] = new Thread(runnable);
            }
        }
    }

    public void m791a() {
        for (Thread thread : this.f628a) {
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void m792b() {
        int i = 1;
        int length = this.f628a.length;
        if (length > 1) {
            try {
                this.f628a[0].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (i < length) {
            try {
                this.f628a[i].join(300);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            i++;
        }
    }

    public void m793c() {
        if (this.f628a != null) {
            int length = this.f628a.length;
            for (int i = 0; i < length; i++) {
                this.f628a[i].interrupt();
                this.f628a[i] = null;
            }
            this.f628a = null;
        }
    }
}
