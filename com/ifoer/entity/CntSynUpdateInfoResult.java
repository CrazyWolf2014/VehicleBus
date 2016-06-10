package com.ifoer.entity;

import com.car.result.WSResult;

public class CntSynUpdateInfoResult extends WSResult {
    private CntSynUpdateInfo cntSynUpdateInfo;

    public CntSynUpdateInfo getCntSynUpdateInfo() {
        return this.cntSynUpdateInfo;
    }

    public void setCntSynUpdateInfo(CntSynUpdateInfo cntSynUpdateInfo) {
        this.cntSynUpdateInfo = cntSynUpdateInfo;
    }
}
