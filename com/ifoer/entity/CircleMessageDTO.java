package com.ifoer.entity;

public class CircleMessageDTO {
    protected Integer circleId;
    protected Integer isRead;
    protected String messageContent;
    protected Integer messageId;
    protected String messageTitle;
    protected Integer messageType;
    protected Integer receiver;
    protected String sendTime;
    protected Integer sender;

    public Integer getCircleId() {
        return this.circleId;
    }

    public void setCircleId(Integer value) {
        this.circleId = value;
    }

    public Integer getIsRead() {
        return this.isRead;
    }

    public void setIsRead(Integer value) {
        this.isRead = value;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public void setMessageContent(String value) {
        this.messageContent = value;
    }

    public Integer getMessageId() {
        return this.messageId;
    }

    public void setMessageId(Integer value) {
        this.messageId = value;
    }

    public String getMessageTitle() {
        return this.messageTitle;
    }

    public void setMessageTitle(String value) {
        this.messageTitle = value;
    }

    public Integer getMessageType() {
        return this.messageType;
    }

    public void setMessageType(Integer value) {
        this.messageType = value;
    }

    public Integer getReceiver() {
        return this.receiver;
    }

    public void setReceiver(Integer value) {
        this.receiver = value;
    }

    public String getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSender() {
        return this.sender;
    }

    public void setSender(Integer value) {
        this.sender = value;
    }
}
