package com.amap.mapapi.route;

import com.amap.mapapi.core.GeoPoint;
import org.xmlpull.v1.XmlPullParser;

public class Segment {
    private GeoPoint f737a;
    private GeoPoint f738b;
    protected int mLength;
    protected Route mRoute;
    protected GeoPoint[] mShapes;
    protected String strTimeConsume;

    public Segment() {
        this.f737a = null;
        this.f738b = null;
        this.strTimeConsume = XmlPullParser.NO_NAMESPACE;
    }

    public String getConsumeTime() {
        return this.strTimeConsume;
    }

    public void setConsumeTime(String str) {
        this.strTimeConsume = str;
    }

    private void m886a() {
        int i = Integer.MAX_VALUE;
        int i2 = Integer.MIN_VALUE;
        GeoPoint[] geoPointArr = this.mShapes;
        int length = geoPointArr.length;
        int i3 = 0;
        int i4 = Integer.MIN_VALUE;
        int i5 = Integer.MAX_VALUE;
        while (i3 < length) {
            GeoPoint geoPoint = geoPointArr[i3];
            int longitudeE6 = geoPoint.getLongitudeE6();
            int latitudeE6 = geoPoint.getLatitudeE6();
            if (longitudeE6 > i4) {
                i4 = longitudeE6;
            }
            if (longitudeE6 >= i5) {
                longitudeE6 = i5;
            }
            if (latitudeE6 > i2) {
                i2 = latitudeE6;
            }
            if (latitudeE6 >= i) {
                latitudeE6 = i;
            }
            i3++;
            i5 = longitudeE6;
            i = latitudeE6;
        }
        this.f737a = new GeoPoint(i, i5);
        this.f738b = new GeoPoint(i2, i4);
    }

    public GeoPoint getLowerLeftPoint() {
        if (this.f737a == null) {
            m886a();
        }
        return this.f737a;
    }

    public GeoPoint getUpperRightPoint() {
        if (this.f738b == null) {
            m886a();
        }
        return this.f738b;
    }

    public int getLength() {
        return this.mLength;
    }

    public void setLength(int i) {
        this.mLength = i;
    }

    public void setRoute(Route route) {
        this.mRoute = route;
    }

    public void setShapes(GeoPoint[] geoPointArr) {
        this.mShapes = geoPointArr;
    }

    public GeoPoint[] getShapes() {
        return this.mShapes;
    }

    private int m887b() {
        int segmentIndex = this.mRoute.getSegmentIndex(this);
        if (segmentIndex >= 0) {
            return segmentIndex;
        }
        throw new IllegalArgumentException("this segment is not in the route !");
    }

    public Segment getPrev() {
        int b = m887b();
        return b == 0 ? null : this.mRoute.getStep(b - 1);
    }

    public Segment getNext() {
        int b = m887b();
        return b == this.mRoute.getStepCount() + -1 ? null : this.mRoute.getStep(b + 1);
    }

    public GeoPoint getFirstPoint() {
        return this.mShapes[0];
    }

    public GeoPoint getLastPoint() {
        return this.mShapes[this.mShapes.length - 1];
    }
}
