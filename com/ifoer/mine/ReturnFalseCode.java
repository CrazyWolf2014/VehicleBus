package com.ifoer.mine;

import java.io.Serializable;
import org.json.JSONObject;

public class ReturnFalseCode implements Serializable {
    private String code;
    private String data;
    private JSONObject jsonObject;
    private String msg;

    public JSONObject getJsonObject() {
        return this.jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

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

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
