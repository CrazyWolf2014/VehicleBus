package com.ifoer.entity;

public class CircleInfoDTO {
    protected String circleAdmin;
    protected String circleDesc;
    protected Integer circleId;
    protected String circleName;
    protected Integer circleTypeId;
    protected String circleTypeName;
    protected String createTime;
    private boolean isJoined;

    public CircleInfoDTO() {
        this.isJoined = false;
    }

    public boolean isJoined() {
        return this.isJoined;
    }

    public void setJoined(boolean isJoined) {
        this.isJoined = isJoined;
    }

    public String getCircleAdmin() {
        return this.circleAdmin;
    }

    public void setCircleAdmin(String value) {
        this.circleAdmin = value;
    }

    public String getCircleDesc() {
        return this.circleDesc;
    }

    public void setCircleDesc(String value) {
        this.circleDesc = value;
    }

    public Integer getCircleId() {
        return this.circleId;
    }

    public void setCircleId(Integer value) {
        this.circleId = value;
    }

    public String getCircleName() {
        return this.circleName;
    }

    public void setCircleName(String value) {
        this.circleName = value;
    }

    public Integer getCircleTypeId() {
        return this.circleTypeId;
    }

    public void setCircleTypeId(Integer value) {
        this.circleTypeId = value;
    }

    public String getCircleTypeName() {
        return this.circleTypeName;
    }

    public void setCircleTypeName(String value) {
        this.circleTypeName = value;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
