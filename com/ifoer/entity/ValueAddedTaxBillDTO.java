package com.ifoer.entity;

import java.io.Serializable;

public class ValueAddedTaxBillDTO implements Serializable {
    private String bankAccountNum;
    private String bankName;
    private String billContent;
    private int billType;
    private String invoiceTtile;
    private String regAddress;
    private String regTelephone;
    private String taxCode;

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

    public String getTaxCode() {
        return this.taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getRegAddress() {
        return this.regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getRegTelephone() {
        return this.regTelephone;
    }

    public void setRegTelephone(String regTelephone) {
        this.regTelephone = regTelephone;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNum() {
        return this.bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }
}
