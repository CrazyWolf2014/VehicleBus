package com.ifoer.entity;

import com.car.result.WSResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConcernedProductInfoResult extends WSResult implements Serializable {
    private String VIN;
    private String carBrandId;
    private String carBrandName;
    private String concernBeginTime;
    private String currentMileage;
    private String customerMobile;
    private String customerName;
    private List<Map<String, String>> downloadFunMap;
    private String isConfigured;
    private String nextMaintanceMileage;
    private String seriaNo;

    public ConcernedProductInfoResult() {
        this.downloadFunMap = new ArrayList();
    }

    public List<Map<String, String>> getDownloadFunMap() {
        return this.downloadFunMap;
    }

    public void setDownloadFunMap(List<Map<String, String>> downloadFunMap) {
        this.downloadFunMap = downloadFunMap;
    }

    public String getSeriaNo() {
        return this.seriaNo;
    }

    public void setSeriaNo(String seriaNo) {
        this.seriaNo = seriaNo;
    }

    public String getIsConfigured() {
        return this.isConfigured;
    }

    public void setIsConfigured(String isConfigured) {
        this.isConfigured = isConfigured;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return this.customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getNextMaintanceMileage() {
        return this.nextMaintanceMileage;
    }

    public void setNextMaintanceMileage(String nextMaintanceMileage) {
        this.nextMaintanceMileage = nextMaintanceMileage;
    }

    public String getCurrentMileage() {
        return this.currentMileage;
    }

    public void setCurrentMileage(String currentMileage) {
        this.currentMileage = currentMileage;
    }

    public String getVIN() {
        return this.VIN;
    }

    public void setVIN(String vIN) {
        this.VIN = vIN;
    }

    public String getCarBrandId() {
        return this.carBrandId;
    }

    public void setCarBrandId(String carBrandId) {
        this.carBrandId = carBrandId;
    }

    public String getCarBrandName() {
        return this.carBrandName;
    }

    public void setCarBrandName(String carBrandName) {
        this.carBrandName = carBrandName;
    }

    public String getConcernBeginTime() {
        return this.concernBeginTime;
    }

    public void setConcernBeginTime(String concernBeginTime) {
        this.concernBeginTime = concernBeginTime;
    }
}
