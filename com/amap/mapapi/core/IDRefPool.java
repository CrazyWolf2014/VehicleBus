package com.amap.mapapi.core;

/* renamed from: com.amap.mapapi.core.g */
public class IDRefPool<T> extends SyncList<T> {
    public void m1873a(T t) {
        if (!this.a.contains(t)) {
            this.a.add(t);
        }
    }

    public void m1874b(T t) {
        for (int i = 0; i < this.a.size(); i++) {
            if (t == this.a.get(i)) {
                this.a.remove(i);
                return;
            }
        }
    }
}
