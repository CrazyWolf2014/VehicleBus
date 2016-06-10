package com.ifoer.entity;

import java.io.Serializable;

public class VIPWarnReport implements Serializable {
    private static final long serialVersionUID = 1;
    private String alarm_type;
    private String carType;
    private String content;
    private String id;
    private Boolean isWarn;
    private int is_read;
    private String item_id;
    private String msg;
    private String serial_no;
    private String tag;
    private String technician_id;
    private String time;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return this.item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getAlarm_type() {
        return this.alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSerial_no() {
        return this.serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getTechnician_id() {
        return this.technician_id;
    }

    public void setTechnician_id(String technician_id) {
        this.technician_id = technician_id;
    }

    public Boolean getIsWarn() {
        return this.isWarn;
    }

    public void setIsWarn(Boolean isWarn) {
        this.isWarn = isWarn;
    }

    public String getCarType() {
        return this.carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIs_read() {
        return this.is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }
}
