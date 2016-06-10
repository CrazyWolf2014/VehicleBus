package com.ifoer.mine;

import android.widget.ImageView;
import java.io.Serializable;

public class Contact implements Serializable {
    public static final String RELATION_AGREE = "5";
    public static final String RELATION_ASK = "0";
    public static final String RELATION_BACKNAME = "2";
    public static final String RELATION_FRIEND = "1";
    public static final String RELATION_NOAGREE = "4";
    public static final String RELATION_NODONE = "3";
    private static final long serialVersionUID = 1;
    private boolean clickFlag;
    private String code;
    private ContactDetail contactDetail;
    private double distance;
    private String face_thumb;
    private ImageView headView;
    private String header_path;
    private String id;
    private double lat;
    private double lon;
    private String msg;
    private String name;
    private String nickName;
    private String ownerId;
    private String purikuraUrl;
    private String relation;
    private String remark;
    private int sex;
    private String signature;
    private String sortKey;
    private String type;

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getFace_thumb() {
        return this.face_thumb;
    }

    public void setFace_thumb(String face_thumb) {
        this.face_thumb = face_thumb;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSortKey() {
        return this.sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRelation() {
        return this.relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getPurikuraUrl() {
        return this.purikuraUrl;
    }

    public void setPurikuraUrl(String purikuraUrl) {
        this.purikuraUrl = purikuraUrl;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getHeader_path() {
        return this.header_path;
    }

    public void setHeader_path(String header_path) {
        this.header_path = header_path;
    }

    public ImageView getHeadView() {
        return this.headView;
    }

    public void setHeadView(ImageView headView) {
        this.headView = headView;
    }

    public ContactDetail getContactDetail() {
        return this.contactDetail;
    }

    public void setContactDetail(ContactDetail contactDetail) {
        this.contactDetail = contactDetail;
    }

    public boolean isClickFlag() {
        return this.clickFlag;
    }

    public void setClickFlag(boolean clickFlag) {
        this.clickFlag = clickFlag;
    }
}
