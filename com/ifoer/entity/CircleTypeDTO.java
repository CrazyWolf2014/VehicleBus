package com.ifoer.entity;

public class CircleTypeDTO {
    protected String circleDesc;
    protected Integer circleTypeId;
    protected String circleTypeName;
    protected String createTime;

    public String getCircleDesc() {
        return this.circleDesc;
    }

    public void setCircleDesc(String value) {
        this.circleDesc = value;
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
