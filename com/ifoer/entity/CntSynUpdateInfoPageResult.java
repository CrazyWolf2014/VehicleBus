package com.ifoer.entity;

import com.car.result.WSResult;

public class CntSynUpdateInfoPageResult extends WSResult {
    private CntSynUpdateInfoPage page;

    public CntSynUpdateInfoPage getPage() {
        return this.page;
    }

    public void setPage(CntSynUpdateInfoPage page) {
        this.page = page;
    }
}
