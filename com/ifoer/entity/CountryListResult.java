package com.ifoer.entity;

import com.ifoer.mine.model.BaseCode;
import java.io.Serializable;
import java.util.ArrayList;

public class CountryListResult extends BaseCode implements Serializable {
    private static final long serialVersionUID = -4519761338818655256L;
    private ArrayList<CountryInfo> datas;

    public CountryListResult() {
        this.datas = new ArrayList();
    }

    public ArrayList<CountryInfo> getDatas() {
        return this.datas;
    }

    public void setDatas(ArrayList<CountryInfo> datas) {
        this.datas = datas;
    }
}
