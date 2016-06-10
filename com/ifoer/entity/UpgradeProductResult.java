package com.ifoer.entity;

import com.car.result.WSResult;
import java.io.Serializable;

public class UpgradeProductResult extends WSResult implements Serializable {
    private static final long serialVersionUID = -3913090890107357819L;
    private String freeEndTime;
    private String oldFreeEndTime;
    private String serial;
    private String updateDate;

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getOldFreeEndTime() {
        return this.oldFreeEndTime;
    }

    public void setOldFreeEndTime(String oldFreeEndTime) {
        this.oldFreeEndTime = oldFreeEndTime;
    }

    public String getFreeEndTime() {
        return this.freeEndTime;
    }

    public void setFreeEndTime(String freeEndTime) {
        this.freeEndTime = freeEndTime;
    }
}
