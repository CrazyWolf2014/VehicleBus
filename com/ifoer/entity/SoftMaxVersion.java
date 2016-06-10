package com.ifoer.entity;

import com.car.result.WSResult;
import java.io.Serializable;

public class SoftMaxVersion extends WSResult implements Serializable {
    private static final long serialVersionUID = 1;
    private int forceUpgrade;
    private String softDescription;
    private int versionDetailId;
    private String versionNo;

    public int getVersionDetailId() {
        return this.versionDetailId;
    }

    public void setVersionDetailId(int versionDetailId) {
        this.versionDetailId = versionDetailId;
    }

    public String getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getSoftDescription() {
        return this.softDescription;
    }

    public void setSoftDescription(String softDescription) {
        this.softDescription = softDescription;
    }

    public int getForceUpgrade() {
        return this.forceUpgrade;
    }

    public void setForceUpgrade(int forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }
}
