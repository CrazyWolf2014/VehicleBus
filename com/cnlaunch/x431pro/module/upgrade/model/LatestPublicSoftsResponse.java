package com.cnlaunch.x431pro.module.upgrade.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;
import java.util.List;

public class LatestPublicSoftsResponse extends BaseResponse {
    private static final long serialVersionUID = 238013042459863126L;
    private List<X431PadDtoSoft> x431PadSoftList;

    public List<X431PadDtoSoft> getX431PadSoftList() {
        return this.x431PadSoftList;
    }

    public void setX431PadSoftList(List<X431PadDtoSoft> x431PadSoftList) {
        this.x431PadSoftList = x431PadSoftList;
    }
}
