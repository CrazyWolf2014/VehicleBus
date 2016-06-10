package com.ifoer.entity;

import com.car.result.WSResult;
import java.io.Serializable;

public class ProductToUpgradeToInfo extends WSResult implements Serializable {
    private static final long serialVersionUID = -3776887039670697575L;
    private String freeTime;
    private int needRenew;

    public String getFreeTime() {
        return this.freeTime;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    public int getNeedRenew() {
        return this.needRenew;
    }

    public void setNeedRenew(int needRenew) {
        this.needRenew = needRenew;
    }
}
