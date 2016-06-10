package com.ifoer.mine.model;

import java.io.Serializable;

public class CarInfo implements Serializable {
    private static final long serialVersionUID = -8882537449429688024L;
    private String autoCode;
    private String classId;
    private String className;
    private String softName;

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

    public String getAutoCode() {
        return this.autoCode;
    }

    public void setAutoCode(String autoCode) {
        this.autoCode = autoCode;
    }

    public String getSoftName() {
        return this.softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }
}
