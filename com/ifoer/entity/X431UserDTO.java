package com.ifoer.entity;

public class X431UserDTO {
    private String address;
    private long birthdays;
    private String companyName;
    private String email;
    private String familyPhone;
    private String fax;
    private String firstName;
    private String lastName;
    private String mobile;
    private String officePhone;
    private int sex;
    private String userName;
    private String zipCode;

    public X431UserDTO(String userName, String email, String mobile, String firstName, String lastName, int sex, long birthdays, String familyPhone, String officePhone, String fax, String companyName, String address, String zipCode) {
        this.userName = userName;
        this.email = email;
        this.mobile = mobile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.birthdays = birthdays;
        this.familyPhone = familyPhone;
        this.officePhone = officePhone;
        this.fax = fax;
        this.companyName = companyName;
        this.address = address;
        this.zipCode = zipCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public long getBirthdays() {
        return this.birthdays;
    }

    public void setBirthdays(long birthdays) {
        this.birthdays = birthdays;
    }

    public String getFamilyPhone() {
        return this.familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getOfficePhone() {
        return this.officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
