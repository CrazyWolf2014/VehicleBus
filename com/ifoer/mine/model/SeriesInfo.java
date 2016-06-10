package com.ifoer.mine.model;

import java.io.Serializable;
import java.util.List;

public class SeriesInfo implements Serializable {
    private static final long serialVersionUID = 4256454805013649734L;
    private List<CarInfo> carList;
    private String classId;
    private String className;

    public String getClassId() {
        return this.classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<CarInfo> getCarList() {
        return this.carList;
    }

    public void setCarList(List<CarInfo> carList) {
        this.carList = carList;
    }
}
