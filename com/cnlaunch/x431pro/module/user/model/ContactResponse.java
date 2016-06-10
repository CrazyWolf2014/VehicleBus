package com.cnlaunch.x431pro.module.user.model;

import com.cnlaunch.x431pro.module.base.BaseResponse;

public class ContactResponse extends BaseResponse {
    private static final long serialVersionUID = 2716504870139991101L;
    private ContactData data;

    public ContactData getData() {
        return this.data;
    }

    public void setData(ContactData data) {
        this.data = data;
    }
}
