package com.ifoer.entity;

public class VIPUserInfoDTO {
    String auto_code;
    String car_brand_vin;
    String car_displacement;
    String car_engine_num;
    String car_gearbox_type;
    String car_producing_year;
    String car_type_id;
    String mileage;
    String mine_car_name;
    String mine_car_plate_num;
    String note;

    public String getMine_car_name() {
        return this.mine_car_name;
    }

    public void setMine_car_name(String mine_car_name) {
        this.mine_car_name = mine_car_name;
    }

    public String getCar_type_id() {
        return this.car_type_id;
    }

    public void setCar_type_id(String car_type_id) {
        this.car_type_id = car_type_id;
    }

    public String getCar_brand_vin() {
        return this.car_brand_vin;
    }

    public void setCar_brand_vin(String car_brand_vin) {
        this.car_brand_vin = car_brand_vin;
    }

    public String getMine_car_plate_num() {
        return this.mine_car_plate_num;
    }

    public void setMine_car_plate_num(String mine_car_plate_num) {
        this.mine_car_plate_num = mine_car_plate_num;
    }

    public String getAuto_code() {
        return this.auto_code;
    }

    public void setAuto_code(String auto_code) {
        this.auto_code = auto_code;
    }

    public String getCar_producing_year() {
        return this.car_producing_year;
    }

    public void setCar_producing_year(String car_producing_year) {
        this.car_producing_year = car_producing_year;
    }

    public String getCar_displacement() {
        return this.car_displacement;
    }

    public void setCar_displacement(String car_displacement) {
        this.car_displacement = car_displacement;
    }

    public String getCar_gearbox_type() {
        return this.car_gearbox_type;
    }

    public void setCar_gearbox_type(String car_gearbox_type) {
        this.car_gearbox_type = car_gearbox_type;
    }

    public String getCar_engine_num() {
        return this.car_engine_num;
    }

    public void setCar_engine_num(String car_engine_num) {
        this.car_engine_num = car_engine_num;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMileage() {
        return this.mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }
}
