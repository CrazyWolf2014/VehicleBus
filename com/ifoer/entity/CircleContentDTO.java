package com.ifoer.entity;

public class CircleContentDTO {
    protected String addTime;
    protected Integer cc;
    protected Integer circleId;
    protected String content;
    protected Integer contentId;
    protected String editTime;
    protected Integer isEdit;
    protected Integer isValid;

    public Integer getCc() {
        return this.cc;
    }

    public void setCc(Integer value) {
        this.cc = value;
    }

    public Integer getCircleId() {
        return this.circleId;
    }

    public void setCircleId(Integer value) {
        this.circleId = value;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String value) {
        this.content = value;
    }

    public Integer getContentId() {
        return this.contentId;
    }

    public void setContentId(Integer value) {
        this.contentId = value;
    }

    public String getAddTime() {
        return this.addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getEditTime() {
        return this.editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public Integer getIsEdit() {
        return this.isEdit;
    }

    public void setIsEdit(Integer value) {
        this.isEdit = value;
    }

    public Integer getIsValid() {
        return this.isValid;
    }

    public void setIsValid(Integer value) {
        this.isValid = value;
    }
}
