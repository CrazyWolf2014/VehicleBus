package com.amap.mapapi.map;

import java.util.Hashtable;
import org.xmlpull.v1.XmlPullParser;

/* compiled from: TileDownloadCtrl */
class at extends Thread {
    int f639a;
    MapView f640b;
    long f641c;
    volatile boolean f642d;
    Thread f643e;
    private Hashtable f644f;

    public synchronized void m799a(String str) {
        this.f644f.remove(str);
    }

    public synchronized boolean m801b(String str) {
        return this.f644f.get(str) != null;
    }

    public synchronized void m803c(String str) {
        this.f644f.put(str, XmlPullParser.NO_NAMESPACE);
    }

    public synchronized void m798a() {
        this.f644f.clear();
    }

    public at(MapView mapView) {
        this.f644f = new Hashtable();
        this.f639a = 0;
        this.f642d = true;
        this.f640b = mapView;
        m800b();
    }

    public void m800b() {
        this.f641c = System.currentTimeMillis();
    }

    public void m802c() {
        this.f642d = false;
        if (this.f643e != null) {
            this.f643e.interrupt();
        }
    }

    public void run() {
        this.f643e = Thread.currentThread();
        while (this.f642d) {
            if (this.f639a > 0 && System.currentTimeMillis() - this.f641c > 300) {
                this.f640b.loadBMtilesData2(this.f640b.m651e(), true);
            }
            try {
                sleep(50);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
