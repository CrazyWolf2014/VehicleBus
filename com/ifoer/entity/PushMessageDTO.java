package com.ifoer.entity;

import java.io.Serializable;

public class PushMessageDTO implements Serializable {
    private static final long serialVersionUID = 1;
    private String concernedSN;
    private String faultType;
    private String language;
    private String messageDesc;
    private String messageId;
    private String messageTitle;
    private String messageUrl;
    private String pushTime;
    private String resolvedFlag;
    private String vehicleType;
    private String versions;

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getResolvedFlag() {
        return this.resolvedFlag;
    }

    public void setResolvedFlag(String resolvedFlag) {
        this.resolvedFlag = resolvedFlag;
    }

    public String getMessageDesc() {
        return this.messageDesc;
    }

    public void setMessageDesc(String messageDesc) {
        this.messageDesc = messageDesc;
    }

    public String getPushTime() {
        return this.pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getConcernedSN() {
        return this.concernedSN;
    }

    public void setConcernedSN(String concernedSN) {
        this.concernedSN = concernedSN;
    }

    public String getMessageTitle() {
        return this.messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageUrl() {
        return this.messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getVehicleType() {
        return this.vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVersions() {
        return this.versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFaultType() {
        return this.faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }
}
