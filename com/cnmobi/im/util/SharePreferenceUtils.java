package com.cnmobi.im.util;

import android.content.Context;
import android.content.SharedPreferences;
import org.xmlpull.v1.XmlPullParser;

public class SharePreferenceUtils {
    SharedPreferences spInfo;

    public SharePreferenceUtils(Context context) {
        this.spInfo = context.getSharedPreferences("user_info", 0);
    }

    public void setIsFirstLogin(boolean isFirstLogin) {
        this.spInfo.edit().putBoolean("isFirstLogin", isFirstLogin).commit();
    }

    public boolean getIsFirstLogin() {
        return this.spInfo.getBoolean("isFirstLogin", true);
    }

    public void setIsLoginUp(boolean isLoginUp) {
        this.spInfo.edit().putBoolean("isLoginUp", isLoginUp).commit();
    }

    public boolean getIsLoginUp() {
        return this.spInfo.getBoolean("isLoginUp", false);
    }

    public void setIsVisitor(boolean isVisitor) {
        this.spInfo.edit().putBoolean("isVisitor", isVisitor).commit();
    }

    public boolean getIsVisitor() {
        return this.spInfo.getBoolean("isVisitor", true);
    }

    public void setIsProtection(boolean protection) {
        this.spInfo.edit().putBoolean("protection", protection).commit();
    }

    public boolean getIsProtection() {
        return this.spInfo.getBoolean("protection", false);
    }

    public void setCityName(String cityName) {
        this.spInfo.edit().putString("cityName", cityName).commit();
    }

    public String getCityName() {
        return this.spInfo.getString("cityName", XmlPullParser.NO_NAMESPACE);
    }

    public void setCityId(String cityId) {
        this.spInfo.edit().putString("cityId", cityId).commit();
    }

    public String getCityId() {
        return this.spInfo.getString("cityId", XmlPullParser.NO_NAMESPACE);
    }

    public void setResidentialName(String residentialName) {
        this.spInfo.edit().putString("residentialName", residentialName).commit();
    }

    public String getResidentialName() {
        return this.spInfo.getString("residentialName", XmlPullParser.NO_NAMESPACE);
    }

    public void setResidentialId(String residentialId) {
        this.spInfo.edit().putString("residentialId", residentialId).commit();
    }

    public String getResidentialId() {
        return this.spInfo.getString("residentialId", XmlPullParser.NO_NAMESPACE);
    }

    public void setBuildName(String buildName) {
        this.spInfo.edit().putString("buildName", buildName).commit();
    }

    public String getBuildName() {
        return this.spInfo.getString("buildName", XmlPullParser.NO_NAMESPACE);
    }

    public void setBuildId(String buildId) {
        this.spInfo.edit().putString("buildId", buildId).commit();
    }

    public String getBuildId() {
        return this.spInfo.getString("buildId", XmlPullParser.NO_NAMESPACE);
    }

    public void setHouseName(String houseName) {
        this.spInfo.edit().putString("houseName", houseName).commit();
    }

    public String getHouseName() {
        return this.spInfo.getString("houseName", XmlPullParser.NO_NAMESPACE);
    }

    public void setHouseId(String houseId) {
        this.spInfo.edit().putString("houseId", houseId).commit();
    }

    public String getHouseId() {
        return this.spInfo.getString("houseId", XmlPullParser.NO_NAMESPACE);
    }

    public void setLoginMode(int loginMode) {
        this.spInfo.edit().putInt("loginMode", loginMode).commit();
    }

    public int getLoginMode() {
        return this.spInfo.getInt("loginMode", 0);
    }

    public void setLoginAccount(String account) {
        this.spInfo.edit().putString("account", account).commit();
    }

    public String getLoginAccount() {
        return this.spInfo.getString("account", XmlPullParser.NO_NAMESPACE);
    }

    public void setCustId(String custId) {
        this.spInfo.edit().putString("custId", custId).commit();
    }

    public String getCustId() {
        return this.spInfo.getString("custId", XmlPullParser.NO_NAMESPACE);
    }

    public void setCustName(String CustName) {
        this.spInfo.edit().putString("CustName", CustName).commit();
    }

    public String getCustName() {
        return this.spInfo.getString("CustName", XmlPullParser.NO_NAMESPACE);
    }

    public void setCertificateId(String certificateId) {
        this.spInfo.edit().putString("certificateId", certificateId).commit();
    }

    public String getCertificateId() {
        return this.spInfo.getString("certificateId", XmlPullParser.NO_NAMESPACE);
    }

    public void setHeadPicName(String headPicName) {
        this.spInfo.edit().putString("headPicName", headPicName).commit();
    }

    public String getHeadPicName() {
        return this.spInfo.getString("headPicName", XmlPullParser.NO_NAMESPACE);
    }

    public void setHeadPicUrl(String headPicUrl) {
        this.spInfo.edit().putString("headPicUrl", headPicUrl).commit();
    }

    public String getHeadPicUrl() {
        return this.spInfo.getString("headPicUrl", XmlPullParser.NO_NAMESPACE);
    }
}
