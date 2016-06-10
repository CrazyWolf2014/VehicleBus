package com.ifoer.mine;

import org.json.JSONObject;

public class InfoDto {
    private int code;
    private JSONObject jsonObject;
    private String msg;

    public int getCode() {
        return this.code;
    }

    public int setContactInfo() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getJsonObject() {
        return this.jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
