package com.amap.mapapi.map;

import android.content.Context;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.SyncList;
import com.amap.mapapi.map.ar.Tile;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/* compiled from: TileServer */
class au extends AsyncServer<Tile, Tile> implements bi {
    private InprocessingTiles f2360g;

    public void b_() {
    }

    public void m2529a() {
        super.m1986a();
        this.f2360g.clear();
    }

    public au(ag agVar, Context context) {
        super(agVar, context);
        this.f2360g = new InprocessingTiles();
        this.c = new as();
        agVar.f594b.m745a((bi) this);
    }

    protected ArrayList<Tile> m2528a(ArrayList<Tile> arrayList, Proxy proxy) throws AMapException {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        if (this.e == null || this.e.f596d == null || this.e.f596d.f564a == null) {
            return null;
        }
        if (((Tile) arrayList.get(0)).f633e >= this.e.f596d.f564a.size()) {
            return null;
        }
        m2530a((List) arrayList);
        if (arrayList.size() == 0) {
            return null;
        }
        if (this.e.f596d.f564a.size() == 0) {
            return null;
        }
        ArrayList<Tile> arrayList2;
        if (((LayerPropertys) this.e.f596d.f564a.get(((Tile) arrayList.get(0)).f633e)).f1927j != null) {
            av avVar = new av(arrayList, proxy, this.e.f597e.m733a(), this.e.f597e.m735b());
            avVar.m1954a((LayerPropertys) this.e.f596d.f564a.get(((Tile) arrayList.get(0)).f633e));
            arrayList2 = (ArrayList) avVar.m531j();
            avVar.m1954a(null);
        } else {
            bb bbVar = new bb(arrayList, proxy, this.e.f597e.m733a(), this.e.f597e.m735b());
            bbVar.m1967a((LayerPropertys) this.e.f596d.f564a.get(((Tile) arrayList.get(0)).f633e));
            arrayList2 = (ArrayList) bbVar.m531j();
            bbVar.m1967a(null);
        }
        m2525b(arrayList);
        if (this.e == null || this.e.f596d == null) {
            return arrayList2;
        }
        this.e.f596d.m721d();
        return arrayList2;
    }

    public void m2530a(List<Tile> list) {
        if (list != null) {
            int size = list.size();
            if (size != 0) {
                int i = 0;
                while (i < size) {
                    int i2;
                    if (this.f2360g.m2005b((Tile) list.get(i))) {
                        i2 = i;
                        i = size;
                    } else {
                        list.remove(i);
                        i2 = i - 1;
                        i = size - 1;
                    }
                    size = i;
                    i = i2 + 1;
                }
            }
        }
    }

    private void m2525b(ArrayList<Tile> arrayList) {
        if (arrayList != null && this.f2360g != null) {
            int size = arrayList.size();
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    this.f2360g.m2004a((Tile) arrayList.get(i));
                }
            }
        }
    }

    private void m2523a(ArrayList<Tile> arrayList, boolean z) {
        if (this.c != null && arrayList != null && arrayList.size() != 0) {
            this.c.m787a((List) arrayList, z);
        }
    }

    public void a_() {
    }

    private boolean m2526g() {
        if (this.e == null || this.e.f596d == null) {
            return false;
        }
        if (this.e.f596d.f564a == null) {
            return false;
        }
        int size = this.e.f596d.f564a.size();
        if (size <= 0) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            LayerPropertys layerPropertys = (LayerPropertys) this.e.f596d.f564a.get(i);
            if (layerPropertys != null && layerPropertys.f1923f) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Tile> m2522a(ArrayList<Tile> arrayList, LayerPropertys layerPropertys, int i, boolean z) {
        if (arrayList == null || layerPropertys == null || !layerPropertys.f1923f || layerPropertys.f1932o == null) {
            return null;
        }
        layerPropertys.f1932o.clear();
        if (i > layerPropertys.f1919b || i < layerPropertys.f1920c) {
            return null;
        }
        int size = arrayList.size();
        if (size <= 0) {
            return null;
        }
        ArrayList<Tile> arrayList2 = new ArrayList();
        for (int i2 = 0; i2 < size; i2++) {
            Tile tile = (Tile) arrayList.get(i2);
            if (tile != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(tile.f630b);
                stringBuilder.append("-");
                stringBuilder.append(tile.f631c);
                stringBuilder.append("-");
                stringBuilder.append(tile.f632d);
                int a = layerPropertys.f1930m.m831a(stringBuilder.toString());
                Tile tile2 = new Tile(tile.f630b, tile.f631c, tile.f632d, layerPropertys.f1928k);
                tile2.f635g = a;
                tile2.f634f = tile.f634f;
                layerPropertys.f1932o.add(tile2);
                if (!(!m2524a(tile2) || z || this.f2360g.contains(tile2))) {
                    if (!layerPropertys.f1924g) {
                        tile2.f629a = -1;
                    }
                    arrayList2.add(tile2);
                }
            }
        }
        return arrayList2;
    }

    public void m2531a(boolean z, boolean z2) {
        if (m2526g()) {
            ArrayList a = this.e.f598f.m680a(this.e.f598f.f543j, this.e.f598f.f540g, this.e.f594b.m750c(), this.e.f594b.m752d());
            if (a != null && a.size() > 0) {
                int size = this.e.f596d.f564a.size();
                int i = 0;
                boolean z3 = true;
                while (i < size) {
                    boolean z4;
                    ArrayList a2 = m2522a(a, (LayerPropertys) this.e.f596d.f564a.get(i), this.e.f594b.m753e(), z2);
                    if (a2 != null) {
                        m2523a(a2, z3);
                        if (z3) {
                            z4 = false;
                        } else {
                            z4 = z3;
                        }
                        a2.clear();
                    } else {
                        z4 = z3;
                    }
                    i++;
                    z3 = z4;
                }
                a.clear();
                this.e.f594b.m755g().invalidate();
            }
        }
    }

    private boolean m2524a(Tile tile) {
        return tile == null || tile.f635g < 0;
    }

    protected int m2533d() {
        return 1;
    }

    protected int m2532c() {
        return 3;
    }

    protected ArrayList<Tile> m2527a(ArrayList<Tile> arrayList) {
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        if (size == 0) {
            return null;
        }
        int i = 0;
        ArrayList<Tile> arrayList2 = null;
        while (i < size) {
            int i2;
            ArrayList<Tile> arrayList3;
            Tile tile = (Tile) arrayList.get(i);
            if (tile == null) {
                i2 = i;
                arrayList3 = arrayList2;
                i = size;
            } else if (this.e == null || this.e.f596d == null || this.e.f596d.f564a == null) {
                return null;
            } else {
                if (tile.f633e >= this.e.f596d.f564a.size()) {
                    i2 = i;
                    arrayList3 = arrayList2;
                    i = size;
                } else if (((LayerPropertys) this.e.f596d.f564a.get(tile.f633e)).f1924g) {
                    int a = ((LayerPropertys) this.e.f596d.f564a.get(tile.f633e)).f1931n.m845a(tile);
                    if (a >= 0) {
                        arrayList.remove(i);
                        size--;
                        i--;
                        SyncList syncList = ((LayerPropertys) this.e.f596d.f564a.get(tile.f633e)).f1932o;
                        if (syncList == null) {
                            i2 = i;
                            arrayList3 = arrayList2;
                            i = size;
                        } else {
                            int size2 = syncList.size();
                            for (int i3 = 0; i3 < size2; i3++) {
                                Tile tile2 = (Tile) syncList.get(i3);
                                if (tile2 != null && tile2.equals(tile)) {
                                    tile2.f635g = a;
                                    this.e.f596d.m721d();
                                    break;
                                }
                            }
                            i2 = i;
                            arrayList3 = arrayList2;
                            i = size;
                        }
                    } else {
                        if (arrayList2 == null) {
                            arrayList3 = new ArrayList();
                        } else {
                            arrayList3 = arrayList2;
                        }
                        Tile tile3 = new Tile(tile);
                        tile3.f629a = -1;
                        arrayList3.add(tile3);
                        i2 = i;
                        i = size;
                    }
                } else {
                    i2 = i;
                    arrayList3 = arrayList2;
                    i = size;
                }
            }
            arrayList2 = arrayList3;
            size = i;
            i = i2 + 1;
        }
        return arrayList2;
    }
}
