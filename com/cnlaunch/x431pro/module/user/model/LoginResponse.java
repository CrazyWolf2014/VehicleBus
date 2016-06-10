package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class LoginResponse extends BaseResponse {
    private static final long serialVersionUID = 7737588270970082146L;
    private LoginData data;

    public LoginData getData() {
        return this.data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }
}
