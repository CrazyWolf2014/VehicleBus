package com.amap.mapapi.core;

/* renamed from: com.amap.mapapi.core.i */
public class MapServerUrl {
    private static MapServerUrl f349g;
    private String f350a;
    private String f351b;
    private String f352c;
    private String f353d;
    private String f354e;
    private String f355f;

    private MapServerUrl() {
        this.f350a = "http://emap0.mapabc.com";
        this.f351b = "http://tm.mapabc.com";
        this.f352c = "http://api.amap.com:9090/sisserver";
        this.f353d = "http://ds.mapabc.com:8888";
        this.f354e = "http://si.mapabc.com";
        this.f355f = "http://tmds.mapabc.com";
    }

    public static synchronized MapServerUrl m503a() {
        MapServerUrl mapServerUrl;
        synchronized (MapServerUrl.class) {
            if (f349g == null) {
                f349g = new MapServerUrl();
            }
            mapServerUrl = f349g;
        }
        return mapServerUrl;
    }

    public String m505b() {
        return this.f350a;
    }

    public String m507c() {
        return this.f351b;
    }

    public String m509d() {
        return this.f352c;
    }

    public String m511e() {
        return this.f354e;
    }

    public void m504a(String str) {
        this.f354e = str;
    }

    public String m513f() {
        return this.f355f;
    }

    public void m506b(String str) {
        this.f355f = str;
    }

    public String m515g() {
        return this.f353d;
    }

    public void m508c(String str) {
        this.f350a = str;
    }

    public void m510d(String str) {
        this.f351b = str;
    }

    public void m512e(String str) {
        this.f352c = str;
    }

    public void m514f(String str) {
        this.f353d = str;
    }
}
