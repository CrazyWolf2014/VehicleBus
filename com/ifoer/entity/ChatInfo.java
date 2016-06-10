package com.ifoer.entity;

public class ChatInfo {
    private String cc;
    private String fromCC;
    private String fromName;
    private boolean image;
    private String message;
    private boolean mySend;
    private String roomID;
    private String time;
    private String toCC;
    private String toName;
    private String userName;

    public ChatInfo() {
        this.image = false;
    }

    public String getCc() {
        return this.cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomID() {
        return this.roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromCC() {
        return this.fromCC;
    }

    public void setFromCC(String fromCC) {
        this.fromCC = fromCC;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMySend() {
        return this.mySend;
    }

    public void setMySend(boolean mySend) {
        this.mySend = mySend;
    }

    public boolean isImage() {
        return this.image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public String getToName() {
        return this.toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToCC() {
        return this.toCC;
    }

    public void setToCC(String toCC) {
        this.toCC = toCC;
    }
}
