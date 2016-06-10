package com.ifoer.entity;

import com.car.result.WSResult;
import java.util.List;

public class DrivingInfoResult extends WSResult {
    private String currentTime;
    private List<RCUDataDTO> dataList;
    private String gpsTime;
    private String serverTime;

    public String getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getGpsTime() {
        return this.gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public String getServerTime() {
        return this.serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public List<RCUDataDTO> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<RCUDataDTO> dataList) {
        this.dataList = dataList;
    }
}
