package com.ifoer.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class RemindBean implements Serializable {
    private static final long serialVersionUID = 1;
    private int code;
    private ArrayList<RemindDetailBean> datas;
    private String msg;
    private int totalSize;
    private int unReadSize;

    public RemindBean() {
        this.datas = new ArrayList();
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<RemindDetailBean> getDatas() {
        return this.datas;
    }

    public void setDatas(ArrayList<RemindDetailBean> datas) {
        this.datas = datas;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getUnReadSize() {
        return this.unReadSize;
    }

    public void setUnReadSize(int unReadSize) {
        this.unReadSize = unReadSize;
    }

    public String toString() {
        return "RemindBean [msg=" + this.msg + ", code=" + this.code + ", totalSize=" + this.totalSize + ", unReadSize=" + this.unReadSize + ", datas=" + this.datas + "]";
    }
}
