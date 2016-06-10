package com.ifoer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PagingHelper implements Serializable {
    private static final long serialVersionUID = 1;
    private List<PushMessageDTO> dataList;
    private int pageNo;
    private int pagesize;
    private int size;

    public PagingHelper() {
        this.dataList = new ArrayList();
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPagesize() {
        return this.pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<PushMessageDTO> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<PushMessageDTO> dataList) {
        this.dataList = dataList;
    }
}
