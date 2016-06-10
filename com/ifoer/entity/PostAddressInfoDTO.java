package com.ifoer.entity;

public class PostAddressInfoDTO {
    private int addressId;
    private String postAddress;
    private String receiver;
    private String telephone;
    private String zipCode;

    public int getAddressId() {
        return this.addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getPostAddress() {
        return this.postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
