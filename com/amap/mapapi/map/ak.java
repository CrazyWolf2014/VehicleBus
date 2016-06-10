package com.amap.mapapi.map;

import java.util.ArrayList;

/* compiled from: RenderPool */
class ak extends Thread {
    volatile boolean f618a;
    Thread f619b;
    MapView f620c;
    ArrayList<al> f621d;

    public ak(MapView mapView) {
        this.f618a = true;
        this.f621d = new ArrayList();
        this.f620c = mapView;
    }

    public void m775a() {
        this.f618a = false;
        if (this.f619b != null) {
            this.f619b.interrupt();
        }
    }

    public void run() {
        while (this.f618a) {
            this.f619b = Thread.currentThread();
            if (this.f621d.size() > 0) {
                al alVar = (al) this.f621d.get(0);
                this.f621d.remove(0);
                if (this.f620c.m646a(alVar.f622a)) {
                    alVar.m778a(this.f620c.f489f);
                    this.f620c.postInvalidate();
                }
            }
            try {
                sleep(50);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void m776a(al alVar) {
        this.f621d.add(alVar);
    }

    public boolean m777a(String str) {
        int i = 0;
        while (i < this.f621d.size()) {
            try {
                if (((al) this.f621d.get(i)).f622a.equals(str)) {
                    return true;
                }
                i++;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
