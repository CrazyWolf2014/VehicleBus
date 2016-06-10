package com.ifoer.mine.model;

import java.io.Serializable;
import java.util.List;

public class Professional implements Serializable {
    private static final long serialVersionUID = 7764634932249244907L;
    private String autoCode;
    private String classId;
    private String className;
    private String softName;
    private String system;
    private List<String> systemList;

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

    public String getSoftName() {
        return this.softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getAutoCode() {
        return this.autoCode;
    }

    public void setAutoCode(String autoCode) {
        this.autoCode = autoCode;
    }

    public String getSystem() {
        return this.system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public List<String> getSystemList() {
        return this.systemList;
    }

    public void setSystemList(List<String> systemList) {
        this.systemList = systemList;
    }
}
