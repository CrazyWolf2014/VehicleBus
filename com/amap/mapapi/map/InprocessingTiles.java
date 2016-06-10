package com.amap.mapapi.map;

import com.amap.mapapi.core.SyncList;
import com.amap.mapapi.map.ar.Tile;

/* renamed from: com.amap.mapapi.map.v */
class InprocessingTiles extends SyncList<Tile> {
    InprocessingTiles() {
    }

    synchronized void m2004a(Tile tile) {
        remove((Object) tile);
    }

    synchronized boolean m2005b(Tile tile) {
        boolean z = true;
        synchronized (this) {
            if (contains(tile)) {
                z = false;
            } else {
                m544c(tile);
            }
        }
        return z;
    }
}
