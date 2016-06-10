package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;
import java.util.List;

public class CountryListResponse extends BaseResponse {
    private static final long serialVersionUID = -2469996449336636625L;
    private List<Country> data;

    public List<Country> getData() {
        return this.data;
    }

    public void setData(List<Country> data) {
        this.data = data;
    }
}
