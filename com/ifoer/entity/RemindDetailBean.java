package com.ifoer.entity;

import java.io.Serializable;

public class RemindDetailBean implements Serializable {
    private static final long serialVersionUID = 1;
    private String alarmType;
    private String content;
    private String id;
    private int isRead;
    private String msg;
    private String sItemId;
    private String serialNo;
    private String technicianId;
    private String time;

    public RemindDetailBean(String id, String sItemId, String alarmType, String msg, String content, String serialNo, String technicianId, int isRead, String time) {
        this.id = id;
        this.sItemId = sItemId;
        this.alarmType = alarmType;
        this.msg = msg;
        this.content = content;
        this.serialNo = serialNo;
        this.technicianId = technicianId;
        this.isRead = isRead;
        this.time = time;
    }

    public String getsItemId() {
        return this.sItemId;
    }

    public void setsItemId(String sItemId) {
        this.sItemId = sItemId;
    }

    public String getAlarmType() {
        return this.alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSerialNo() {
        return this.serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTechnicianId() {
        return this.technicianId;
    }

    public void setTechnicianId(String technicianId) {
        this.technicianId = technicianId;
    }

    public int getIsRead() {
        return this.isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
