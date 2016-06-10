package com.ifoer.entity;

import java.io.Serializable;

public class PageInteractiveData implements Serializable {
    private static final long serialVersionUID = -4713433694170845908L;
    private byte[] data;
    private byte packageId;
    private int packageType;

    public byte getPackageId() {
        return this.packageId;
    }

    public void setPackageId(byte packageId) {
        this.packageId = packageId;
    }

    public int getPackageType() {
        return this.packageType;
    }

    public void setPackageType(int packageType) {
        this.packageType = packageType;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
