package com.ifoer.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class SptDataStreamIdEx implements Serializable {
    private byte[] choseMaskBufferList;
    private int dataStreamHandelItemCount;
    private ArrayList<String> dataStreamIDList;
    private int dataStreamItemCount;
    private String dataStreamLabel;
    private ArrayList<String> dataStreamUnitList;
    private ArrayList<String> dataStreamValueList;

    public SptDataStreamIdEx() {
        this.dataStreamItemCount = 0;
        this.dataStreamLabel = "111";
        this.dataStreamHandelItemCount = 0;
        this.choseMaskBufferList = new byte[0];
        this.dataStreamIDList = new ArrayList();
        this.dataStreamValueList = new ArrayList();
        this.dataStreamUnitList = new ArrayList();
    }

    public int getDataStreamItemCount() {
        return this.dataStreamItemCount;
    }

    public void setDataStreamItemCount(int dataStreamItemCount) {
        this.dataStreamItemCount = dataStreamItemCount;
    }

    public byte[] getChoseMaskBufferList() {
        return this.choseMaskBufferList;
    }

    public void setChoseMaskBufferList(byte[] choseMaskBufferList) {
        this.choseMaskBufferList = choseMaskBufferList;
    }

    public String getDataStreamLabel() {
        return this.dataStreamLabel;
    }

    public void setDataStreamLabel(String dataStreamLabel) {
        this.dataStreamLabel = dataStreamLabel;
    }

    public int getDataStreamHandelItemCount() {
        return this.dataStreamHandelItemCount;
    }

    public void setDataStreamHandelItemCount(int dataStreamHandelItemCount) {
        this.dataStreamHandelItemCount = dataStreamHandelItemCount;
    }

    public ArrayList<String> getDataStreamValueList() {
        return this.dataStreamValueList;
    }

    public void setDataStreamValueList(ArrayList<String> dataStreamValueList) {
        this.dataStreamValueList = dataStreamValueList;
    }

    public ArrayList<String> getDataStreamIDList() {
        return this.dataStreamIDList;
    }

    public void setDataStreamIDList(ArrayList<String> dataStreamIDList) {
        this.dataStreamIDList = dataStreamIDList;
    }

    public ArrayList<String> getDataStreamUnitList() {
        return this.dataStreamUnitList;
    }

    public void setDataStreamUnitList(ArrayList<String> dataStreamUnitList) {
        this.dataStreamUnitList = dataStreamUnitList;
    }
}
