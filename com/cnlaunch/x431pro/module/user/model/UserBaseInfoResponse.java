package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class UserBaseInfoResponse extends BaseResponse {
    private static final long serialVersionUID = -672361565600968899L;
    private UserBaseInfo data;

    public UserBaseInfo getData() {
        return this.data;
    }

    public void setData(UserBaseInfo data) {
        this.data = data;
    }
}
