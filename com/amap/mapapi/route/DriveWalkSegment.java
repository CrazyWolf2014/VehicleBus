package com.amap.mapapi.route;

public class DriveWalkSegment extends Segment {
    public static final int NoAction = -1;
    protected int mActionCode;
    protected String mActionDes;
    protected String mRoadName;

    public String getActionDescription() {
        return this.mActionDes;
    }

    public void setActionDescription(String str) {
        this.mActionDes = str;
    }

    public int getActionCode() {
        return this.mActionCode;
    }

    public void setActionCode(int i) {
        this.mActionCode = i;
    }

    public String getRoadName() {
        return this.mRoadName;
    }

    public void setRoadName(String str) {
        this.mRoadName = str;
    }
}
