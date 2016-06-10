package com.ifoer.entity;

import java.io.Serializable;

public class SptExDataStreamIdItem34 implements Serializable {
    private static final long serialVersionUID = 1;
    private byte[] choseMaskBufferList;
    private int dataStreamHandelItemCount;
    private int dataStreamItemCount;

    public SptExDataStreamIdItem34() {
        this.dataStreamItemCount = 0;
        this.dataStreamHandelItemCount = 0;
        this.choseMaskBufferList = new byte[0];
    }

    public int getDataStreamItemCount() {
        return this.dataStreamItemCount;
    }

    public void setDataStreamItemCount(int dataStreamItemCount) {
        this.dataStreamItemCount = dataStreamItemCount;
    }

    public int getDataStreamHandelItemCount() {
        return this.dataStreamHandelItemCount;
    }

    public void setDataStreamHandelItemCount(int dataStreamHandelItemCount) {
        this.dataStreamHandelItemCount = dataStreamHandelItemCount;
    }

    public byte[] getChoseMaskBufferList() {
        return this.choseMaskBufferList;
    }

    public void setChoseMaskBufferList(byte[] choseMaskBufferList) {
        this.choseMaskBufferList = choseMaskBufferList;
    }
}
