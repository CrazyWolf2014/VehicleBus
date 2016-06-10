package com.ifoer.mine;

import org.json.JSONArray;

public class NationInfoDto {
    String code;
    JSONArray jsonArray;
    String msg;

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

    public JSONArray getJsonArray() {
        return this.jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
