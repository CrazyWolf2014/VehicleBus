package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;
import java.util.List;

public class ProvinceListResponse extends BaseResponse {
    private static final long serialVersionUID = 1784773097294448681L;
    private List<Province> data;

    public List<Province> getData() {
        return this.data;
    }

    public void setData(List<Province> data) {
        this.data = data;
    }
}
