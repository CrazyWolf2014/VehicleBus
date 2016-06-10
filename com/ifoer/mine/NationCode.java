package com.ifoer.mine;

import java.util.List;
import org.json.JSONObject;

public class NationCode {
    private String code;
    private JSONObject jsonObject;
    private List<NationCodeInfo> list;
    private String msg;

    public JSONObject getJsonObject() {
        return this.jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public List<NationCodeInfo> getList() {
        return this.list;
    }

    public void setList(List<NationCodeInfo> list) {
        this.list = list;
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
}
