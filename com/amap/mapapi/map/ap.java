package com.amap.mapapi.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

/* compiled from: TaskPool */
class ap<T> {
    protected LinkedList<T> f625a;
    protected final Semaphore f626b;
    protected boolean f627c;

    ap() {
        this.f625a = new LinkedList();
        this.f626b = new Semaphore(0, false);
        this.f627c = true;
    }

    public void m786a() {
        this.f627c = false;
        this.f626b.release(100);
    }

    public synchronized void m787a(List<T> list, boolean z) {
        if (this.f625a != null) {
            if (z) {
                this.f625a.clear();
            }
            if (list != null) {
                this.f625a.addAll(list);
            }
            m789b();
        }
    }

    protected void m789b() {
        if (this.f625a != null && this.f627c && this.f625a.size() != 0) {
            this.f626b.release();
        }
    }

    public void m790c() {
        if (this.f625a != null) {
            this.f625a.clear();
        }
    }

    public ArrayList<T> m785a(int i, boolean z) {
        if (this.f625a == null) {
            return null;
        }
        try {
            this.f626b.acquire();
        } catch (InterruptedException e) {
        }
        if (this.f627c) {
            return m788b(i, z);
        }
        return null;
    }

    protected synchronized ArrayList<T> m788b(int i, boolean z) {
        ArrayList<T> arrayList;
        synchronized (this) {
            if (this.f625a == null) {
                arrayList = null;
            } else {
                int size = this.f625a.size();
                if (i > size) {
                    i = size;
                }
                arrayList = new ArrayList(i);
                for (int i2 = 0; i2 < i; i2++) {
                    arrayList.add(this.f625a.get(0));
                    this.f625a.removeFirst();
                }
                m789b();
            }
        }
        return arrayList;
    }
}
