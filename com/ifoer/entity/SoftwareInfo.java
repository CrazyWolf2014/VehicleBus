package com.ifoer.entity;

public class SoftwareInfo {
    private String amount;
    private boolean ifSelect;
    private String price;
    private String softID;
    private String softName;
    private String type;

    public SoftwareInfo() {
        this.ifSelect = false;
    }

    public String getSoftID() {
        return this.softID;
    }

    public void setSoftID(String softID) {
        this.softID = softID;
    }

    public String getSoftName() {
        return this.softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isIfSelect() {
        return this.ifSelect;
    }

    public void setIfSelect(boolean ifSelect) {
        this.ifSelect = ifSelect;
    }
}
