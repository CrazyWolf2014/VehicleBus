package com.ifoer.mine.model;

public class Feedback {
    private String auto_code;
    private String nick_name;
    private String review_level;
    private String serial_no;
    private String thumb_url;
    private String updated;
    private String user_id;
    private String user_name;

    public String getSerial_no() {
        return this.serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getAuto_code() {
        return this.auto_code;
    }

    public void setAuto_code(String auto_code) {
        this.auto_code = auto_code;
    }

    public String getUpdated() {
        return this.updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReview_level() {
        return this.review_level;
    }

    public void setReview_level(String review_level) {
        this.review_level = review_level;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getThumb_url() {
        return this.thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }
}
