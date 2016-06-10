package com.ifoer.mine;

import org.json.JSONArray;

public class AreaInfoDto {
    private int code;
    private JSONArray jsonArray;
    private String msg;

    public int getCode() {
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

    public JSONArray getJsonArray() {
        return this.jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
