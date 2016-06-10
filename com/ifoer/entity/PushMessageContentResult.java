package com.ifoer.entity;

import com.car.result.WSResult;

public class PushMessageContentResult extends WSResult {
    private String detailContent;

    public String getDetailContent() {
        return this.detailContent;
    }

    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }
}
