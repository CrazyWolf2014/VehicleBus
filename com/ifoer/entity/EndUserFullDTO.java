package com.ifoer.entity;

import java.io.Serializable;
import org.xmlpull.v1.XmlPullParser;

public class EndUserFullDTO implements Serializable {
    private static final long serialVersionUID = 1;
    protected String address;
    protected long birthdays;
    protected String city;
    protected String companyName;
    protected String continent;
    protected String country;
    protected String drivingLicense;
    protected String email;
    protected String familyPhone;
    protected String firstName;
    protected String issueDate;
    protected String lastName;
    protected String latitude;
    protected String longitude;
    protected String markAddress;
    protected String mobile;
    protected String nickName;
    protected String officePhone;
    protected String province;
    protected Integer sex;
    protected Integer userId;
    protected String userName;
    protected Integer userTypeId;
    protected String zipCode;

    public EndUserFullDTO() {
        this.address = XmlPullParser.NO_NAMESPACE;
        this.birthdays = 0;
        this.city = XmlPullParser.NO_NAMESPACE;
        this.companyName = XmlPullParser.NO_NAMESPACE;
        this.continent = XmlPullParser.NO_NAMESPACE;
        this.country = XmlPullParser.NO_NAMESPACE;
        this.drivingLicense = XmlPullParser.NO_NAMESPACE;
        this.email = XmlPullParser.NO_NAMESPACE;
        this.familyPhone = XmlPullParser.NO_NAMESPACE;
        this.firstName = XmlPullParser.NO_NAMESPACE;
        this.issueDate = XmlPullParser.NO_NAMESPACE;
        this.lastName = XmlPullParser.NO_NAMESPACE;
        this.latitude = XmlPullParser.NO_NAMESPACE;
        this.longitude = XmlPullParser.NO_NAMESPACE;
        this.markAddress = XmlPullParser.NO_NAMESPACE;
        this.mobile = XmlPullParser.NO_NAMESPACE;
        this.nickName = XmlPullParser.NO_NAMESPACE;
        this.officePhone = XmlPullParser.NO_NAMESPACE;
        this.province = XmlPullParser.NO_NAMESPACE;
        this.sex = Integer.valueOf(0);
        this.userId = Integer.valueOf(0);
        this.userTypeId = Integer.valueOf(0);
        this.zipCode = XmlPullParser.NO_NAMESPACE;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getCity() {
        return this.city;
    }

    public long getBirthdays() {
        return this.birthdays;
    }

    public void setBirthdays(long birthdays) {
        this.birthdays = birthdays;
    }

    public void setCity(String value) {
        this.city = value;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String value) {
        this.companyName = value;
    }

    public String getContinent() {
        return this.continent;
    }

    public void setContinent(String value) {
        this.continent = value;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String value) {
        this.country = value;
    }

    public String getDrivingLicense() {
        return this.drivingLicense;
    }

    public void setDrivingLicense(String value) {
        this.drivingLicense = value;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String value) {
        this.email = value;
    }

    public String getFamilyPhone() {
        return this.familyPhone;
    }

    public void setFamilyPhone(String value) {
        this.familyPhone = value;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String value) {
        this.firstName = value;
    }

    public String getIssueDate() {
        return this.issueDate;
    }

    public void setIssueDate(String value) {
        this.issueDate = value;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String value) {
        this.lastName = value;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String value) {
        this.latitude = value;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String value) {
        this.longitude = value;
    }

    public String getMarkAddress() {
        return this.markAddress;
    }

    public void setMarkAddress(String value) {
        this.markAddress = value;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String value) {
        this.mobile = value;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String value) {
        this.nickName = value;
    }

    public String getOfficePhone() {
        return this.officePhone;
    }

    public void setOfficePhone(String value) {
        this.officePhone = value;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String value) {
        this.province = value;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer value) {
        this.sex = value;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer value) {
        this.userId = value;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String value) {
        this.userName = value;
    }

    public Integer getUserTypeId() {
        return this.userTypeId;
    }

    public void setUserTypeId(Integer value) {
        this.userTypeId = value;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String value) {
        this.zipCode = value;
    }
}
