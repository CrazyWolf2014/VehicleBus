package com.ifoer.mine.model;

public class GetTechInfoResult extends BaseCode {
    private static final long serialVersionUID = -2008061971094396329L;
    private TechInfoData data;

    public TechInfoData getData() {
        return this.data;
    }

    public void setData(TechInfoData data) {
        this.data = data;
    }
}
