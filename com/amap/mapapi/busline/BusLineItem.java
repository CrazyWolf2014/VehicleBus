package com.amap.mapapi.busline;

import com.amap.mapapi.core.GeoPoint;
import java.util.ArrayList;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class BusLineItem {
    private ArrayList<BusStationItem> f228A;
    private GeoPoint f229B;
    private GeoPoint f230C;
    private float f231a;
    private String f232b;
    private int f233c;
    private String f234d;
    private int f235e;
    private float f236f;
    private ArrayList<GeoPoint> f237g;
    private String f238h;
    private String f239i;
    private String f240j;
    private String f241k;
    private String f242l;
    private String f243m;
    private String f244n;
    private float f245o;
    private float f246p;
    private boolean f247q;
    private boolean f248r;
    private boolean f249s;
    private boolean f250t;
    private boolean f251u;
    private int f252v;
    private boolean f253w;
    private String f254x;
    private String f255y;
    private boolean f256z;

    public BusLineItem() {
        this.f229B = null;
        this.f230C = null;
    }

    private void m428a() {
        int i = Integer.MAX_VALUE;
        int i2 = Integer.MIN_VALUE;
        int i3 = Integer.MAX_VALUE;
        int i4 = Integer.MIN_VALUE;
        for (int i5 = 0; i5 < this.f237g.size(); i5++) {
            GeoPoint geoPoint = (GeoPoint) this.f237g.get(i5);
            int longitudeE6 = geoPoint.getLongitudeE6();
            int latitudeE6 = geoPoint.getLatitudeE6();
            if (longitudeE6 < i3) {
                i3 = longitudeE6;
            }
            if (latitudeE6 < i) {
                i = latitudeE6;
            }
            if (longitudeE6 > i2) {
                i2 = longitudeE6;
            }
            if (latitudeE6 > i4) {
                i4 = latitudeE6;
            }
        }
        this.f229B = new GeoPoint(i, i3);
        this.f230C = new GeoPoint(i4, i2);
    }

    public GeoPoint getLowerLeftPoint() {
        if (this.f229B == null) {
            m428a();
        }
        return this.f229B;
    }

    public GeoPoint getUpperRightPoint() {
        if (this.f230C == null) {
            m428a();
        }
        return this.f230C;
    }

    public float getmLength() {
        return this.f231a;
    }

    public void setmLength(float f) {
        this.f231a = f;
    }

    public String getmName() {
        return this.f232b;
    }

    public void setmName(String str) {
        this.f232b = str;
    }

    public int getmType() {
        return this.f233c;
    }

    public void setmType(int i) {
        this.f233c = i;
    }

    public String getmDescription() {
        return this.f234d;
    }

    public void setmDescription(String str) {
        this.f234d = str;
    }

    public int getmStatus() {
        return this.f235e;
    }

    public void setmStatus(int i) {
        this.f235e = i;
    }

    public float getmSpeed() {
        return this.f236f;
    }

    public void setmSpeed(float f) {
        this.f236f = f;
    }

    public ArrayList<GeoPoint> getmXys() {
        return this.f237g;
    }

    public void setmXys(ArrayList<GeoPoint> arrayList) {
        this.f237g = arrayList;
    }

    public String getmLineId() {
        return this.f238h;
    }

    public void setmLineId(String str) {
        this.f238h = str;
    }

    public String getmKeyName() {
        return this.f239i;
    }

    public void setmKeyName(String str) {
        this.f239i = str;
    }

    public String getmFrontName() {
        return this.f240j;
    }

    public void setmFrontName(String str) {
        this.f240j = str;
    }

    public String getmTerminalName() {
        return this.f241k;
    }

    public void setmTerminalName(String str) {
        this.f241k = str;
    }

    public String getmStartTime() {
        return this.f242l;
    }

    public void setmStartTime(String str) {
        this.f242l = str;
    }

    public String getmEndTime() {
        return this.f243m;
    }

    public void setmEndTime(String str) {
        this.f243m = str;
    }

    public String getmCompany() {
        return this.f244n;
    }

    public void setmCompany(String str) {
        this.f244n = str;
    }

    public float getmBasicPrice() {
        return this.f245o;
    }

    public void setmBasicPrice(float f) {
        this.f245o = f;
    }

    public float getmTotalPrice() {
        return this.f246p;
    }

    public void setmTotalPrice(float f) {
        this.f246p = f;
    }

    public boolean getmCommunicationTicket() {
        return this.f247q;
    }

    public void setmCommunicationTicket(boolean z) {
        this.f247q = z;
    }

    public boolean getmAuto() {
        return this.f248r;
    }

    public void setmAuto(boolean z) {
        this.f248r = z;
    }

    public boolean ismIcCard() {
        return this.f249s;
    }

    public void setmIcCard(boolean z) {
        this.f249s = z;
    }

    public boolean ismLoop() {
        return this.f250t;
    }

    public void setmLoop(boolean z) {
        this.f250t = z;
    }

    public boolean ismDoubleDeck() {
        return this.f251u;
    }

    public void setmDoubleDeck(boolean z) {
        this.f251u = z;
    }

    public int getmDataSource() {
        return this.f252v;
    }

    public void setmDataSource(int i) {
        this.f252v = i;
    }

    public boolean getmAir() {
        return this.f253w;
    }

    public void setmAir(boolean z) {
        this.f253w = z;
    }

    public String getmFrontSpell() {
        return this.f254x;
    }

    public void setmFrontSpell(String str) {
        this.f254x = str;
    }

    public String getmTerminalSpell() {
        return this.f255y;
    }

    public void setmTerminalSpell(String str) {
        this.f255y = str;
    }

    public boolean ismExpressWay() {
        return this.f256z;
    }

    public void setmExpressWay(boolean z) {
        this.f256z = z;
    }

    public ArrayList<BusStationItem> getmStations() {
        return this.f228A;
    }

    public void setmStations(ArrayList<BusStationItem> arrayList) {
        this.f228A = arrayList;
    }

    public String toString() {
        return this.f232b + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + this.f242l + "-" + this.f243m;
    }
}
