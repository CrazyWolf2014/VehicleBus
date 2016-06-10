package com.ifoer.entity;

import com.car.result.WSResult;

public class PushMessageCountResult extends WSResult {
    private Integer amount;
    private Integer resolvedCount;
    private Integer unResolvedCount;

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getResolvedCount() {
        return this.resolvedCount;
    }

    public void setResolvedCount(Integer resolvedCount) {
        this.resolvedCount = resolvedCount;
    }

    public Integer getUnResolvedCount() {
        return this.unResolvedCount;
    }

    public void setUnResolvedCount(Integer unResolvedCount) {
        this.unResolvedCount = unResolvedCount;
    }
}
