package com.amap.mapapi.map;

import java.util.Vector;

/* compiled from: ConnectionManager */
class aj extends Vector {
    protected int f617a;

    aj() {
        this.f617a = -1;
    }

    public synchronized Object m772a() {
        Object obj;
        if (m774c()) {
            obj = null;
        } else {
            obj = super.elementAt(0);
            super.removeElementAt(0);
        }
        return obj;
    }

    public synchronized Object m773b() {
        Object obj;
        if (m774c()) {
            obj = null;
        } else {
            obj = super.elementAt(0);
        }
        return obj;
    }

    public boolean m774c() {
        return super.isEmpty();
    }

    public synchronized void clear() {
        super.removeAllElements();
    }
}
