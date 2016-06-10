package com.ifoer.dbentity;

import java.util.ArrayList;
import java.util.List;

public class SerialNumber {
    private List<Car> carList;
    private String carPath;
    private String serialNum;

    public SerialNumber() {
        this.carList = new ArrayList();
    }

    public String getCarPath() {
        return this.carPath;
    }

    public void setCarPath(String carPath) {
        this.carPath = carPath;
    }

    public String getSerialNum() {
        return this.serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public List<Car> getCarList() {
        return this.carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }
}
