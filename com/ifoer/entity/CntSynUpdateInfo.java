package com.ifoer.entity;

public class CntSynUpdateInfo {
    private String content;
    private String fstrUser;
    private int id;
    private int lanId;
    private String lastUpdateDate;
    private String name;
    private String oaAuditNo;
    private int synId;

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFstrUser() {
        return this.fstrUser;
    }

    public void setFstrUser(String fstrUser) {
        this.fstrUser = fstrUser;
    }

    public String getOaAuditNo() {
        return this.oaAuditNo;
    }

    public void setOaAuditNo(String oaAuditNo) {
        this.oaAuditNo = oaAuditNo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastUpdateDate() {
        return this.lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLanId() {
        return this.lanId;
    }

    public void setLanId(int lanId) {
        this.lanId = lanId;
    }

    public int getSynId() {
        return this.synId;
    }

    public void setSynId(int synId) {
        this.synId = synId;
    }
}
