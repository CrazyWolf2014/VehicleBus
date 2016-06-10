package com.ifoer.entity;

import java.util.ArrayList;
import java.util.List;

public class CntSynUpdateInfoPage {
    protected List<CntSynUpdateInfo> dataList;
    protected int pageNo;
    protected int pageSize;
    protected int size;

    public CntSynUpdateInfoPage() {
        this.dataList = new ArrayList();
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<CntSynUpdateInfo> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<CntSynUpdateInfo> dataList) {
        this.dataList = dataList;
    }
}
