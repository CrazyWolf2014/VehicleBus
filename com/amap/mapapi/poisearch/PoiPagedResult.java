package com.amap.mapapi.poisearch;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.PoiItem;
import com.amap.mapapi.poisearch.PoiSearch.Query;
import com.amap.mapapi.poisearch.PoiSearch.SearchBound;
import java.util.ArrayList;
import java.util.List;

public final class PoiPagedResult {
    private int f706a;
    private ArrayList<ArrayList<PoiItem>> f707b;
    private PoiSearchServerHandler f708c;

    static PoiPagedResult m859a(PoiSearchServerHandler poiSearchServerHandler, ArrayList<PoiItem> arrayList) {
        return new PoiPagedResult(poiSearchServerHandler, arrayList);
    }

    private PoiPagedResult(PoiSearchServerHandler poiSearchServerHandler, ArrayList<PoiItem> arrayList) {
        this.f708c = poiSearchServerHandler;
        this.f706a = m858a(poiSearchServerHandler.m2661c());
        m860a((ArrayList) arrayList);
    }

    private int m858a(int i) {
        int a = this.f708c.m2653a();
        a = ((i + a) - 1) / a;
        if (a > 30) {
            return 30;
        }
        return a;
    }

    private void m860a(ArrayList<PoiItem> arrayList) {
        this.f707b = new ArrayList();
        for (int i = 0; i <= this.f706a; i++) {
            this.f707b.add(null);
        }
        if (this.f706a > 0) {
            this.f707b.set(1, arrayList);
        }
    }

    public int getPageCount() {
        return this.f706a;
    }

    public Query getQuery() {
        return this.f708c.m2662d();
    }

    public SearchBound getBound() {
        return this.f708c.m2668l();
    }

    private boolean m861b(int i) {
        return i <= this.f706a && i > 0;
    }

    public List<PoiItem> getPageLocal(int i) {
        if (m861b(i)) {
            return (List) this.f707b.get(i);
        }
        throw new IllegalArgumentException("page out of range");
    }

    public List<PoiItem> getPage(int i) throws AMapException {
        if (this.f706a == 0) {
            return null;
        }
        if (m861b(i)) {
            ArrayList arrayList = (ArrayList) getPageLocal(i);
            if (arrayList != null) {
                return arrayList;
            }
            this.f708c.m2655a(i);
            arrayList = (ArrayList) this.f708c.m531j();
            this.f707b.set(i, arrayList);
            return arrayList;
        }
        throw new IllegalArgumentException("page out of range");
    }

    public List<String> getSearchSuggestions() {
        return this.f708c.m2669m();
    }
}
