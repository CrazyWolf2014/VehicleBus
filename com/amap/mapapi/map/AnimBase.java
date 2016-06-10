package com.amap.mapapi.map;

/* renamed from: com.amap.mapapi.map.b */
class AnimBase implements Runnable {
    final /* synthetic */ AnimBase f664a;

    AnimBase(AnimBase animBase) {
        this.f664a = animBase;
    }

    public void run() {
        this.f664a.m661e();
        if (this.f664a.m662f()) {
            long currentTimeMillis = System.currentTimeMillis();
            this.f664a.m657a();
            this.f664a.m663g();
            long currentTimeMillis2 = System.currentTimeMillis();
            if (currentTimeMillis2 - currentTimeMillis < ((long) this.f664a.f517d)) {
                try {
                    Thread.sleep(((long) this.f664a.f517d) - (currentTimeMillis2 - currentTimeMillis));
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
            return;
        }
        this.f664a.f518e.removeCallbacks(this);
        this.f664a.f518e = null;
        this.f664a.m658b();
    }
}
