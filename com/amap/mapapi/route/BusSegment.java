package com.amap.mapapi.route;

import com.amap.mapapi.core.GeoPoint;

public class BusSegment extends Segment {
    protected String mFirstStation;
    protected String mLastStation;
    protected String mLine;
    protected String[] mPassStopName;
    protected GeoPoint[] mPassStopPos;

    public int getStopNumber() {
        return this.mPassStopPos.length;
    }

    public void setPassStopPos(GeoPoint[] geoPointArr) {
        this.mPassStopPos = geoPointArr;
    }

    public String getLineName() {
        return this.mLine;
    }

    public void setLineName(String str) {
        this.mLine = str;
    }

    public String getFirstStationName() {
        return this.mFirstStation;
    }

    public void setFirstStationName(String str) {
        this.mFirstStation = str;
    }

    public String getLastStationName() {
        return this.mLastStation;
    }

    public void setLastStationName(String str) {
        this.mLastStation = str;
    }

    public String getOnStationName() {
        return this.mPassStopName[0];
    }

    public String[] getPassStopName() {
        return this.mPassStopName;
    }

    public void setPassStopName(String[] strArr) {
        this.mPassStopName = strArr;
    }

    public String getOffStationName() {
        return this.mPassStopName[this.mPassStopName.length - 1];
    }
}
