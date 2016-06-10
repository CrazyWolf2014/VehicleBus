package com.car.result;

import com.ifoer.entity.CntNewsPage;

public class CntNewsPageResult extends WSResult {
    protected CntNewsPage page;

    public CntNewsPage getPage() {
        return this.page;
    }

    public void setPage(CntNewsPage value) {
        this.page = value;
    }
}
