package com.ifoer.entity;

import java.util.ArrayList;
import java.util.List;

public class SptDataStreamId {
    private List<SptDataStreamIdItem> list;
    private int streamNum;

    public SptDataStreamId() {
        this.list = new ArrayList();
    }

    public int getStreamNum() {
        return this.streamNum;
    }

    public void setStreamNum(int streamNum) {
        this.streamNum = streamNum;
    }

    public List<SptDataStreamIdItem> getList() {
        return this.list;
    }

    public void setList(List<SptDataStreamIdItem> list) {
        this.list = list;
    }
}
