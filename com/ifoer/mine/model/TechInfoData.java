package com.ifoer.mine.model;

import java.io.Serializable;
import java.util.List;

public class TechInfoData implements Serializable {
    private static final long serialVersionUID = -3748187834617335111L;
    private String address;
    private String apply_time;
    private List<String> available_ele_skill;
    private String city_code;
    private String city_name;
    private String country_code;
    private String country_name;
    private String email;
    private List<ExpertSkill> expert_ele_skill;
    private String expert_time;
    private String face;
    private List<Feedback> feedback;
    private String flag;
    private String identity_tag;
    private String introduce;
    private String is_bind_email;
    private String is_bind_mobile;
    private String maintenance_leve;
    private String mobile;
    private String nick_name;
    private String now_rolel;
    private String office_phone;
    private String province_code;
    private String province_name;
    private String refuse_time;
    private String roles;
    private String set_face_time;
    private String sex;
    private String signature;
    private String status;
    private String updated;
    private String user_id;
    private String user_name;
    private String work_unit;

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSet_face_time() {
        return this.set_face_time;
    }

    public void setSet_face_time(String set_face_time) {
        this.set_face_time = set_face_time;
    }

    public String getFace() {
        return this.face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getOffice_phone() {
        return this.office_phone;
    }

    public void setOffice_phone(String office_phone) {
        this.office_phone = office_phone;
    }

    public String getCountry_code() {
        return this.country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getProvince_code() {
        return this.province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getCity_code() {
        return this.city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIs_bind_mobile() {
        return this.is_bind_mobile;
    }

    public void setIs_bind_mobile(String is_bind_mobile) {
        this.is_bind_mobile = is_bind_mobile;
    }

    public String getIs_bind_email() {
        return this.is_bind_email;
    }

    public void setIs_bind_email(String is_bind_email) {
        this.is_bind_email = is_bind_email;
    }

    public String getCity_name() {
        return this.city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCountry_name() {
        return this.country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getProvince_name() {
        return this.province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getMaintenance_leve() {
        return this.maintenance_leve;
    }

    public void setMaintenance_leve(String maintenance_leve) {
        this.maintenance_leve = maintenance_leve;
    }

    public String getWork_unit() {
        return this.work_unit;
    }

    public void setWork_unit(String work_unit) {
        this.work_unit = work_unit;
    }

    public String getIntroduce() {
        return this.introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApply_time() {
        return this.apply_time;
    }

    public void setApply_time(String apply_time) {
        this.apply_time = apply_time;
    }

    public String getExpert_time() {
        return this.expert_time;
    }

    public void setExpert_time(String expert_time) {
        this.expert_time = expert_time;
    }

    public String getRefuse_time() {
        return this.refuse_time;
    }

    public void setRefuse_time(String refuse_time) {
        this.refuse_time = refuse_time;
    }

    public String getUpdated() {
        return this.updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<String> getAvailable_ele_skill() {
        return this.available_ele_skill;
    }

    public void setAvailable_ele_skill(List<String> available_ele_skill) {
        this.available_ele_skill = available_ele_skill;
    }

    public List<ExpertSkill> getExpert_ele_skill() {
        return this.expert_ele_skill;
    }

    public void setExpert_ele_skill(List<ExpertSkill> expert_ele_skill) {
        this.expert_ele_skill = expert_ele_skill;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<Feedback> getFeedback() {
        return this.feedback;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }

    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getNow_rolel() {
        return this.now_rolel;
    }

    public void setNow_rolel(String now_rolel) {
        this.now_rolel = now_rolel;
    }

    public String getIdentity_tag() {
        return this.identity_tag;
    }

    public void setIdentity_tag(String identity_tag) {
        this.identity_tag = identity_tag;
    }
}
