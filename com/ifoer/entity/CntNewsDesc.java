package com.ifoer.entity;

public class CntNewsDesc {
    protected String author;
    protected String createDate;
    protected Integer lanId;
    protected String mainKeyword;
    protected String newsDesc;
    protected Integer newsDescId;
    protected Integer newsId;
    protected Integer newsType;
    protected String secondKeyword;
    protected String subject;
    protected String updateDate;
    protected Integer validFlag;

    public String getAuthor() {
        return this.author;
    }

    public String getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getLanId() {
        return this.lanId;
    }

    public void setLanId(Integer value) {
        this.lanId = value;
    }

    public String getMainKeyword() {
        return this.mainKeyword;
    }

    public void setMainKeyword(String value) {
        this.mainKeyword = value;
    }

    public String getNewsDesc() {
        return this.newsDesc;
    }

    public void setNewsDesc(String value) {
        this.newsDesc = value;
    }

    public Integer getNewsDescId() {
        return this.newsDescId;
    }

    public void setNewsDescId(Integer value) {
        this.newsDescId = value;
    }

    public Integer getNewsId() {
        return this.newsId;
    }

    public void setNewsId(Integer value) {
        this.newsId = value;
    }

    public Integer getNewsType() {
        return this.newsType;
    }

    public void setNewsType(Integer value) {
        this.newsType = value;
    }

    public String getSecondKeyword() {
        return this.secondKeyword;
    }

    public void setSecondKeyword(String value) {
        this.secondKeyword = value;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String value) {
        this.subject = value;
    }

    public String getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(String value) {
        this.updateDate = value;
    }

    public Integer getValidFlag() {
        return this.validFlag;
    }

    public void setValidFlag(Integer value) {
        this.validFlag = value;
    }
}
