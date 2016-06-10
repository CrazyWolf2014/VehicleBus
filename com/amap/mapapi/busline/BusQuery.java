package com.amap.mapapi.busline;

import com.amap.mapapi.core.CoreUtil;

public class BusQuery {
    private String f261a;
    private SearchType f262b;
    private String f263c;

    public enum SearchType {
        BY_ID,
        BY_LINE_NAME,
        BY_STATION_NAME
    }

    public BusQuery(String str, SearchType searchType) {
        this(str, searchType, null);
    }

    public BusQuery(String str, SearchType searchType, String str2) {
        this.f261a = str;
        this.f262b = searchType;
        this.f263c = str2;
        if (!m433a()) {
            throw new IllegalArgumentException("Empty query");
        }
    }

    private boolean m433a() {
        return !CoreUtil.m488a(this.f261a);
    }

    public String getQueryString() {
        return this.f261a;
    }

    public SearchType getCategory() {
        return this.f262b;
    }

    public String getCity() {
        return this.f263c;
    }
}
