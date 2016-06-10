package com.amap.mapapi.map;

import android.content.Context;
import com.amap.mapapi.core.AMapException;
import java.net.Proxy;
import java.util.ArrayList;

/* renamed from: com.amap.mapapi.map.c */
abstract class AsyncServer<T, V> extends ad {
    protected volatile boolean f1897a;
    protected ArrayList<Thread> f1898b;
    protected ap<T> f1899c;
    protected aq f1900d;
    private Runnable f1901g;
    private Runnable f1902h;

    protected abstract ArrayList<T> m1984a(ArrayList<T> arrayList) throws AMapException;

    protected abstract ArrayList<T> m1985a(ArrayList<T> arrayList, Proxy proxy) throws AMapException;

    protected abstract int m1988c();

    protected abstract int m1989d();

    public AsyncServer(ag agVar, Context context) {
        super(agVar, context);
        this.f1897a = true;
        this.f1898b = new ArrayList();
        this.f1901g = new AsyncServer(this);
        this.f1902h = new AsyncServer(this);
        this.f1900d = new aq(m1988c(), this.f1902h, this.f1901g);
        this.f1900d.m791a();
    }

    public void m1986a() {
        this.f1899c.m786a();
        m1987b();
        this.f1899c.m790c();
        this.f1899c = null;
        this.e = null;
        this.f = null;
    }

    public void m1987b() {
        this.f1897a = false;
        if (this.f1898b != null) {
            int size = this.f1898b.size();
            for (int i = 0; i < size; i++) {
                Thread thread = (Thread) this.f1898b.get(0);
                if (thread != null) {
                    thread.interrupt();
                    this.f1898b.remove(0);
                }
            }
            this.f1898b = null;
        }
        this.f1900d.m792b();
        if (this.f1900d != null) {
            this.f1900d.m793c();
            this.f1900d = null;
        }
    }
}
