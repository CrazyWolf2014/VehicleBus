package com.ifoer.entity;

public class OneChatInfo {
    private boolean ifMy;
    private String message;
    private String name;
    private String time;

    public OneChatInfo(String time, String name, String message, boolean ifMy) {
        this.ifMy = false;
        this.time = time;
        this.name = name;
        this.message = message;
        this.ifMy = ifMy;
    }

    public OneChatInfo() {
        this.ifMy = false;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIfMy() {
        return this.ifMy;
    }

    public void setIfMy(boolean ifMy) {
        this.ifMy = ifMy;
    }
}
