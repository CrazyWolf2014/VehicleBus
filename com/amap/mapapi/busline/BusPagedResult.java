package com.amap.mapapi.busline;

import com.amap.mapapi.core.AMapException;
import java.util.ArrayList;
import java.util.List;

public final class BusPagedResult {
    private int f257a;
    private ArrayList<ArrayList<BusLineItem>> f258b;
    private BusSearchServerHandler f259c;

    static BusPagedResult m430a(BusSearchServerHandler busSearchServerHandler, ArrayList<BusLineItem> arrayList) {
        return new BusPagedResult(busSearchServerHandler, arrayList);
    }

    private BusPagedResult(BusSearchServerHandler busSearchServerHandler, ArrayList<BusLineItem> arrayList) {
        this.f259c = busSearchServerHandler;
        this.f257a = m429a(busSearchServerHandler.m2606d());
        m431a((ArrayList) arrayList);
    }

    private int m429a(int i) {
        int a = this.f259c.m2598a();
        a = ((i + a) - 1) / a;
        if (a > 30) {
            return 30;
        }
        return a;
    }

    private void m431a(ArrayList<BusLineItem> arrayList) {
        this.f258b = new ArrayList();
        for (int i = 0; i <= this.f257a; i++) {
            this.f258b.add(null);
        }
        if (this.f257a > 0) {
            this.f258b.set(1, arrayList);
        }
    }

    public int getPageCount() {
        return this.f257a;
    }

    public BusQuery getQuery() {
        return this.f259c.m2605c();
    }

    private boolean m432b(int i) {
        return i <= this.f257a && i > 0;
    }

    public List<BusLineItem> getPageLocal(int i) {
        if (m432b(i)) {
            return (List) this.f258b.get(i);
        }
        throw new IllegalArgumentException("page out of range");
    }

    public List<BusLineItem> getPage(int i) throws AMapException {
        if (this.f257a == 0) {
            return null;
        }
        if (m432b(i)) {
            ArrayList arrayList = (ArrayList) getPageLocal(i);
            if (arrayList != null) {
                return arrayList;
            }
            this.f259c.m2600a(i);
            arrayList = (ArrayList) this.f259c.m531j();
            this.f258b.set(i, arrayList);
            return arrayList;
        }
        throw new IllegalArgumentException("page out of range");
    }
}
