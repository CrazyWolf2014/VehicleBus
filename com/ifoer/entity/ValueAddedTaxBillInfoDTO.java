package com.ifoer.entity;

public class ValueAddedTaxBillInfoDTO {
    private String bankAccountNum;
    private String bankName;
    private String billContent;
    private int billId;
    private String invoiceTitle;
    private String regAddress;
    private String regTelephone;
    private String taxCode;

    public int getBillId() {
        return this.billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getInvoiceTitle() {
        return this.invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
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
