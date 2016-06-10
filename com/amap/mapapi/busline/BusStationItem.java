package com.amap.mapapi.busline;

import com.amap.mapapi.core.GeoPoint;

public class BusStationItem {
    private String f267a;
    private GeoPoint f268b;
    private String f269c;
    private String f270d;
    private int f271e;

    public String getmName() {
        return this.f267a;
    }

    public void setmName(String str) {
        this.f267a = str;
    }

    public GeoPoint getmCoord() {
        return this.f268b;
    }

    public void setmCoord(GeoPoint geoPoint) {
        this.f268b = geoPoint;
    }

    public String getmSpell() {
        return this.f269c;
    }

    public void setmSpell(String str) {
        this.f269c = str;
    }

    public String getmCode() {
        return this.f270d;
    }

    public void setmCode(String str) {
        this.f270d = str;
    }

    public int getmStationNum() {
        return this.f271e;
    }

    public void setmStationNum(int i) {
        this.f271e = i;
    }

    public String toString() {
        return "Name: " + this.f267a + " Coord: " + this.f268b.toString() + " Spell: " + this.f269c + " Code: " + this.f270d + " StationNum: " + this.f271e;
    }
}
