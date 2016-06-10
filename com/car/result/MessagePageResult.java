package com.car.result;

import com.ifoer.entity.CircleMessageDTO;
import java.util.List;

public class MessagePageResult extends WSResult {
    private List<CircleMessageDTO> dataList;
    private int pageNo;
    private int pageSize;
    private int size;

    public List<CircleMessageDTO> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<CircleMessageDTO> dataList) {
        this.dataList = dataList;
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
}
