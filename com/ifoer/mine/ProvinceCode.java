package com.ifoer.mine;

import java.util.List;

public class ProvinceCode {
    private String code;
    private List<ProvinceInfo> list;
    private String msg;

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ProvinceInfo> getList() {
        return this.list;
    }

    public void setList(List<ProvinceInfo> list) {
        this.list = list;
    }
}
