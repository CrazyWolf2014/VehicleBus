package com.ifoer.entity;

public class NormalBillInfoDTO {
    private String billContent;
    private int billId;
    private int billType;
    private String invoiceTtile;

    public int getBillId() {
        return this.billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getBillType() {
        return this.billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public String getInvoiceTtile() {
        return this.invoiceTtile;
    }

    public void setInvoiceTtile(String invoiceTtile) {
        this.invoiceTtile = invoiceTtile;
    }

    public String getBillContent() {
        return this.billContent;
    }

    public void setBillContent(String billContent) {
        this.billContent = billContent;
    }
}
