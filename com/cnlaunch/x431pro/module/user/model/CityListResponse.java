package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;
import java.util.List;

public class CityListResponse extends BaseResponse {
    private static final long serialVersionUID = 1866613780308912119L;
    private List<City> data;

    public List<City> getData() {
        return this.data;
    }

    public void setData(List<City> data) {
        this.data = data;
    }
}
