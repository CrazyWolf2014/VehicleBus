package com.ifoer.entity;

import java.util.ArrayList;
import java.util.List;

public class CntNewsPage {
    protected List<CntSynNews> dataList;
    protected int pageNo;
    protected int pageSize;
    protected int size;

    public CntNewsPage() {
        this.dataList = new ArrayList();
    }

    public List<CntSynNews> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<CntSynNews> dataList) {
        this.dataList = dataList;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int value) {
        this.size = value;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int value) {
        this.pageSize = value;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int value) {
        this.pageNo = value;
    }
}
