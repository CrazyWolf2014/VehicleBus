package com.amap.mapapi.map;

import com.amap.mapapi.map.ar.Tile;
import java.util.ArrayList;

/* compiled from: TaskPool */
class as extends ap<Tile> {
    as() {
    }

    protected synchronized ArrayList<Tile> m1948b(int i, boolean z) {
        ArrayList<Tile> arrayList;
        int i2 = 0;
        synchronized (this) {
            if (this.a == null) {
                arrayList = null;
            } else {
                int size = this.a.size();
                if (i > size) {
                    i = size;
                }
                ArrayList<Tile> arrayList2 = new ArrayList(i);
                int i3 = 0;
                while (i2 < size) {
                    if (this.a == null) {
                        arrayList = null;
                        break;
                    }
                    int i4;
                    Tile tile = (Tile) this.a.get(i2);
                    if (tile == null) {
                        i4 = i2;
                        i2 = i3;
                        i3 = size;
                    } else {
                        int i5 = tile.f629a;
                        if (z) {
                            if (i5 == 0) {
                                arrayList2.add(tile);
                                this.a.remove(i2);
                                i4 = i2 - 1;
                                i2 = i3 + 1;
                                i3 = size - 1;
                            }
                            i4 = i2;
                            i2 = i3;
                            i3 = size;
                        } else {
                            if (i5 < 0) {
                                arrayList2.add(tile);
                                this.a.remove(i2);
                                i4 = i2 - 1;
                                i2 = i3 + 1;
                                i3 = size - 1;
                            }
                            i4 = i2;
                            i2 = i3;
                            i3 = size;
                        }
                        if (i2 >= i) {
                            break;
                        }
                    }
                    size = i3;
                    i3 = i2;
                    i2 = i4 + 1;
                }
                m789b();
                arrayList = arrayList2;
            }
        }
        return arrayList;
    }
}
